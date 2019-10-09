package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class TableAlreadyDefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Table with name '%s' is already defined, cannot redefine it at line %d";
  private String message;

  public TableAlreadyDefinedSemanticException(Token tableName) {
    message = String.format(ERROR_STRING, tableName.getValue(), tableName.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }

}
