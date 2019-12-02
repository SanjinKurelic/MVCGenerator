package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

public class UnsupportedOperandTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_OPERAND_TYPE = "Operand %s is not supported by semantic analysis";

  public UnsupportedOperandTypeSemanticException(Class<?> operandClass) {
    super(String.format(UNSUPPORTED_OPERAND_TYPE, operandClass.getSimpleName()));
  }
}
