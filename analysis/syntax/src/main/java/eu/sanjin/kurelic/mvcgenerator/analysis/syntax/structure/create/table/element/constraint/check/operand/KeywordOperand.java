package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class KeywordOperand implements Operand {

  private Token operand;

  public KeywordOperand(Token operand) {
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

  @Override
  public String toString() {
    return XmlTagBuilder.surroundToken(operand, this);
  }
}
