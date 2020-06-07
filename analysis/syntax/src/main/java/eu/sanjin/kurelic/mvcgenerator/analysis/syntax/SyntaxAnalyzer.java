package eu.sanjin.kurelic.mvcgenerator.analysis.syntax;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.LexicalAnalyzer;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.SyntaxParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.TokenSupplier;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;

public class SyntaxAnalyzer {

  private SyntaxParser syntaxParser;
  private final LexicalAnalyzer lexicalAnalyzer;

  public SyntaxAnalyzer() {
    lexicalAnalyzer = new LexicalAnalyzer();
  }

  public void parse(String code) throws Exception {
    lexicalAnalyzer.parse(code);
    syntaxParser = new SyntaxParser(new TokenSupplier(lexicalAnalyzer.getTokens()));
    syntaxParser.parse();
  }

  public SyntaxTree getSyntaxTree() {
    return syntaxParser.getSyntaxTree();
  }
}
