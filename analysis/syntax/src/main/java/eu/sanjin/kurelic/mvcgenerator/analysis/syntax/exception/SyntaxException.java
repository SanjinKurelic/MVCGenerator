package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class SyntaxException extends Exception {

  private static final String SYNTAX_EXCEPTION = "Syntax error => ";
  private final String message;

  public SyntaxException(String message) {
    this.message = SYNTAX_EXCEPTION + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
