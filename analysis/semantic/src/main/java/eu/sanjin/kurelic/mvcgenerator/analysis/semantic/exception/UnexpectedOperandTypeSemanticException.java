package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;

public class UnexpectedOperandTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_OPERAND_TYPE = "Operand %s is not supported by semantic analysis, at line %d";

  public UnexpectedOperandTypeSemanticException(Operand operand) {
    super(String.format(UNSUPPORTED_OPERAND_TYPE, operand.getClass().getSimpleName(), operand.getOperand().getLineNumber()));
  }
}
