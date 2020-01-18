package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

import java.util.Objects;

public class ExtendedRealColumnAttribute extends ExtendedColumnAttribute {

  private Double min;
  private boolean inclusiveMin;
  private Double max;
  private boolean inclusiveMax;

  public ExtendedRealColumnAttribute(ColumnAttribute columnAttribute) {
    super(columnAttribute);
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public boolean isInclusiveMin() {
    return inclusiveMin;
  }

  public void setInclusiveMin(boolean inclusiveMin) {
    this.inclusiveMin = inclusiveMin;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }

  public boolean isInclusiveMax() {
    return inclusiveMax;
  }

  public void setInclusiveMax(boolean inclusiveMax) {
    this.inclusiveMax = inclusiveMax;
  }

  @Override
  protected String customAttributes() {
    String minMax = "";
    if (!Objects.isNull(min)) {
      minMax += "MINIMUM IS ";
      if (!Boolean.TRUE.equals(inclusiveMin)) {
        minMax += "GREATER THEN ";
      }
      minMax += min;
    }
    if (!Objects.isNull(max)) {
      if (!minMax.isEmpty()) {
        minMax += " AND ";
      }
      minMax += "MAXIMUM IS ";
      if (!Boolean.TRUE.equals(inclusiveMax)) {
        minMax += "GREATER THEN ";
      }
      minMax += max;
    }
    if (!minMax.isEmpty()) {
      return " CHECK (" + minMax + ")";
    }
    return super.customAttributes();
  }
}
