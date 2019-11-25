package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CheckConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CheckExpressionNotBooleanException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ReferenceConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TypeMismatchSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.CheckAttribute;
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

  private DataTypeAttribute analyzeCheckExpression(Expression expression) throws SemanticException {
    if (expression instanceof Predicate) {
      return analyzeCheckPredicate((Predicate) expression);
    }
    if (expression instanceof Operand) {
      return analyzeCheckOperand((Operand) expression);
    }
    return null;
  }

  private DataTypeAttribute analyzeCheckPredicate(Predicate predicate) throws SemanticException {
    if (predicate instanceof UnaryPredicate) {
      return analyzeUnaryPredicate((UnaryPredicate) predicate);
    }
    if (predicate instanceof BinaryPredicate) {
      return analyzeBinaryPredicate((BinaryPredicate) predicate);
    }
    return null;
  }

  private DataTypeAttribute analyzeUnaryPredicate(UnaryPredicate predicate) throws SemanticException {
    DataTypeAttribute dataTypeAttribute = analyzeCheckExpression(predicate.getExpression());
    int lineNumber = predicate.getOperator().getLineNumber();
    switch (predicate.getOperator().getOperator()) {
      case NOT:
        if (dataTypeAttribute != DataTypeAttribute.BOOLEAN) {
          throw new TypeMismatchSemanticException(dataTypeAttribute, lineNumber, DataTypeAttribute.BOOLEAN);
        }
        return DataTypeAttribute.BOOLEAN;
      case PLUS:
      case MINUS:
        if (dataTypeAttribute != DataTypeAttribute.REAL && dataTypeAttribute != DataTypeAttribute.INTEGER) {
          throw new TypeMismatchSemanticException(dataTypeAttribute, lineNumber, DataTypeAttribute.INTEGER, DataTypeAttribute.REAL);
        }
        return dataTypeAttribute;
    }
    return null;
  }

  private DataTypeAttribute analyzeBinaryPredicate(BinaryPredicate predicate) throws SemanticException {
    switch (predicate.getOperator().getOperator().getRootType()) {
      case BINARY:
      case RATIONAL:
      case STRING_MANIPULATION:
      case COMPOUND:
        switch (predicate.getOperator().getOperator()) {
          case NOT_LIKE:
          case LIKE:
          case CONCAT:
          case OVERLAPS:
        }
    }
  }

  private Expression analyzeCheckOperand(Operand operand, DataTypeAttribute dataTypeAttribute) throws SemanticException {
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
    return null;
  }

  private Expression castToBoolean(Operand operand, DataTypeAttribute fromDataType) {
    if (fromDataType.equals(DataTypeAttribute.BOOLEAN)) {
      return operand;
    }
    BinaryPredicate cast = new BinaryPredicate();
    cast.setOperator(new Operator(SpecialCharacterToken.CAST, operand.getOperand().getLineNumber()));
    cast.setSecondExpression(buildDataTypeOperand(DataTypeAttribute.BOOLEAN));
    switch (fromDataType) {
      case DATE:
      case DATETIME:
      case TIME:
      case TIMESTAMP:
      case INTERVAL:
      case ZONED_DATETIME:
        cast.setFirstExpression(castToInteger(operand, fromDataType));
        break;
      case STRING:
        if (operand.getOperand().getValue().equalsIgnoreCase("TRUE")) {

        }
      default:
        cast.setFirstExpression(operand);
        break;
    }
    return cast;
  }

  private Expression castToInteger(Operand operand, DataTypeAttribute fromDataType) {

  }

  private void implicitCastString(Expression expression, DataTypeAttribute toDataType) {

  }

  private DataTypeOperand buildDataTypeOperand(DataTypeAttribute dataTypeAttribute) {
    return new DataTypeOperand(new DataType(new Token(TokenType.DATA_TYPE, dataTypeAttribute.name())));
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticAttributeTable;
  }
}
