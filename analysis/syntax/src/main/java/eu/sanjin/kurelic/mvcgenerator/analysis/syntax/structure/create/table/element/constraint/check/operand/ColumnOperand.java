package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class ColumnOperand implements Operand {

    private Token operand;

    public ColumnOperand(Token operand) {
        setOperand(operand);
    }

    @Override
    public Token getOperand() {
        return operand;
    }

    @Override
    public void setOperand(Token operand) {
        this.operand = operand;
    }
}
