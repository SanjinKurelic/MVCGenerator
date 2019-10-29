package eu.sanjin.kurelic.mvcgenerator.analysis.semantic;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception.SemanticException;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.parser.SemanticParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;

public class SemanticAnalyzer {

  private SemanticParser semanticParser;

  public void parse(SyntaxTree syntaxTree) throws SemanticException {
    semanticParser = new SemanticParser(syntaxTree);
    semanticParser.parse();
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticParser.getSemanticAttributeTable();
  }
}
