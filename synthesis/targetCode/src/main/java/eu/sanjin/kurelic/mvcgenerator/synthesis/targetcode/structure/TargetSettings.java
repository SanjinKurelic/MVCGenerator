package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure;

public class TargetSettings {

  private String projectName;
  private String rootNamespace;
  private TargetFramework targetFramework;
  private String outputPath;

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getRootNamespace() {
    return rootNamespace;
  }

  public void setRootNamespace(String rootNamespace) {
    this.rootNamespace = rootNamespace;
  }

  public TargetFramework getTargetFramework() {
    return targetFramework;
  }

  public void setTargetFramework(TargetFramework targetFramework) {
    this.targetFramework = targetFramework;
  }

  public String getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
}
