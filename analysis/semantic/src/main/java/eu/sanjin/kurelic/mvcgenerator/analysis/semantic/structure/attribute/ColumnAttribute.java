package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;

import java.util.Objects;

public class ColumnAttribute {

  private Token columnName;
  private DataTypeAttribute dataType;
  private Integer length;
  private Token defaultValue;
  // Constraints
  private boolean primary = false;
  private boolean unique = false;
  private boolean notNull = false;
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

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
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

  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

  public boolean isNotNull() {
    return notNull;
  }

  public void setNotNull(boolean notNull) {
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
    columnDefinition += (notNull ? " NOT NULL" : "");

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
