package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.Constraint;

public class TableConstraintDefinition implements TableElement {

  private Constraint constraintType;
  private Tokens columnList;

  public TableConstraintDefinition() {
    columnList = new Tokens();
  }

  public Constraint getConstraintType() {
    return constraintType;
  }

  public void setConstraintType(Constraint constraintType) {
    this.constraintType = constraintType;
  }

  public Tokens getColumnList() {
    return columnList;
  }

  public void setColumnList(Tokens columnList) {
    this.columnList = columnList;
  }

  @Override
  public String toString() {
    return constraintType.toString(columnList);
  }
}
