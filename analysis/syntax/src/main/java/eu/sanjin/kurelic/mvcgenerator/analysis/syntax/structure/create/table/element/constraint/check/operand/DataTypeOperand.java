package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;

public class DataTypeOperand implements Operand {

  private DataType dataType;

  public DataTypeOperand(DataType dataType) {
    this.dataType = dataType;
  }

  @Override
  public Token getOperand() {
    return dataType.getType();
  }

  @Override
  public void setOperand(Token operand) {
    throw new UnsupportedOperationException();
  }
}
