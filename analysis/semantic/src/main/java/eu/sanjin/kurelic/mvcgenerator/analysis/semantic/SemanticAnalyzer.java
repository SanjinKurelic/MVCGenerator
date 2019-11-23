package eu.sanjin.kurelic.mvcgenerator.analysis.semantic;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser.SemanticParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.SyntaxAnalyzer;

public class SemanticAnalyzer {

  private SemanticParser semanticParser;
  private SyntaxAnalyzer syntaxAnalyzer;

  public SemanticAnalyzer() {
    syntaxAnalyzer = new SyntaxAnalyzer();
  }

  public void parse(String code) throws Exception {
    syntaxAnalyzer.parse(code);
    semanticParser = new SemanticParser(syntaxAnalyzer.getSyntaxTree());
    semanticParser.parse();
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticParser.getSemanticAttributeTable();
  }
}
