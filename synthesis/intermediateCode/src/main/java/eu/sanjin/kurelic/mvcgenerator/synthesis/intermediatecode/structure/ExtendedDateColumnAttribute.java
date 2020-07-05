package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;

public class ExtendedDateColumnAttribute extends ExtendedColumnAttribute {

  private Boolean future;
  private Boolean futureOrPresent;
  private Boolean past;
  private Boolean pastOrPresent;

  public ExtendedDateColumnAttribute(ColumnAttribute columnAttribute) {
    super(columnAttribute);
  }

  public Boolean getFuture() {
    return future;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isFuture() {
    return Boolean.TRUE.equals(future);
  }

  public void setFuture(Boolean future) {
    this.future = future;
  }

  public Boolean getFutureOrPresent() {
    return futureOrPresent;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isFutureOrPresent() {
    return Boolean.TRUE.equals(futureOrPresent);
  }

  public void setFutureOrPresent(Boolean futureOrPresent) {
    this.futureOrPresent = futureOrPresent;
  }

  public Boolean getPast() {
    return past;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isPast() {
    return Boolean.TRUE.equals(past);
  }

  public void setPast(Boolean past) {
    this.past = past;
  }

  public Boolean getPastOrPresent() {
    return pastOrPresent;
  }

  // Used in template engine
  @SuppressWarnings("unused")
  public boolean isPastOrPresent() {
    return Boolean.TRUE.equals(pastOrPresent);
  }

  public void setPastOrPresent(Boolean pastOrPresent) {
    this.pastOrPresent = pastOrPresent;
  }

  @Override
  protected String customAttributes() {
    if (Boolean.TRUE.equals(past)) {
      return " CHECK (IS PAST)";
    }
    if (Boolean.TRUE.equals(pastOrPresent)) {
      return " CHECK (IS PAST OR PRESENT)";
    }
    if (Boolean.TRUE.equals(future)) {
      return " CHECK (IS FUTURE)";
    }
    if (Boolean.TRUE.equals(futureOrPresent)) {
      return " CHECK (IS FUTURE OR PRESENT)";
    }
    return super.customAttributes();
  }
}
