package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AnnotationBuilder {

  protected String annotationValueStart = "(";
  protected String annotationValueEnd = ")";
  protected String annotationValueSeparator = ", ";
  protected String annotationValueEquality = "=";

  public abstract String annotationBlockStart();

  public abstract String annotationBlockEnd();

  public abstract String annotationKeyword();

  public String generateAnnotation(String annotationName, HashMap<String, String> annotationValues) {
    StringBuilder annotation = new StringBuilder();

    if (!Objects.isNull(annotationBlockStart())) {
      annotation.append(annotationBlockStart()).append("\n");
    }

    annotation.append(annotationKeyword()).append(annotationName).append(annotationValueStart);
    String separator = "";
    for (Map.Entry<String, String> entry : annotationValues.entrySet()) {
      annotation.append(separator);

      if (!Objects.isNull(entry.getKey())) {
        annotation.append(entry.getKey()).append(annotationValueEquality);
      }
      annotation.append(entry.getValue());

      separator = annotationValueSeparator;
    }
    annotation.append(annotationValueEnd).append("\n");

    if (!Objects.isNull(annotationBlockEnd())) {
      annotation.append(annotationBlockEnd()).append("\n");
    }

    return annotation.toString();
  }

  public String generateAnnotation(String annotationName) {
    return generateAnnotation(annotationName, new HashMap<>());
  }

  public String generateAnnotation(String annotationName, String annotationValue) {
    return generateAnnotation(annotationName, new HashMap<>() {{
      put(null, annotationValue);
    }});
  }
}
