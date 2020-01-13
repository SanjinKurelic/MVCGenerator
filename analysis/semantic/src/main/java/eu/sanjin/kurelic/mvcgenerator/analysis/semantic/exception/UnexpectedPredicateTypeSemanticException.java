package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;

public class UnexpectedPredicateTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_PREDICATE_TYPE = "Predicate %s is not supported by semantic analysis, at line %d";

  public UnexpectedPredicateTypeSemanticException(Predicate predicate) {
    super(String.format(UNSUPPORTED_PREDICATE_TYPE, predicate.getClass().getSimpleName(), predicate.getOperator().getLineNumber()));
  }
}
