package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

import java.util.Objects;

public class ExtendedIntegerColumnAttribute extends ExtendedColumnAttribute {

  private Integer max;
  private Integer min;

  public ExtendedIntegerColumnAttribute(ColumnAttribute columnAttribute) {
    super(columnAttribute);
  }

  public Integer getMax() {
    return max;
  }

  public void setMax(Integer max) {
    this.max = max;
  }

  public Integer getMin() {
    return min;
  }

  public void setMin(Integer min) {
    this.min = min;
  }

  @Override
  protected String customAttributes() {
    String minMax = "";
    if (!Objects.isNull(min)) {
      minMax += "MINIMUM IS " +  min;
    }
    if (!Objects.isNull(max)) {
      if (!minMax.isEmpty()) {
        minMax += " AND ";
      }
      minMax += "MAXIMUM IS " + max;
    }
    if (!minMax.isEmpty()) {
      return " CHECK (" +minMax + ")";
    }
    return super.customAttributes();
  }
}
