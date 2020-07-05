package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class ExtendedBooleanColumnAttribute extends ExtendedColumnAttribute {

  private Boolean assertTrue;
  private Boolean assertFalse;

  public ExtendedBooleanColumnAttribute(ColumnAttribute columnAttribute) {
    super(columnAttribute);
  }

  public Boolean getAssertTrue() {
    return assertTrue;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isAssertTrue() {
    return Boolean.TRUE.equals(assertTrue);
  }

  public void setAssertTrue(Boolean assertTrue) {
    this.assertTrue = assertTrue;
  }

  public Boolean getAssertFalse() {
    return assertFalse;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isAssertFalse() {
    return Boolean.TRUE.equals(assertFalse);
  }

  public void setAssertFalse(Boolean assertFalse) {
    this.assertFalse = assertFalse;
  }

  @Override
  protected String customAttributes() {
    if (Boolean.TRUE.equals(assertTrue)) {
      return " CHECK (IS TRUE)";
    }
    if (Boolean.TRUE.equals(assertFalse)) {
      return " CHECK (IS FALSE)";
    }
    return super.customAttributes();
  }
}
