package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public class ZonedDataTypeOperand extends DataTypeOperand {

  private static final String ZONED_DATE_QUALIFIER = "ZONED_DATE";
  private Expression zone;

  public ZonedDataTypeOperand(int lineNumber) {
    super(new DataType(new Token(TokenType.DATA_TYPE, ZONED_DATE_QUALIFIER, lineNumber)));
  }

  public Expression getZone() {
    return zone;
  }

  public void setZone(Expression zone) {
    this.zone = zone;
  }

  @Override
  public String toString() {
    return XmlTagBuilder.surroundToken(zone.toString(), this);
  }
}
