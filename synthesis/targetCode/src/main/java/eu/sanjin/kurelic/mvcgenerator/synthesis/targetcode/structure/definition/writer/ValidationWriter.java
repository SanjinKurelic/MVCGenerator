package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;

import java.util.List;

public interface ValidationWriter {

  List<String> transformCheckExpressionToNativeIf(List<Expression> expressions);

}
