package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import java.util.Objects;

public class ExtendedNumberColumnAttribute extends ExtendedColumnAttribute {

  private Double numericMax;
  private Double numericMin;

  public Double getNumericMax() {
    return numericMax;
  }

  public Integer getNumericIntegerMax() {
    return Objects.isNull(numericMax) ? null : numericMax.intValue();
  }

  public void setNumericMax(Double numericMax) {
    this.numericMax = numericMax;
  }

  public Double getNumericMin() {
    return numericMin;
  }

  public Integer getNumericIntegerMin() {
    return Objects.isNull(numericMin) ? null : numericMin.intValue();
  }

  public void setNumericMin(Double numericMin) {
    this.numericMin = numericMin;
  }
}
