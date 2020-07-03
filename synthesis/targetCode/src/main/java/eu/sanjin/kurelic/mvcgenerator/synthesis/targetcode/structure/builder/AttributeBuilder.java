package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.builder;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;

public abstract class AttributeBuilder {

  protected abstract String attributeKeyword();
  protected abstract String attributeStart();
  protected abstract String attributeEnd();
  protected abstract String attributeType(DataType dataType);

  public String generateCode(String attributeName, DataType dataType) {
    return String.format("%s %s %s%s%s\n",
      attributeKeyword(),
      attributeType(dataType),
      attributeStart(),
      attributeName,
      attributeEnd()
    );
  }

}
