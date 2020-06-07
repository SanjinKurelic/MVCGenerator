package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class LexicalException extends Exception {

  private static final String LEXICAL_EXCEPTION = "Lexical error => ";
  private final String message;

  LexicalException(String message) {
    this.message = LEXICAL_EXCEPTION + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
