package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.block.definition;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition.ClassDefinition;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.block.definition.builder.JavaSpringAnnotationsBuilder;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util.CodePrettifyUtil;

public class JavaSpringClassDefinition extends ClassDefinition {
  private static final String CLASS_KEYWORD = "public class";
  private String className;

  public JavaSpringClassDefinition(String className) {
    super(new JavaSpringAnnotationsBuilder());
    this.className = className;
  }

  public String generateContent(String body) {
    return getAnnotations() + CLASS_KEYWORD + " " + className + " {\n" +
      CodePrettifyUtil.indentCode(body) +
      "}\n";
  }
}
