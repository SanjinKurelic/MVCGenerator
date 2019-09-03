package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class UnaryPredicate implements Predicate {

  private Expression expression;
  private Operator operator;

  public Expression getExpression() {
    return expression;
  }

  public void setExpression(Expression expression) {
    this.expression = expression;
  }

  @Override
  public Operator getOperator() {
    return operator;
  }

  @Override
  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  @Override
  public String toString() {
    String xml = XmlTagBuilder.getStartTag(this);
    xml += operator.toString() + expression.toString();
    return xml + XmlTagBuilder.getEndTag(this);
  }
}
