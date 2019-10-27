package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;

import java.util.Objects;

public class ColumnAttribute {

  private Token columnName;
  private DataType dataType;
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
}
