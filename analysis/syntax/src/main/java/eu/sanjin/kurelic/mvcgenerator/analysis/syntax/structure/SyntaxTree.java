package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SyntaxTree implements Serializable {

  private List<CreateDefinition> createDefinitions;

  public void setCreateDefinition(List<CreateDefinition> createDefinitions) {
    this.createDefinitions = createDefinitions;
  }

  public List<CreateDefinition> getCreateDefinitions() {
    return createDefinitions;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(XmlTagBuilder.getStartTag(this));
    for (CreateDefinition createDefinition : createDefinitions) {
      sb.append(createDefinition);
    }
    sb.append(XmlTagBuilder.getEndTag(this));
    return sb.toString();
  }

}
