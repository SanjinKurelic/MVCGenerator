package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class ColumnAlreadyDefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Column with name '%s' is already defined, cannot redefine it at line %d";
  private String message;

  public ColumnAlreadyDefinedSemanticException(Token columnName) {
    message = String.format(ERROR_STRING, columnName.getValue(), columnName.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }
}
