package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;

public class UnexpectedDataTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_DATA_TYPE = "Data type %s is not supported by semantic analysis";
  private static final String UNEXPECTED_DATA_TYPE = "Data type %s is not expected in current position, at line %d";

  public UnexpectedDataTypeSemanticException(String dataType) {
    super(String.format(UNSUPPORTED_DATA_TYPE, dataType));
  }

  public UnexpectedDataTypeSemanticException(DataTypeAttribute dataTypeAttribute, int lineNumber) {
    super(String.format(UNEXPECTED_DATA_TYPE, dataTypeAttribute.name(), lineNumber));
  }
}
