package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;

import java.util.HashMap;

public class SemanticAttributeTable {

  private final HashMap<String, TableAttribute> tableAttributes;

  public SemanticAttributeTable() {
    tableAttributes = new HashMap<>();
  }

  public boolean hasTable(TableAttribute tableAttribute) {
    return hasTable(tableAttribute.getTableName().getValue());
  }

  public boolean hasTable(String tableName) {
    return tableAttributes.containsKey(tableName);
  }

  public void addTable(TableAttribute tableAttribute) {
    tableAttributes.put(tableAttribute.getTableName().getValue(), tableAttribute);
  }

  public TableAttribute getTable(String tableName) {
    return tableAttributes.get(tableName);
  }

  public HashMap<String, TableAttribute> getTables() {
    return tableAttributes;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    tableAttributes.forEach((tableName, tableAttribute) -> {
      stringBuilder.append(tableAttribute.toString());
      stringBuilder.append("\n\n");
    });
    return stringBuilder.toString();
  }
}
