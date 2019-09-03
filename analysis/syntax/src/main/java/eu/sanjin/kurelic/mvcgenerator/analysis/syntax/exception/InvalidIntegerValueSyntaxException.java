package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class InvalidIntegerValueSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Expecting integer value, got %s, at line %d";
  private String message;

  public InvalidIntegerValueSyntaxException(Token token) {
    message = String.format(ERROR_STRING, token.getValue(), token.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }

}
