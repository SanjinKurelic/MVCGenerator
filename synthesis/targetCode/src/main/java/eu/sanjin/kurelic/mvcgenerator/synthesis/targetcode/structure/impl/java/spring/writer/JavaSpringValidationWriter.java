package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.writer;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer.ValidationWriter;

import java.util.List;

public class JavaSpringValidationWriter implements ValidationWriter {
  @Override
  public List<String> transformCheckExpressionToNativeIf(List<Expression> expressions) {
    return null;
  }
}
