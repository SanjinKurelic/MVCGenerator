package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

public class SemanticException extends Exception {

  private static final String SEMANTIC_EXCEPTION = "Semantic error => ";
  private String message;

  public SemanticException(String message) {
    this.message = SEMANTIC_EXCEPTION + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
