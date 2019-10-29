package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidDataTypeSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Data type is not valid, at line %d";

  public InvalidDataTypeSyntaxException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
