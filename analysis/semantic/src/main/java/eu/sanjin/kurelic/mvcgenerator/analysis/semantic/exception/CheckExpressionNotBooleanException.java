package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

public class CheckExpressionNotBooleanException extends SemanticException {

  private static final String CHECK_NOT_BOOLEAN_ERROR = "Check clause does not return boolean value at line %d";

  public CheckExpressionNotBooleanException(int lineNumber) {
    super(String.format(CHECK_NOT_BOOLEAN_ERROR, lineNumber));
  }
}
