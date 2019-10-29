package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidReferenceColumnCountSyntaxException extends SyntaxException {

  private static final String ERROR_STRING = "Reference clause has to many or to few columns at line %d";

  public InvalidReferenceColumnCountSyntaxException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
