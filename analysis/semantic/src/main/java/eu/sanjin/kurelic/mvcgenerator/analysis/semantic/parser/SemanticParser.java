package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.CheckConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ColumnUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.ReferenceConstraintAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableAlreadyDefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TableUndefinedSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.TypeMismatchSemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateTableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.ColumnDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableConstraintDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElement;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.CheckConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.Constraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.ConstraintList;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.NotNullConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.PrimaryKeyConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.ReferenceConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.UniqueConstraint;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ColumnOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ConstantOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;

import java.util.ArrayList;
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
    columnAttribute.setDataType(columnDefinition.getDataType());
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
          if (!foreignColumn.getDataType().getType().getValue().equals(column.getDataType().getType().getValue())) {
            throw new TypeMismatchSemanticException(column, foreignColumn);
          }
        }
      }
    }
  }

  private void analyzeCheckClause() throws SemanticException {
    semanticAttributeTable.getTables().forEach((tableName, tableAttribute) -> {
      tableAttribute.getCheckAttribute().getCheckExpressions().forEach(expression -> {
        analyzeCheckPredicate((Predicate) expression);
      });
    });
    // TODO check check clauses type and if column exists, for every table
  }

  private void analyzeCheckExpression(Expression expression) {
    if (expression instanceof Predicate) {
      analyzeCheckPredicate((Predicate) expression);
    } else if (expression instanceof Operand) {
      analyzeCheckOperand((Operand) expression);
    }
  }

  private void analyzeCheckPredicate(Predicate predicate) {
    if (predicate instanceof UnaryPredicate) {
      analyzeUnaryPredicate((UnaryPredicate) predicate);
    } else if (predicate instanceof BinaryPredicate) {
      analyzeBinaryPredicate((BinaryPredicate) predicate);
    }
  }

  private void analyzeCheckOperand(Operand operand) {
    if (operand instanceof ConstantOperand) {
      ((ConstantOperand) operand).getOperand().getTokenType();
    }
    //((ColumnOperand)operand).getOperand()
  }

  private void analyzeUnaryPredicate(UnaryPredicate predicate) {
    switch (predicate.getOperator().getOperator()) {
      case NOT:
      case PLUS:
      case MINUS:
    }
  }

  private void analyzeBinaryPredicate(BinaryPredicate predicate) {
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

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticAttributeTable;
  }
}
