package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class ReferenceConstraintAlreadyDefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Reference constraint on column '%s' is already defined, cannot redefine it at line %d";
  private String message;

  public ReferenceConstraintAlreadyDefinedSemanticException(Token referenceConstraintName) {
    message = String.format(ERROR_STRING, referenceConstraintName.getValue(), referenceConstraintName.getLineNumber());
  }

  @Override
  public String getMessage() {
    return message;
  }
}
