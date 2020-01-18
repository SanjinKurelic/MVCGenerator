package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class ValidationMismatchIntermediateCodeException extends IntermediateCodeException {

  private static final String VALIDATION_MISMATCH = "Column %s can not be %s and %s in same time.";

  public ValidationMismatchIntermediateCodeException(ColumnAttribute columnAttribute, String firstType, String secondType) {
    super(String.format(VALIDATION_MISMATCH, columnAttribute.getColumnName(), firstType, secondType));
  }
}
