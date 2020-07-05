package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models;

public class TypeDefinition {

  private String name;
  private String namespace;
  private String variableName;

  public TypeDefinition() {
  }

  public TypeDefinition(String name) {
    this(name, null);
  }

  public TypeDefinition(String name, String namespace) {
    this.name = name;
    this.namespace = namespace;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }
}
