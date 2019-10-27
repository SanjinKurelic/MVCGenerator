package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class CheckConstraintAlreadyDefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Check constraint on column '%s' is already defined, cannot redefine it at line %d";
  private String message;

  public CheckConstraintAlreadyDefinedSemanticException(Token checkConstraintName) {
    message = String.format(ERROR_STRING, checkConstraintName.getValue(), checkConstraintName.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }
}
