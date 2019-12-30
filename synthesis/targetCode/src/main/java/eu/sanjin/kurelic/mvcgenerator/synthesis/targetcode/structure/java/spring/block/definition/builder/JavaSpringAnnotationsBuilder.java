package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.block.definition.builder;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.definition.builder.AnnotationBuilder;

import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;

public class JavaSpringAnnotationsBuilder implements AnnotationBuilder {

  public String buildAnnotation(String annotationName, HashMap<String, Object> annotationParameters) {
    StringBuilder annotation = new StringBuilder();
    annotation.append("@").append(annotationName).append("(");

    // append parameters
    StringJoiner parameters = new StringJoiner(", ");
    annotationParameters.forEach((paramName, paramValue) -> {
      String parameter = "";
      if (!Objects.isNull(paramName)) {
        parameter += paramName + "=";
      }
      if (paramValue instanceof String) {
        parameter += "\"" + paramValue + "\"";
      } else {
        parameter += paramValue;
      }
      parameters.add(parameter);
    });
    annotation.append(parameters.toString()).append(")\n");
    return annotation.toString();
  }

  public String buildAnnotation(String annotationName, Object annotationValue) {
    return buildAnnotation(annotationName, new HashMap<>() {{
      put(null, annotationValue);
    }});
  }
}
