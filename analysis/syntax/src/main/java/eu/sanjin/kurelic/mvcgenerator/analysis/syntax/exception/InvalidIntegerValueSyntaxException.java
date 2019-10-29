package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class InvalidIntegerValueSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Expecting integer value, got %s, at line %d";

  public InvalidIntegerValueSyntaxException(Token token) {
    super(String.format(ERROR_STRING, token.getValue(), token.getLineNumber()));
  }
}
