package eu.sanjin.kurelic.mvcgenerator.analysis.syntax;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.SyntaxException;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.SyntaxParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.TokenSupplier;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;

public class SyntaxAnalyzer {

  private SyntaxParser syntaxParser;

  public void parse(Tokens tokens) throws SyntaxException {
    syntaxParser = new SyntaxParser(new TokenSupplier(tokens));
    syntaxParser.parse();
  }

  public SyntaxTree getSyntaxTree() {
    return syntaxParser.getSyntaxTree();
  }
}
