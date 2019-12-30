package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition.builder.AnnotationBuilder;

public abstract class ClassDefinition extends Definition {

  protected ClassDefinition(AnnotationBuilder annotationBuilder) {
    super(annotationBuilder);
  }

  public abstract String generateContent(String body);

}
