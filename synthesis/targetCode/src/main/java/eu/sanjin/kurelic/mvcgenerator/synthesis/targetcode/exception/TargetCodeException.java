package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception;

public class TargetCodeException extends Exception {

  private static final String TARGET_CODE_EXCEPTION = "Target code error => ";
  private final String message;

  public TargetCodeException(String message) {
    this.message = TARGET_CODE_EXCEPTION + message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
