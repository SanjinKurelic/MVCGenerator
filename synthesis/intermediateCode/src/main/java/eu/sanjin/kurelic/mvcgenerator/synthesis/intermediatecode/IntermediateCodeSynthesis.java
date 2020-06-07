package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.SemanticAnalyzer;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.parser.IntermediateCodeParser;

public class IntermediateCodeSynthesis {

  private IntermediateCodeParser intermediateCodeParser;
  private final SemanticAnalyzer semanticAnalyzer;

  public IntermediateCodeSynthesis() {
    semanticAnalyzer = new SemanticAnalyzer();
  }

  public void parse(String code) throws Exception {
    semanticAnalyzer.parse(code);
    intermediateCodeParser = new IntermediateCodeParser(semanticAnalyzer.getSemanticAttributeTable());
    intermediateCodeParser.parse();
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return intermediateCodeParser.getSemanticAttributeTable();
  }
}
