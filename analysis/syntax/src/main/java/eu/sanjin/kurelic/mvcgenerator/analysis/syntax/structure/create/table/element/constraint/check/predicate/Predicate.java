package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;

public interface Predicate extends Expression {

  Operator getOperator();

  void setOperator(Operator operator);

}
