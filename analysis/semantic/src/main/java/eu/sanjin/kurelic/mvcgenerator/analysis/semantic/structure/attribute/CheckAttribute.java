package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;

import java.util.ArrayList;
import java.util.List;

public class CheckAttribute {

  private ArrayList<Expression> checkExpressions;

  public CheckAttribute() {
    checkExpressions = new ArrayList<>();
  }

  public void addExpression(Expression checkExpression) {
    checkExpressions.add(checkExpression);
  }

  public ArrayList<Expression> getCheckExpressions() {
    return checkExpressions;
  }

  public void setCheckExpressions(List<Expression> checkExpressions) {
    this.checkExpressions = new ArrayList<>(checkExpressions);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    checkExpressions.forEach(stringBuilder::append);
    return stringBuilder.toString();
  }
}
