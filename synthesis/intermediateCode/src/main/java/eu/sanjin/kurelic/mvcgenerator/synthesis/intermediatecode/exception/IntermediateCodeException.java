package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception;

public class IntermediateCodeException extends Exception {

  private static final String INTERMEDIATE_CODE_EXCEPTION = "Validation error => ";
  private String message;

  public IntermediateCodeException(String message) {
    this.message = INTERMEDIATE_CODE_EXCEPTION + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
