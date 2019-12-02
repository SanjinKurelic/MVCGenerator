package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

public class UnsupportedPredicateTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_PREDICATE_TYPE = "Predicate %s is not supported by semantic analysis";

  public UnsupportedPredicateTypeSemanticException(Class<?> predicateClass) {
    super(String.format(UNSUPPORTED_PREDICATE_TYPE, predicateClass.getSimpleName()));
  }
}
