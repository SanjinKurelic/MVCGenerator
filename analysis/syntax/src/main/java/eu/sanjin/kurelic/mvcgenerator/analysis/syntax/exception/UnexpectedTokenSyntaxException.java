package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class UnexpectedTokenSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Unexpected token '%s', at line %d";
  private static final String EXPECTING_TEXT = ". Expecting token '%s'";

  public UnexpectedTokenSyntaxException(Token token) {
    super(String.format(ERROR_STRING, token.getValue(), token.getLineNumber()));
  }

  public UnexpectedTokenSyntaxException(Token token, String expected) {
    super(String.format(ERROR_STRING + EXPECTING_TEXT, token.getValue(), token.getLineNumber(), expected));
  }
}
