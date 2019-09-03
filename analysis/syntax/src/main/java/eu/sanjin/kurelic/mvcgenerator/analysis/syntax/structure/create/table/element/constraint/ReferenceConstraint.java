package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.util.Objects;

public class ReferenceConstraint extends Constraint {

  private Token tableName;
  private Tokens columnList;
  private ReferenceAction updateAction;
  private ReferenceAction deleteAction;

  private static final String REFERENCED_TABLE_TAG = "ReferencedTable";
  private static final String REFERENCED_COLUMN_LIST = "ReferencedColumnList";
  private static final String REFERENCING_COLUMN_LIST = "ReferencingColumnList";
  private static final String UPDATE_ACTION_TAG = "UpdateAction";
  private static final String DELETE_ACTION_TAG = "DeleteAction";

  public Token getTableName() {
    return tableName;
  }

  public void setTableName(Token tableName) {
    this.tableName = tableName;
  }

  public Tokens getColumnList() {
    return columnList;
  }

  public void setColumnList(Tokens columnList) {
    this.columnList = columnList;
  }

  public ReferenceAction getUpdateAction() {
    return updateAction;
  }

  public void setUpdateAction(ReferenceAction updateAction) {
    this.updateAction = updateAction;
  }

  public ReferenceAction getDeleteAction() {
    return deleteAction;
  }

  public void setDeleteAction(ReferenceAction deleteAction) {
    this.deleteAction = deleteAction;
  }

  private String getColumnList(Tokens columnList, String surroundingTag) {
    StringBuilder xml = new StringBuilder();
    xml.append(XmlTagBuilder.getStartTag(surroundingTag));
    for (Token column : columnList) {
      xml.append(XmlTagBuilder.surroundToken(column, COLUMN_TAG));
    }
    xml.append(XmlTagBuilder.getEndTag(surroundingTag));
    return xml.toString();
  }

  @Override
  public String toString(Tokens columnList) {
    String xml = XmlTagBuilder.getStartTag(this);
    // Referenced
    xml += XmlTagBuilder.surroundToken(tableName, REFERENCED_TABLE_TAG);
    xml += getColumnList(this.columnList, REFERENCED_COLUMN_LIST);
    // Referencing
    xml += getColumnList(columnList, REFERENCING_COLUMN_LIST);
    // Actions
    if (!Objects.isNull(updateAction)) {
      xml += XmlTagBuilder.surroundToken(updateAction.name(), UPDATE_ACTION_TAG);
    }
    if (!Objects.isNull(deleteAction)) {
      xml += XmlTagBuilder.surroundToken(deleteAction.name(), DELETE_ACTION_TAG);
    }

    return xml + XmlTagBuilder.getEndTag(this);
  }

  @Override
  public String toString() {
    throw new UnsupportedOperationException();
  }
}
