package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;

public class CanNotCastTypeSemanticException extends SemanticException {

  private static final String TYPE_CAN_NOT_CAST = "Type %s can not be casted to %s, at line %d";

  public CanNotCastTypeSemanticException(DataTypeAttribute dataTypeAttribute1, DataTypeAttribute dataTypeAttribute2, int lineNumber) {
    super(String.format(TYPE_CAN_NOT_CAST, dataTypeAttribute1.name(), dataTypeAttribute2.name(), lineNumber));
  }
}
