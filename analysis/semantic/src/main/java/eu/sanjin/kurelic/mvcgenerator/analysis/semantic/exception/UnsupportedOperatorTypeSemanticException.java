package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;

public class UnsupportedOperatorTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_OPERATOR_TYPE = "Operator %s is not supported by semantic analysis, at line %d";

  public UnsupportedOperatorTypeSemanticException(Operator operator) {
    super(String.format(UNSUPPORTED_OPERATOR_TYPE, operator.getOperator(), operator.getLineNumber()));
  }
}
