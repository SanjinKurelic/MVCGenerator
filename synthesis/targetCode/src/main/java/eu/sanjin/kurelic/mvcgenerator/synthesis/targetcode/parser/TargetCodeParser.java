package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.UnsupportedTargetFrameworkTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.ControllerBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.block.JavaSpringControllerBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util.CodeOutputWriterUtil;

public class TargetCodeParser {

  private final SemanticAttributeTable semanticAttributeTable;
  private TargetSettings targetSettings;
  private CodeOutputWriterUtil codeOutputWriterUtil;

  public TargetCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse(TargetSettings targetSettings) {
    this.targetSettings = targetSettings;

    // generate project structure


    for(TableAttribute tableAttribute : semanticAttributeTable.getTables().values()) {
      // generate presentation layer

      // generate controller layer

      // generate service layer

      // generate dao layer
    }
  }

  public String getStatus() {
    return "";
  }
}
