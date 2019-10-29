package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class ColumnUndefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Column with name '%s' is undefined, at line %d";

  public ColumnUndefinedSemanticException(Token columnName) {
    super(String.format(ERROR_STRING, columnName.getValue(), columnName.getLineNumber()));
  }
}
