package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;

import java.util.HashMap;

public class SemanticAttributeTable {

  private HashMap<String, TableAttribute> tableAttributes;

  public SemanticAttributeTable() {
    tableAttributes = new HashMap<>();
  }

  public boolean hasTableAttribute(TableAttribute tableAttribute) {
    return tableAttributes.containsKey(tableAttribute.getTableName().getValue());
  }

  public void addTableAttribute(TableAttribute tableAttribute) {
    tableAttributes.put(tableAttribute.getTableName().getValue(), tableAttribute);
  }

  public HashMap<String, TableAttribute> getTableAttributes() {
    return tableAttributes;
  }
}
