package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

@SuppressWarnings("unused")
public class ColumnAttributeViewModel {

  private String name;
  private String nameLowercaseFirst;
  private String type;
  private String value;
  private String foreignTable;
  private String foreignColumn;
  private Boolean cascadeDelete;
  private Boolean cascadeUpdate;
  private ColumnAttribute model;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameLowercaseFirst() {
    return nameLowercaseFirst;
  }

  public void setNameLowercaseFirst(String nameLowercaseFirst) {
    this.nameLowercaseFirst = nameLowercaseFirst;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getForeignTable() {
    return foreignTable;
  }

  public void setForeignTable(String foreignTable) {
    this.foreignTable = foreignTable;
  }

  public String getForeignColumn() {
    return foreignColumn;
  }

  public void setForeignColumn(String foreignColumn) {
    this.foreignColumn = foreignColumn;
  }

  public Boolean getCascadeDelete() {
    return cascadeDelete;
  }

  public void setCascadeDelete(Boolean cascadeDelete) {
    this.cascadeDelete = cascadeDelete;
  }

  public Boolean getCascadeUpdate() {
    return cascadeUpdate;
  }

  public void setCascadeUpdate(Boolean cascadeUpdate) {
    this.cascadeUpdate = cascadeUpdate;
  }

  public ColumnAttribute getModel() {
    return model;
  }

  public void setModel(ColumnAttribute model) {
    this.model = model;
  }
}
