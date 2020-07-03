package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.builder;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util.CodePrettifyUtil;

public abstract class ClassBuilder {

  public abstract String classKeyword();

  public String generateClass(String className, String outsideBody, String body) {
    return String.format("%s\n%s %s {\n%s\n}\n",
      outsideBody,
      classKeyword(),
      className,
      CodePrettifyUtil.indentCode(body)
    );
  }

}
