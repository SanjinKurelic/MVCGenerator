package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.ConstraintList;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class ColumnDefinition implements TableElement {

  private static final String COLUMN_NAME_TAG = "ColumnName";
  private static final String DEFAULT_VALUE_TAG = "DefaultValue";

  private Token columnName;
  private DataType dataType;
  private Token defaultValue;
  private ConstraintList constraintList;

  public ColumnDefinition() {
    constraintList = new ConstraintList();
  }

  public Token getColumnName() {
    return columnName;
  }

  public void setColumnName(Token columnName) {
    this.columnName = columnName;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public Token getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Token defaultValue) {
    this.defaultValue = defaultValue;
  }

  public ConstraintList getConstraintList() {
    return constraintList;
  }

  public void setConstraintList(ConstraintList constraintList) {
    this.constraintList = constraintList;
  }

  @Override
  public String toString() {
    String xml = XmlTagBuilder.getStartTag(this);
    xml += XmlTagBuilder.surroundToken(columnName, COLUMN_NAME_TAG);
    xml += dataType;
    xml += XmlTagBuilder.surroundToken(defaultValue, DEFAULT_VALUE_TAG);
    xml += constraintList.toString(columnName);
    xml += XmlTagBuilder.getEndTag(this);
    return xml;
  }

}
