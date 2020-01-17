package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class ValidationMismatchIntermediateCodeException extends IntermediateCodeException {

  private static final String VALIDATION_MISMATCH = "Column %s can not be $s to both ";

  public ValidationMismatchIntermediateCodeException(ColumnAttribute columnAttribute) {
  }
}
