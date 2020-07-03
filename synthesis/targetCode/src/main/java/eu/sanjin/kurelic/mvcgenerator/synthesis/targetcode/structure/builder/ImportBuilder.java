package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.builder;

public abstract class ImportBuilder {

  public abstract String importKeyword();
  public abstract String startEscapeCharacter();
  public abstract String endEscapeCharacter();

  public String generateImport(String packageName) {
    return String.format("%s %s%s%s\n", importKeyword(), startEscapeCharacter(), packageName, endEscapeCharacter());
  }

}
