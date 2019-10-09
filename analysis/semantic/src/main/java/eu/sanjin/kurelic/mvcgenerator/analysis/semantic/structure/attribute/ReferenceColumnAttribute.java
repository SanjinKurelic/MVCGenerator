package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;

public class ReferenceColumnAttribute extends ColumnAttribute {

  private Token foreignTable;
  private Token foreignColumn;
  private ReferenceAction foreignAction;

  public Token getForeignTable() {
    return foreignTable;
  }

  public void setForeignTable(Token foreignTable) {
    this.foreignTable = foreignTable;
  }

  public Token getForeignColumn() {
    return foreignColumn;
  }

  public void setForeignColumn(Token foreignColumn) {
    this.foreignColumn = foreignColumn;
  }

  public ReferenceAction getForeignAction() {
    return foreignAction;
  }

  public void setForeignAction(ReferenceAction foreignAction) {
    this.foreignAction = foreignAction;
  }
}
