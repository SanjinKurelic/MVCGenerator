package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CanNotCastTypeSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CheckConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CheckExpressionNotBooleanException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ReferenceConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TypeMismatchSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.UnsupportedOperandTypeSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.UnsupportedPredicateTypeSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateTableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.ColumnDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableConstraintDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElement;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.CheckConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.Constraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.ConstraintList;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.NotNullConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.PrimaryKeyConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.ReferenceConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.UniqueConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ConstantOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.DataTypeOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.IntervalDataTypeOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.KeywordOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ZonedDataTypeOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SemanticParser {

  private SyntaxTree syntaxTree;
  private SemanticAttributeTable semanticAttributeTable;

  public SemanticParser(SyntaxTree syntaxTree) {
    this.syntaxTree = syntaxTree;
  }

  public void parse() throws SemanticException {
    semanticAttributeTable = new SemanticAttributeTable();
    fillSemanticAttributeTable();
    analyzeForeignKeys();
    analyzeCheckClause();
  }

  private void fillSemanticAttributeTable() throws SemanticException {
    TableAttribute tableAttribute;
    for (CreateDefinition createDefinition : syntaxTree.getCreateDefinitions()) {
      if (createDefinition instanceof CreateTableDefinition) {
        tableAttribute = getTableAttribute((CreateTableDefinition) createDefinition);
        if (semanticAttributeTable.hasTable(tableAttribute)) {
          throw new TableAlreadyDefinedSemanticException(tableAttribute.getTableName());
        }
        semanticAttributeTable.addTable(tableAttribute);
      }
    }
  }

  private TableAttribute getTableAttribute(CreateTableDefinition createTableDefinition) throws SemanticException {
    TableAttribute tableAttribute = new TableAttribute();
    tableAttribute.setTableName(createTableDefinition.getTableDefinition().getTableName());

    // Fill all columns
    ArrayList<TableConstraintDefinition> tableConstraintDefinitions = new ArrayList<>();
    ColumnAttribute columnAttribute;
    for (TableElement tableElement : createTableDefinition.getTableDefinition().getTableElementList()) {
      if (tableElement instanceof ColumnDefinition) {
        columnAttribute = getColumnAttribute((ColumnDefinition) tableElement);
        if (tableAttribute.hasColumn(columnAttribute)) {
          throw new ColumnAlreadyDefinedSemanticException(columnAttribute.getColumnName());
        }
        tableAttribute.addColumn(columnAttribute);
        // Collect check constraints from column
        fillCheckAttribute(tableAttribute, ((ColumnDefinition) tableElement).getColumnName(), ((ColumnDefinition) tableElement).getConstraintList());
      } else if (tableElement instanceof TableConstraintDefinition) {
        tableConstraintDefinitions.add((TableConstraintDefinition) tableElement);
      }
    }

    // Parse table scope constraints
    int columnIndex;
    for (TableConstraintDefinition tableConstraint : tableConstraintDefinitions) {
      // Check constraint
      if (tableConstraint.getConstraintType() instanceof CheckConstraint) {
        tableAttribute.addCheckExpression(((CheckConstraint) tableConstraint.getConstraintType()).getCheckExpression());
      }
      // Add table constraint to column attributes
      for (Token column : tableConstraint.getColumnList()) {
        columnIndex = 0;
        if (!tableAttribute.hasColumn(column.getValue())) {
          throw new ColumnUndefinedSemanticException(column);
        }
        fillConstraintAttributes(tableAttribute.getColumn(column.getValue()), tableConstraint.getConstraintType(), columnIndex);
        ++columnIndex;
      }
    }

    return tableAttribute;
  }

  /**
   * Fill column attributes
   *
   * @param columnDefinition - syntax tree element from which we use data
   */
  private ColumnAttribute getColumnAttribute(ColumnDefinition columnDefinition) throws SemanticException {
    ColumnAttribute columnAttribute = new ColumnAttribute();

    columnAttribute.setColumnName(columnDefinition.getColumnName());
    columnAttribute.setDataType(DataTypeAttribute.convertToDataTypeAttribute(columnDefinition.getDataType()));
    columnAttribute.setLength(Integer.valueOf(columnDefinition.getDataType().getPrecisionOrLength().getValue()));
    columnAttribute.setDefaultValue(columnDefinition.getDefaultValue());
    fillConstraintAttributes(columnAttribute, columnDefinition.getConstraintList());

    return columnAttribute;
  }

  /**
   * Copy CHECK constraint from column definition to table definition, if column has any
   *
   * @param tableAttribute - table scope
   * @param columnName     - column name for semantic errors
   * @param constraintList - list of column constraints - only check constraints are considered
   * @throws SemanticException - if there are multiple check constraint
   */
  private void fillCheckAttribute(TableAttribute tableAttribute, Token columnName, ConstraintList constraintList) throws SemanticException {
    // No check clauses defined on column
    if (constraintList.stream().noneMatch(CheckConstraint.class::isInstance)) {
      return;
    }
    Supplier<Stream<CheckConstraint>> checkConstraintStreamSupplier = () -> constraintList.stream()
        .filter(CheckConstraint.class::isInstance)
        .map(CheckConstraint.class::cast);
    // Only one check clause is allowed per column constraint definitions
    if (checkConstraintStreamSupplier.get().count() != 1) {
      throw new CheckConstraintAlreadyDefinedSemanticException(columnName);
    }
    tableAttribute.addCheckExpression(checkConstraintStreamSupplier.get().findFirst().orElseThrow().getCheckExpression());
  }

  /**
   * Used when we copy constraints from table scope to column scope
   */
  private void fillConstraintAttributes(ColumnAttribute columnAttribute, Constraint constraint, int referencedIndex) throws SemanticException {
    fillConstraintAttributes(columnAttribute, new ConstraintList(constraint), referencedIndex);
  }

  /**
   * Used when we fill info about column
   */
  private void fillConstraintAttributes(ColumnAttribute columnAttribute, ConstraintList constraintList) throws SemanticException {
    fillConstraintAttributes(columnAttribute, constraintList, 0);
  }

  /**
   * Set column attribute information about constraints (primary, unique, not null etc)
   *
   * @param columnAttribute - column in which we fill info
   * @param constraintList  - list of constraints
   * @param referenceIndex  - used when we define reference constraint in table scope
   * @throws SemanticException - if we define multiple reference clause on column
   */
  private void fillConstraintAttributes(ColumnAttribute columnAttribute, ConstraintList constraintList, int referenceIndex) throws SemanticException {
    columnAttribute.setNotNull(constraintList.stream().anyMatch(PrimaryKeyConstraint.class::isInstance));
    columnAttribute.setNotNull(constraintList.stream().anyMatch(UniqueConstraint.class::isInstance));
    columnAttribute.setNotNull(constraintList.stream().anyMatch(NotNullConstraint.class::isInstance));

    // Reference constraint
    if (constraintList.stream().anyMatch(ReferenceConstraint.class::isInstance)) {
      Supplier<Stream<ReferenceConstraint>> referenceConstraintStreamSupplier = () -> constraintList.stream()
          .filter(ReferenceConstraint.class::isInstance)
          .map(ReferenceConstraint.class::cast);
      // Can't redefine reference constraint
      if (columnAttribute.isForeign() || (referenceConstraintStreamSupplier.get().count() != 1)) {
        throw new ReferenceConstraintAlreadyDefinedSemanticException(columnAttribute.getColumnName());
      }
      ReferenceConstraint constraint = referenceConstraintStreamSupplier.get().findFirst().orElseThrow();
      columnAttribute.setForeign(true);
      columnAttribute.setForeignColumn(constraint.getColumnList().get(referenceIndex));
      columnAttribute.setForeignTable(constraint.getTableName());
      columnAttribute.setForeignUpdateAction(constraint.getUpdateAction());
      columnAttribute.setForeignDeleteAction(constraint.getDeleteAction());
    }
  }

  /**
   * Analyze all foreign keys
   * @throws SemanticException - table or column destination doesn't exists
   */
  private void analyzeForeignKeys() throws SemanticException {
    String tableName, columnName;
    // For every table and every column that is reference key check if table/column destination exists
    for (TableAttribute table : semanticAttributeTable.getTables().values()) {
      for (ColumnAttribute column : table.getColumns().values()) {
        if (column.isForeign()) {
          tableName = column.getForeignTable().getValue();
          columnName = column.getForeignColumn().getValue();
          // Does table exists
          if (!semanticAttributeTable.hasTable(tableName)) {
            throw new TableUndefinedSemanticException(column.getForeignTable());
          }
          // Does column in that table exists
          if (!semanticAttributeTable.getTable(tableName).hasColumn(columnName)) {
            throw new ColumnUndefinedSemanticException(column.getForeignColumn());
          }
          // Check if data type is correct
          ColumnAttribute foreignColumn = semanticAttributeTable.getTable(tableName).getColumn(columnName);
          if (!foreignColumn.getDataType().equals(column.getDataType())) {
            throw new TypeMismatchSemanticException(column, foreignColumn);
          }
        }
      }
    }
  }

  /**
   * Analyze check clause
   * @throws SemanticException - there is type mismatch
   */
  private void analyzeCheckClause() throws SemanticException {
    for (Map.Entry<String, TableAttribute> table : semanticAttributeTable.getTables().entrySet()) {
      for (Expression checkExpression : table.getValue().getCheckAttribute().getCheckExpressions()) {
        // Every Check statement must return boolean value
        if (analyzeCheckPredicate((Predicate) checkExpression) != DataTypeAttribute.BOOLEAN) {
          throw new CheckExpressionNotBooleanException(((Predicate) checkExpression).getOperator().getLineNumber());
        }
      }
    }
  }

  /**
   * <check>{n} -> <unary expression>{q} | <binary expression>{q}
   * { q <- n }
   */
  private DataTypeAttribute analyzeCheckPredicate(Predicate predicate) throws SemanticException {
    if (predicate instanceof UnaryPredicate) {
      return analyzeUnaryExpression((UnaryPredicate) predicate);
    } else {
      return analyzeBinaryExpression((BinaryPredicate) predicate);
    }
  }

  /**
   * <unary expression>{n} -> <not predicate>{q} | <plus-minus predicate>{q}
   * { q <- n }
   */
  private DataTypeAttribute analyzeUnaryExpression(UnaryPredicate predicate) throws SemanticException {
    if (predicate.getOperator().getOperator().equals(SpecialCharacterToken.NOT)) {
      return analyzeNotPredicate(predicate);
    }
    else if (predicate.getOperator().getOperator().equals(SpecialCharacterToken.PLUS)
      || predicate.getOperator().getOperator().equals(SpecialCharacterToken.MINUS)) {
      return analyzePlusMinusPredicate(predicate);
    }
    throw new UnsupportedPredicateTypeSemanticException(predicate.getClass());
  }

  /**
   * <not predicate>{n} -> NOT ( <boolean>{q} | <date-time>{p} | <numeric>{k} | <string>{k} )
   * { n <- BOOLEAN, p <- cast(cast(p, INTEGER), BOOLEAN), k <- cast(k, BOOLEAN)}
   */
  private DataTypeAttribute analyzeNotPredicate(UnaryPredicate predicate) throws SemanticException {
    DataTypeAttribute dataTypeAttribute = analyzeCheckExpression(predicate.getExpression());
    if (DataTypeAttribute.isDateTimeType(dataTypeAttribute)) {
      predicate.setExpression(cast(cast(predicate.getExpression(), DataTypeAttribute.INTEGER), DataTypeAttribute.BOOLEAN));
    }
    else if (DataTypeAttribute.isNumber(dataTypeAttribute) || DataTypeAttribute.STRING.equals(dataTypeAttribute)) {
      predicate.setExpression(cast(predicate.getExpression(), DataTypeAttribute.BOOLEAN));
    }
    else if (!DataTypeAttribute.BOOLEAN.equals(dataTypeAttribute)) {
      throw new TypeMismatchSemanticException(dataTypeAttribute, predicate.getOperator().getLineNumber(), DataTypeAttribute.BOOLEAN);
    }
    return DataTypeAttribute.BOOLEAN;
  }

  /**
   * <plus-minus predicate>{n} -> ( + | - ) ( <numeric>{q} | <boolean>{p} | <string>{p} | <date-time>{p} )
   * n <- q, n <- cast(p, INTEGER)
   */
  private DataTypeAttribute analyzePlusMinusPredicate(UnaryPredicate predicate) throws SemanticException {
    DataTypeAttribute dataTypeAttribute = analyzeCheckExpression(predicate.getExpression());
    if (DataTypeAttribute.isNumber(dataTypeAttribute)) {
      return dataTypeAttribute;
    }
    else if (DataTypeAttribute.isDateTimeType(dataTypeAttribute) || DataTypeAttribute.BOOLEAN.equals(dataTypeAttribute)
      || DataTypeAttribute.STRING.equals(dataTypeAttribute)) {
      predicate.setExpression(cast(predicate.getExpression(), DataTypeAttribute.INTEGER));
    }
    else {
      throw new TypeMismatchSemanticException(dataTypeAttribute, predicate.getOperator().getLineNumber(),
        DataTypeAttribute.INTEGER, DataTypeAttribute.REAL);
    }

    return DataTypeAttribute.INTEGER;
  }

  /**
   * <binary expression>{n} -> <like-concat predicate>{q} | <binary predicate>{q} | <rational predicate>{q}
   * { n <- q }
   */
  private DataTypeAttribute analyzeBinaryExpression(BinaryPredicate predicate) throws SemanticException {
    switch (predicate.getOperator().getOperator().getRootType()) {
      case BINARY:
        return analyzeBinaryPredicate(predicate);
      case RATIONAL:
        return analyzeRationalPredicate(predicate);
      default:
        return analyzeLikeConcatPredicate(predicate);
    }
  }

  /**
   * <binary predicate>{n} -> <operand1>{p} <binary operator> <operand2>{q}
   * { n <- REAL for p = REAL or/and q = REAL, n <- INTEGER for p = INTEGER and q = INTEGER }
   * { p <- cast(p, INTEGER) for p != INTEGER and p != REAL, q <- cast(q, INTEGER) for q != INTEGER and q != REAL }
   */
  private DataTypeAttribute analyzeBinaryPredicate(BinaryPredicate predicate) throws SemanticException {
    DataTypeAttribute dataTypeAttribute1 = analyzeCheckExpression(predicate.getFirstExpression());
    DataTypeAttribute dataTypeAttribute2 = analyzeCheckExpression(predicate.getSecondExpression());

    if (!DataTypeAttribute.isNumber(dataTypeAttribute1)) {
      predicate.setFirstExpression(cast(predicate.getFirstExpression(), DataTypeAttribute.INTEGER));
    }
    if (!DataTypeAttribute.isNumber(dataTypeAttribute2)) {
      predicate.setSecondExpression(cast(predicate.getSecondExpression(), DataTypeAttribute.INTEGER));
    }

    if (DataTypeAttribute.REAL.equals(dataTypeAttribute1) || DataTypeAttribute.REAL.equals(dataTypeAttribute2)) {
      return DataTypeAttribute.REAL;
    }
    return DataTypeAttribute.INTEGER;
  }

  /**
   * <rational predicate>{n} -> <operand1>{p} <rational operator> <operand2>{q}
   * { n <- BOOLEAN, q <- q and p <- p for q = p }
   * { q <- cast(q, STRING) for q != BOOLEAN and p = STRING  }
   * { q <- cast(cast(q, INTEGER), STRING) for q = BOOLEAN and p = STRING }
   * { q <- cast(q, INTEGER) for q = BOOLEAN and p = INTEGER }
   * { q <- cast(cast(q, INTEGER), REAL) for q = BOOLEAN and p = REAL }
   * { q <- cast(q, INTEGER), p <- cast(p, INTEGER) for q = BOOLEAN and p = DATE-TIME }
   * { q <- cast(q, INTEGER) for q = DATE-TIME and p = NUMERIC }
   * { q <- cast(q, REAL) for q = INTEGER and p = REAL }
   * { q <- cast(q, DATE) for (q = DATETIME or q = TIMESTAMP) and p = DATE }
   * { q <- cast(q, TIME) for (q = DATETIME or q = TIMESTAMP) and p = TIME }
   * { q <- cast(q, DATETIME) for q = TIMESTAMP and p = DATETIME }
   */
  private DataTypeAttribute analyzeRationalPredicate(BinaryPredicate predicate) throws SemanticException {
    DataTypeAttribute q = analyzeCheckExpression(predicate.getFirstExpression());
    DataTypeAttribute p = analyzeCheckExpression(predicate.getSecondExpression());
    boolean swapped = false;

    // No need for casting
    if (p.equals(q)) {
      return DataTypeAttribute.BOOLEAN;
    }
    // Swap variables if second data type is higher order than first data type
    if (p.getOrder() > q.getOrder()) {
      DataTypeAttribute temp = q;
      q = p;
      p = temp;
      swapped = true;
    }
    // validate & cast
    if (DataTypeAttribute.STRING.equals(p) && !DataTypeAttribute.BOOLEAN.equals(q)) {
      // cast to string
      castHigherOrderType(predicate, DataTypeAttribute.STRING, swapped);
    }
    else if (DataTypeAttribute.STRING.equals(p)) { // DataTypeAttribute.BOOLEAN.equals(q) is already true
       // cast to integer and then cast to string
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, swapped);
      castHigherOrderType(predicate, DataTypeAttribute.STRING, swapped);
    }
    else if (DataTypeAttribute.INTEGER.equals(p) && DataTypeAttribute.BOOLEAN.equals(q)) {
      // cast to integer
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, swapped);
    }
    else if (DataTypeAttribute.REAL.equals(p) && DataTypeAttribute.BOOLEAN.equals(q)) {
      // cast to integer then cast to real
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, swapped);
      castHigherOrderType(predicate, DataTypeAttribute.REAL, swapped);
    }
    else if (DataTypeAttribute.isDateTimeType(p) && DataTypeAttribute.BOOLEAN.equals(q)) {
      // q cast to integer, p cast to integer
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, swapped);
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, !swapped);
    }
    else if (DataTypeAttribute.isNumber(p) && DataTypeAttribute.isDateTimeType(q)) {
      // cast to integer
      castHigherOrderType(predicate, DataTypeAttribute.INTEGER, swapped);
    }
    else if (DataTypeAttribute.REAL.equals(p) && DataTypeAttribute.INTEGER.equals(q)) {
      // cast to real
      castHigherOrderType(predicate, DataTypeAttribute.REAL, swapped);
    }
    else if (DataTypeAttribute.DATE.equals(p) && DataTypeAttribute.isDateAndTimeType(q)) {
      // cast to date
      castHigherOrderType(predicate, DataTypeAttribute.DATE, swapped);
    }
    else if (DataTypeAttribute.TIME.equals(p) && DataTypeAttribute.isDateAndTimeType(q)) {
      // cast to time
      castHigherOrderType(predicate, DataTypeAttribute.TIME, swapped);
    }
    else if (DataTypeAttribute.DATETIME.equals(p) && DataTypeAttribute.TIMESTAMP.equals(q)) {
      // cast to date time
      castHigherOrderType(predicate, DataTypeAttribute.DATETIME, swapped);
    }
    else {
      throw new CanNotCastTypeSemanticException(p, q, predicate.getOperator().getLineNumber());
    }

    return DataTypeAttribute.BOOLEAN;
  }

  /**
   * <like-concat predicate>{n} -> <operand1>{p} ( [ NOT ] LIKE | <concat operator> ) <operand2>{q}
   * { n <- STRING, p <- cast(p, STRING) for p != STRING, q <- cast(q, STRING) for q != STRING }
   */
  private DataTypeAttribute analyzeLikeConcatPredicate(BinaryPredicate predicate) throws SemanticException {
    predicate.setFirstExpression(castLikeConcatOperand(predicate.getFirstExpression()));
    predicate.setSecondExpression(castLikeConcatOperand(predicate.getSecondExpression()));

    return DataTypeAttribute.STRING;
  }

  /**
   * Helper method
   * @param expression - first & second
   * @return - expression or cast expression depending if data type is string
   */
  private Expression castLikeConcatOperand(Expression expression) throws SemanticException {
    DataTypeAttribute dataTypeAttribute = analyzeCheckExpression(expression);
    if (!DataTypeAttribute.STRING.equals(dataTypeAttribute)) {
      return cast(expression, DataTypeAttribute.STRING);
    }
    return expression;
  }

  /**
   * Get data type of operand
   */
  private DataTypeAttribute analyzeCheckOperand(Operand operand) throws SemanticException {
    if (operand instanceof ColumnAttribute) {
      return ((ColumnAttribute) operand).getDataType();
    }
    else if (operand instanceof ConstantOperand) {
      switch (operand.getOperand().getTokenType()) {
        case CONSTANT_INTEGER_VALUE:
          return DataTypeAttribute.INTEGER;
        case CONSTANT_REAL_NUMBER_VALUE:
          return DataTypeAttribute.REAL;
        case CONSTANT_QUOTED_VALUE:
          return DataTypeAttribute.STRING;
      }
    }
    else if (operand instanceof KeywordOperand) {
      String value = operand.getOperand().getValue().toLowerCase();
      if (value.equals(KeywordToken.TRUE.toString().toLowerCase()) || value.equals(KeywordToken.FALSE.toString().toLowerCase())) {
        return DataTypeAttribute.BOOLEAN;
      }
    }
    else if (operand instanceof IntervalDataTypeOperand) {
      return DataTypeAttribute.INTERVAL;
    }
    else if (operand instanceof ZonedDataTypeOperand) {
      return DataTypeAttribute.ZONED_DATETIME;
    }
    else if (operand instanceof DataTypeOperand) {
      return DataTypeAttribute.convertToDataTypeAttribute(operand.getOperand().getValue());
    }
    throw new UnsupportedOperandTypeSemanticException(operand.getClass());
  }

  /**
   * Helper method to determine if expression is predicate or operand
   */
  private DataTypeAttribute analyzeCheckExpression(Expression expression) throws SemanticException {
    if (expression instanceof Predicate) {
      return analyzeCheckPredicate((Predicate) expression);
    } else {
      return analyzeCheckOperand((Operand) expression);
    }
  }

  private void castHigherOrderType(BinaryPredicate predicate, DataTypeAttribute toDataType, boolean secondIsHigher) {
    if (secondIsHigher) {
      predicate.setSecondExpression(cast(predicate.getSecondExpression(), toDataType));
    } else {
      predicate.setFirstExpression(cast(predicate.getFirstExpression(), toDataType));
    }
  }

  /**
   * Helper method for building cast expression
   * @param castExpression - expression that we need to cast
   * @param toDataType - cast to this data type
   * @return - cast expression
   */
  private Expression cast(Expression castExpression, DataTypeAttribute toDataType) {
    BinaryPredicate castPredicate = new BinaryPredicate();
    castPredicate.setFirstExpression(castExpression);
    // Operator
    Operator castOperator = new Operator();
    castOperator.setOperator(SpecialCharacterToken.CAST);
    int lineNumber = 0;
    if (castExpression instanceof Predicate) {
      lineNumber = ((Predicate) castExpression).getOperator().getLineNumber();
    } else if (castExpression instanceof Operand) {
      lineNumber = ((Operand) castExpression).getOperand().getLineNumber();
    }
    castOperator.setLineNumber(lineNumber);
    castPredicate.setOperator(castOperator);
    // To data type
    Token dataToken = new Token(TokenType.DATA_TYPE, toDataType.name());
    DataTypeOperand toDataTypeOperand = new DataTypeOperand(new DataType(dataToken));
    castPredicate.setSecondExpression(toDataTypeOperand);

    return castPredicate;
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticAttributeTable;
  }
}
