package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;

import java.util.Objects;

public class ColumnAttribute {

  private Token columnName;
  private DataTypeAttribute dataType;
  private Integer length;
  private Integer scale;
  private Token defaultValue;
  // Constraints
  private boolean primary = false;
  private boolean unique = false;
  private Boolean notNull; // Unknown by default
  private boolean foreign = false;
  // Reference constraints
  private Token foreignTable;
  private Token foreignColumn;
  private ReferenceAction foreignDeleteAction;
  private ReferenceAction foreignUpdateAction;

  public Token getColumnName() {
    return columnName;
  }

  public void setColumnName(Token columnName) {
    this.columnName = columnName;
  }

  public DataTypeAttribute getDataType() {
    return dataType;
  }

  public void setDataType(DataTypeAttribute dataType) {
    this.dataType = dataType;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  public Token getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Token defaultValue) {
    this.defaultValue = defaultValue;
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  // Used in template engine
  public boolean isNotNull() {
    return Boolean.TRUE.equals(notNull);
  }

  public Boolean getNotNull() {
    return notNull;
  }

  public void setNotNull(Boolean notNull) {
    this.notNull = notNull;
  }

  public boolean isForeign() {
    return foreign;
  }

  public void setForeign(boolean foreign) {
    this.foreign = foreign;
  }

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

  public ReferenceAction getForeignDeleteAction() {
    return foreignDeleteAction;
  }

  public void setForeignDeleteAction(ReferenceAction foreignDeleteAction) {
    this.foreignDeleteAction = foreignDeleteAction;
  }

  public ReferenceAction getForeignUpdateAction() {
    return foreignUpdateAction;
  }

  public void setForeignUpdateAction(ReferenceAction foreignUpdateAction) {
    this.foreignUpdateAction = foreignUpdateAction;
  }

  protected String customAttributes() {
    return "";
  }

  @Override
  public String toString() {
    String columnDefinition = columnName.getValue() + " " + dataType.name();

    if (!Objects.isNull(length)) {
      columnDefinition += " (" + length + ")";
    }
    if (!Objects.isNull(defaultValue)) {
      columnDefinition += " DEFAULT " + defaultValue.getValue();
    }
    columnDefinition += (primary ? " PRIMARY KEY" : "");
    columnDefinition += (unique ? " UNIQUE" : "");
    columnDefinition += (Boolean.TRUE.equals(notNull) ? " NOT NULL" : "");
    columnDefinition += customAttributes();

    if (foreign) {
      columnDefinition += " REFERENCES " + foreignTable.getValue() + " (" + foreignColumn.getValue() + ")";
      if (!Objects.isNull(foreignUpdateAction)) {
        columnDefinition += " ON UPDATE " + foreignUpdateAction.name().replace('_', ' ');
      }
      if (!Objects.isNull(foreignDeleteAction)) {
        columnDefinition += " ON DELETE " + foreignDeleteAction.name().replace('_', ' ');
      }
    }

    return columnDefinition;
  }
}
