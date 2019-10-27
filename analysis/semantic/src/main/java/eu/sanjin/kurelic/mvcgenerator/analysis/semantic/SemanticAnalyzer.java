package eu.sanjin.kurelic.mvcgenerator.analysis.semantic;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser.SemanticParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;

public class SemanticAnalyzer {

  private SemanticParser semanticParser;

  public SemanticAnalyzer(SyntaxTree syntaxTree) {
    semanticParser = new SemanticParser(syntaxTree);
  }

  public void parse() throws SemanticException {
      semanticParser.parse();
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
      return semanticParser.getSemanticAttributeTable();
  }

}
