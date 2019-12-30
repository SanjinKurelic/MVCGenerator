package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;

public class UnsupportedTargetFrameworkTargetCodeException extends TargetCodeException {

  private static final String UNSUPPORTED_FRAMEWORK_AND_LANGUAGE = "Framework %s for programming language %s is not supported";

  public UnsupportedTargetFrameworkTargetCodeException(TargetFramework targetFramework) {
    super(String.format(UNSUPPORTED_FRAMEWORK_AND_LANGUAGE, targetFramework.name(), targetFramework.getTargetProgrammingLanguage().name()));
  }
}
