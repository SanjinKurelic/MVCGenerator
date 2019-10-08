package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidIntervalTypeSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Interval definition is not valid, at line %d";
  private String message;

  public InvalidIntervalTypeSyntaxException(int lineNumber) {
    message = String.format(ERROR_STRING, lineNumber);
  }

  @Override
  public String getMessage() {
    return message;
  }

}
