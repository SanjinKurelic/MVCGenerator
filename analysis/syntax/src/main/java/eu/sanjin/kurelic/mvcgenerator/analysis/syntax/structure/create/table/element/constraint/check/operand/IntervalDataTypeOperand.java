package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;

public class IntervalDataTypeOperand extends DataTypeOperand {

  private static final String INTERVAL_QUALIFIER = "INTERVAL";
  private Token from;
  private Token to;

  public IntervalDataTypeOperand(int lineNumber) {
    super(new DataType(new Token(TokenType.DATA_TYPE, INTERVAL_QUALIFIER, lineNumber)));
  }

  public Token getFrom() {
    return from;
  }

  public void setFrom(Token from) {
    this.from = from;
  }

  public Token getTo() {
    return to;
  }

  public void setTo(Token to) {
    this.to = to;
  }
}
