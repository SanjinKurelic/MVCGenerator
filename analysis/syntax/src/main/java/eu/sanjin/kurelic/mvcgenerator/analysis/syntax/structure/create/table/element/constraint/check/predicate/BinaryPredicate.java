package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class BinaryPredicate implements Predicate {

  private Expression firstExpression;
  private Expression secondExpression;
  private Operator operator;

  public Expression getFirstExpression() {
    return firstExpression;
  }

  public void setFirstExpression(Expression firstExpression) {
    this.firstExpression = firstExpression;
  }

  public Expression getSecondExpression() {
    return secondExpression;
  }

  public void setSecondExpression(Expression secondExpression) {
    this.secondExpression = secondExpression;
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
    xml += String.valueOf(firstExpression) + operator + secondExpression;
    return xml + XmlTagBuilder.getEndTag(this);
  }
}
