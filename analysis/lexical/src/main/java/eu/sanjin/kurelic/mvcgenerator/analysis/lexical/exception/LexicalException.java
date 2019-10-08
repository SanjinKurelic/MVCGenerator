package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class LexicalException extends Exception {

  private String message;

  LexicalException(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
