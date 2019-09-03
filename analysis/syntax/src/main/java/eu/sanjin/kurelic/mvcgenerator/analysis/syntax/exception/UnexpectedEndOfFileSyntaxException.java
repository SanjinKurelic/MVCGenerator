package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class UnexpectedEndOfFileSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Unexpected end of file";

  @Override
  public String getMessage() {
    return ERROR_STRING;
  }
}
