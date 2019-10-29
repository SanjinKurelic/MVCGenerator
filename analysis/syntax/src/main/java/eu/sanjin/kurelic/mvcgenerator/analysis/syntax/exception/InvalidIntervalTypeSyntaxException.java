package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidIntervalTypeSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Interval definition is not valid, at line %d";

  public InvalidIntervalTypeSyntaxException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
