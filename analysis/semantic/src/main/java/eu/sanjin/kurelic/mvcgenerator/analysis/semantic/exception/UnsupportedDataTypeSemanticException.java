package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

public class UnsupportedDataTypeSemanticException extends SemanticException {

  private static final String UNSUPPORTED_DATA_TYPE = "Data type %s is not supported by semantic analysis";

  public UnsupportedDataTypeSemanticException(String dataType) {
    super(String.format(UNSUPPORTED_DATA_TYPE, dataType));
  }
}
