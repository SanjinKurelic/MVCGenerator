package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;

public class UnexpectedOperatorTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_OPERATOR_TYPE = "Operator %s is not supported by semantic analysis, at line %d";
  private static final String UNSUPPORTED_OPERATOR_FOR_DATA_TYPE = "Operator %s is not supported for data type %s, at line %d";

  public UnexpectedOperatorTypeSemanticException(Operator operator) {
    super(String.format(UNSUPPORTED_OPERATOR_TYPE, operator.getOperator(), operator.getLineNumber()));
  }

  public UnexpectedOperatorTypeSemanticException(Operator operator, DataTypeAttribute dataTypeAttribute) {
    super(String.format(
        UNSUPPORTED_OPERATOR_FOR_DATA_TYPE,
        operator.getOperator(),
        dataTypeAttribute.name(),
        operator.getLineNumber()
    ));
  }
}
