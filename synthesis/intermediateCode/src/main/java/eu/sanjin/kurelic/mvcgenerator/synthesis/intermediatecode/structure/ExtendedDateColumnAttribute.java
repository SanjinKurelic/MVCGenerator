package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

public class ExtendedDateColumnAttribute extends ExtendedColumnAttribute {

  private Boolean isFuture;
  private Boolean isFutureOrPresent;
  private Boolean isPast;
  private Boolean isPastOrPresent;

  public Boolean getFuture() {
    return isFuture;
  }

  public void setFuture(Boolean future) {
    isFuture = future;
  }

  public Boolean getFutureOrPresent() {
    return isFutureOrPresent;
  }

  public void setFutureOrPresent(Boolean futureOrPresent) {
    isFutureOrPresent = futureOrPresent;
  }

  public Boolean getPast() {
    return isPast;
  }

  public void setPast(Boolean past) {
    isPast = past;
  }

  public Boolean getPastOrPresent() {
    return isPastOrPresent;
  }

  public void setPastOrPresent(Boolean pastOrPresent) {
    isPastOrPresent = pastOrPresent;
  }
}
