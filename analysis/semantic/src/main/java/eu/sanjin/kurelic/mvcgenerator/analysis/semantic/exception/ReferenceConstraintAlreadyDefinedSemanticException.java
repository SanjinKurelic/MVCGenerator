package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class ReferenceConstraintAlreadyDefinedSemanticException extends SemanticException {

  private static final String ERROR_STRING = "Reference constraint on column '%s' is already defined, cannot redefine it at line %d";

  public ReferenceConstraintAlreadyDefinedSemanticException(Token referenceConstraintName) {
    super(String.format(ERROR_STRING, referenceConstraintName.getValue(), referenceConstraintName.getLineNumber()));
  }
}
