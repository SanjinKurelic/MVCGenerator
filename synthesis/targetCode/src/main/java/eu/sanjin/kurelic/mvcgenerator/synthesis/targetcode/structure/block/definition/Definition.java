package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition.builder.AnnotationBuilder;

import java.util.HashMap;

public abstract class Definition {

  private String annotations;
  private AnnotationBuilder annotationBuilder;

  protected Definition(AnnotationBuilder annotationBuilder) {
    this.annotationBuilder = annotationBuilder;
    annotations = "";
  }

  public void addAnnotation(String annotationName, HashMap<String, Object> annotationParams) {
    annotations += annotationBuilder.buildAnnotation(annotationName, annotationParams) + "\n";
  }

  public void addAnnotation(String annotationName, Object annotationValue) {
    annotations += annotationBuilder.buildAnnotation(annotationName, annotationValue) + "\n";
  }

  protected String getAnnotations() {
    return annotations;
  }
}
