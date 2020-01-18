package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class MinMaxIntermediateCodeException extends IntermediateCodeException {

  private static final String MIN_MAX_EXCEPTION = "Minimum is greater than maximum for column %s.";
  private static final String REAL_MIN_MAX_VALUES = "Minimum is %f and maximum is %f";
  private static final String INTEGER_MIN_MAX_VALUES = "Minimum is %d and maximum is %d";

  public MinMaxIntermediateCodeException(ColumnAttribute columnAttribute, double min, double max) {
    super(String.format(MIN_MAX_EXCEPTION + REAL_MIN_MAX_VALUES, columnAttribute.getColumnName(), min, max));
  }

  public MinMaxIntermediateCodeException(ColumnAttribute columnAttribute, int min, int max) {
    super(String.format(MIN_MAX_EXCEPTION+ INTEGER_MIN_MAX_VALUES, columnAttribute.getColumnName(), min, max));
  }
}
