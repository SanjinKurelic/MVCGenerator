package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

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
}
