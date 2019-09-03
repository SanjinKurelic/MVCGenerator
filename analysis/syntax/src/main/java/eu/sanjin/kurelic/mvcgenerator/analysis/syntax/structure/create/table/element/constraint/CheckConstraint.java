package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class CheckConstraint extends Constraint {

  private Expression checkExpression;

  public Expression getCheckExpression() {
    return checkExpression;
  }

  public void setCheckExpression(Expression checkExpression) {
    this.checkExpression = checkExpression;
  }

  @Override
  public String toString(Tokens columnList) {
    return toString();
  }

  @Override
  public String toString() {
    String xml = XmlTagBuilder.getStartTag(this);
    xml += checkExpression;
    return xml + XmlTagBuilder.getEndTag(this);
  }
}
