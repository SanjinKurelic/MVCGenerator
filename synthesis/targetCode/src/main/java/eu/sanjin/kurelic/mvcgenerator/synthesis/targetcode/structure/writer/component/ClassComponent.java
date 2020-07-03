package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component;

public class ClassComponent implements Component {

  enum ClassType {
    CONTROLLER, SERVICE, MODEL, REPOSITORY, VIEW
  }

  private String packageName;
  private String className;
  private ClassType classType;

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setClassType(ClassType classType) {
    this.classType = classType;
  }

  public ClassType getClassType() {
    return classType;
  }

  @Override
  public String getComponentName() {
    return className;
  }

  @Override
  public String getComponentPackage() {
    return packageName;
  }
}
