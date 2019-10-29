package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidIdentifierSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Identifier is not valid, at line %d. ";

  public enum ErrorType {
    RESERVED_WORD("Identifier is either reserved word or constant"),
    INVALID_CHARACTER("Identifier must contain only alphanumeric characters and underscore or space characters"),
    INVALID_FIRST_CHARACTER("Identifier must start with alphabetic character");

    private String message;

    ErrorType(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  public InvalidIdentifierSyntaxException(int lineNumber, ErrorType errorType) {
    super(String.format(ERROR_STRING + errorType.getMessage(), lineNumber));
  }
}
