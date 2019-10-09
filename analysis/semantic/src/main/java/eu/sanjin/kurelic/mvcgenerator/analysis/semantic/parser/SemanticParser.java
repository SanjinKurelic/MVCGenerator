package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.*;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.*;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateTableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.ColumnDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableConstraintDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElement;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.*;

import java.util.ArrayList;
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
        if (semanticAttributeTable.hasTableAttribute(tableAttribute)) {
          throw new TableAlreadyDefinedSemanticException(tableAttribute.getTableName());
        }
        semanticAttributeTable.addTableAttribute(tableAttribute);
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
   * @param columnDefinition - syntax tree element from which we use data
   */
  private ColumnAttribute getColumnAttribute(ColumnDefinition columnDefinition) throws SemanticException {
    ColumnAttribute columnAttribute = new ColumnAttribute();

    columnAttribute.setColumnName(columnDefinition.getColumnName());
    columnAttribute.setDataType(columnAttribute.getDataType());
    columnAttribute.setDefaultValue(columnAttribute.getDefaultValue());
    fillConstraintAttributes(columnAttribute, columnDefinition.getConstraintList());

    return columnAttribute;
  }

  /**
   * Copy check constraint from column definition to table definition, if column has any
   * @param tableAttribute - table scope
   * @param columnName - column name for semantic errors
   * @param constraintList - list of column constraints - only check constraints are considered
   * @throws SemanticException - if there are multiple check constraint
   */
  private void fillCheckAttribute(TableAttribute tableAttribute, Token columnName, ConstraintList constraintList) throws SemanticException {
    // No check clauses defined on column
    if (constraintList.stream().noneMatch(CheckConstraint.class::isInstance)) {
      return;
    }
    Stream<CheckConstraint> checkConstraintStream = constraintList.stream()
        .filter(CheckConstraint.class::isInstance)
        .map(CheckConstraint.class::cast);
    // Only one check clause is allowed per column constraint definitions
    if (checkConstraintStream.count() != 1) {
      throw new CheckConstraintAlreadyDefinedSemanticException(columnName);
    }
    tableAttribute.addCheckExpression(checkConstraintStream.findFirst().orElseThrow().getCheckExpression());
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
   * @param columnAttribute - column in which we fill info
   * @param constraintList - list of constraints
   * @param referenceIndex - used when we define reference constraint in table scope
   * @throws SemanticException - if we define multiple reference clause on column
   */
  private void fillConstraintAttributes(ColumnAttribute columnAttribute, ConstraintList constraintList, int referenceIndex) throws SemanticException {
    Stream<Constraint> constraintStream = constraintList.stream();

    columnAttribute.setNotNull(constraintStream.anyMatch(PrimaryKeyConstraint.class::isInstance));
    columnAttribute.setNotNull(constraintStream.anyMatch(UniqueConstraint.class::isInstance));
    columnAttribute.setNotNull(constraintStream.anyMatch(NotNullConstraint.class::isInstance));

    // Reference constraint
    if (constraintStream.anyMatch(ReferenceConstraint.class::isInstance)) {
      Stream<ReferenceConstraint> referenceConstraintStream = constraintStream
          .filter(ReferenceConstraint.class::isInstance)
          .map(ReferenceConstraint.class::cast);
      // Can't redefine reference constraint
      if (columnAttribute.isForeign() || (referenceConstraintStream.count() != 1)) {
        throw new ReferenceConstraintAlreadyDefinedSemanticException(columnAttribute.getColumnName());
      }
      ReferenceConstraint constraint = referenceConstraintStream.findFirst().orElseThrow();
      columnAttribute.setForeign(true);
      columnAttribute.setForeignColumn(constraint.getColumnList().get(referenceIndex));
      columnAttribute.setForeignTable(constraint.getTableName());
      columnAttribute.setForeignUpdateAction(constraint.getUpdateAction());
      columnAttribute.setForeignDeleteAction(constraint.getDeleteAction());
    }
  }

  private void analyzeForeignKeys() throws SemanticException {

  }

  private void analyzeCheckClause() throws SemanticException {

  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticAttributeTable;
  }
}
