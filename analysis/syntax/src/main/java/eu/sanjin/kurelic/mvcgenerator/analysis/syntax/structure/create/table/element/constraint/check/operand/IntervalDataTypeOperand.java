package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class IntervalDataTypeOperand extends DataTypeOperand {

  private static final String INTERVAL_QUALIFIER = "INTERVAL";
  private static final String FROM_TAG = "From";
  private static final String TO_TAG = "To";
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

  @Override
  public String toString() {
    String xml = XmlTagBuilder.getStartTag(this);
    xml += XmlTagBuilder.surroundToken(from, FROM_TAG);
    xml += XmlTagBuilder.surroundToken(to, TO_TAG);
    return xml + XmlTagBuilder.getEndTag(this);
  }
}
