package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;

public class IntermediateCodeParser {

  private SemanticAttributeTable semanticAttributeTable;

  public IntermediateCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse() {
    for(TableAttribute table : semanticAttributeTable.getTables().values()) {

    }
  }
}
