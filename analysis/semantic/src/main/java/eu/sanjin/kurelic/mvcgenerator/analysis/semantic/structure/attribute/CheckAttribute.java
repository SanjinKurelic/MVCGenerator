package eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;

import java.util.ArrayList;

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
}
