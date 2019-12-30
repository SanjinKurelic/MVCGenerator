package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition.builder;

import java.util.HashMap;

public interface AnnotationBuilder {

  String buildAnnotation(String annotationName, Object annotationValue);

  String buildAnnotation(String annotationName, HashMap<String, Object> annotationParams);
}
