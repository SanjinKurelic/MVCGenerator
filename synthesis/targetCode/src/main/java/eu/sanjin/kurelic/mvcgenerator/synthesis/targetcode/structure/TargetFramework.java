package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum TargetFramework {

  SPRING(TargetProgrammingLanguage.JAVA), SERVLET(TargetProgrammingLanguage.JAVA),
  LARAVEL(TargetProgrammingLanguage.PHP), SYMFONY(TargetProgrammingLanguage.PHP), PURE_PHP(TargetProgrammingLanguage.PHP),
  ASP(TargetProgrammingLanguage.C_SHARP), ASP_CORE(TargetProgrammingLanguage.C_SHARP);

  private final TargetProgrammingLanguage targetProgrammingLanguage;

  TargetFramework(TargetProgrammingLanguage targetProgrammingLanguage) {
    this.targetProgrammingLanguage = targetProgrammingLanguage;
  }

  public TargetProgrammingLanguage getTargetProgrammingLanguage() {
    return targetProgrammingLanguage;
  }
}
