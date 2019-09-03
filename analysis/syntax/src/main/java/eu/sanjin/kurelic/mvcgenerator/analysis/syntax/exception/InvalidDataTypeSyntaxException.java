package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidDataTypeSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Data type is not valid, at line %d";
  private String message;

  public InvalidDataTypeSyntaxException(int lineNumber) {
    message = String.format(ERROR_STRING, lineNumber);
  }

  @Override
  public String getMessage() {
    return message;
  }
}
