package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;

public class CheckConstraint implements Constraint {

    private Expression checkExpression;

    public Expression getCheckExpression() {
        return checkExpression;
    }

    public void setCheckExpression(Expression checkExpression) {
        this.checkExpression = checkExpression;
    }
}
