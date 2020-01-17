package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

public class ExtendedBooleanColumnAttribute extends ExtendedColumnAttribute{

  private Boolean assertTrue;
  private Boolean assertFalse;
  private Boolean assertUnknown;

  public Boolean getAssertTrue() {
    return assertTrue;
  }

  public void setAssertTrue(Boolean assertTrue) {
    this.assertTrue = assertTrue;
  }

  public Boolean getAssertFalse() {
    return assertFalse;
  }

  public void setAssertFalse(Boolean assertFalse) {
    this.assertFalse = assertFalse;
  }

  public Boolean getAssertUnknown() {
    return assertUnknown;
  }

  public void setAssertUnknown(Boolean assertUnknown) {
    this.assertUnknown = assertUnknown;
  }
}
