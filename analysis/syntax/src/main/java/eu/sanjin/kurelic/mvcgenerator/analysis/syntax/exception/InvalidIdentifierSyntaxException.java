package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidIdentifierSyntaxException extends SyntaxException {

  public enum ErrorType {
    RESERVED_WORD, INVALID_CHARACTER, INVALID_FIRST_CHARACTER
  }

  private static final String ERROR_STRING = "Identifier is not valid, at line %d. ";
  private static final String RESERVED_WORD_STRING = "Identifier is either reserved word or constant";
  private static final String INVALID_CHARACTER = "Identifier must contain only alphanumeric characters and underscore or space characters";
  private static final String INVALID_FIRST_CHARACTER = "Identifier must start with alphabetic character";
  private String message;

  public InvalidIdentifierSyntaxException(int lineNumber, ErrorType errorType) {
    String errorMessage = ERROR_STRING;
    switch (errorType) {
      case RESERVED_WORD:
        errorMessage += RESERVED_WORD_STRING;
        break;
      case INVALID_CHARACTER:
        errorMessage += INVALID_CHARACTER;
        break;
      case INVALID_FIRST_CHARACTER:
        errorMessage += INVALID_FIRST_CHARACTER;
        break;
    }
    message = String.format(errorMessage, lineNumber);
  }

  @Override
  public String getMessage() {
    return message;
  }

}
