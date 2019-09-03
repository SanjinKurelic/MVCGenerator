package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class Operator {

  private SpecialCharacterToken operator;
  private Integer lineNumber;

  public Operator() {
  }

  public Operator(SpecialCharacterToken operator, Integer lineNumber) {
    this.operator = operator;
    this.lineNumber = lineNumber;
  }

  public SpecialCharacterToken getOperator() {
    return operator;
  }

  public void setOperator(SpecialCharacterToken operator) {
    this.operator = operator;
  }

  public Integer getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(Integer lineNumber) {
    this.lineNumber = lineNumber;
  }

  @Override
  public String toString() {
    return XmlTagBuilder.surroundToken(operator.name(), this);
  }
}
