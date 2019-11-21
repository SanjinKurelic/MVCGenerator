package eu.sanjin.kurelic.mvcgenerator.view.model;

public class Settings {

  private String fileContent;
  private String rootNamespace;
  private String outputPath;

  public String getFileContent() {
    return fileContent;
  }

  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  public String getRootNamespace() {
    return rootNamespace;
  }

  public void setRootNamespace(String rootNamespace) {
    this.rootNamespace = rootNamespace;
  }

  public String getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
}
