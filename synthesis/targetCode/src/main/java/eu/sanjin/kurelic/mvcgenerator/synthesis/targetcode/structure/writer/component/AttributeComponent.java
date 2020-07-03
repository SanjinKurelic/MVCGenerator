package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component;

public class AttributeComponent implements Component {

  private String attributeName;
  private String attributePackage;

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public void setAttributePackage(String attributePackage) {
    this.attributePackage = attributePackage;
  }

  @Override
  public String getComponentName() {
    return attributeName;
  }

  @Override
  public String getComponentPackage() {
    return attributePackage;
  }
}
