package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class TableUndefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Table with name '%s' is undefined, at line %d";
  private String message;

  public TableUndefinedSemanticException(Token tableName) {
    message = String.format(ERROR_STRING, tableName.getValue(), tableName.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }
}
