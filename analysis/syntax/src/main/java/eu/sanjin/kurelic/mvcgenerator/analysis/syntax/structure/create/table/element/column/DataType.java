package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.util.Objects;

public class DataType {

  private static final String TYPE_TAG = "Type";
  private static final String PRECISION_OR_LENGTH_TAG = "PrecisionOrLength";
  private static final String SCALE_TAG = "Scale";

  private Token type;
  private Token precisionOrLength;
  private Token scale;

  public DataType(Token type) {
    this.type = type;
  }

  public Token getType() {
    return type;
  }

  public Token getPrecisionOrLength() {
    return precisionOrLength;
  }

  public void setPrecisionOrLength(Token precisionOrLength) {
    this.precisionOrLength = precisionOrLength;
  }

  public Token getScale() {
    return scale;
  }

  public void setScale(Token scale) {
    this.scale = scale;
  }

  @Override
  public String toString() {
    String xml = XmlTagBuilder.getStartTag(this);
    xml += XmlTagBuilder.surroundToken(type, TYPE_TAG);
    xml += XmlTagBuilder.surroundToken(precisionOrLength, PRECISION_OR_LENGTH_TAG);
    xml += XmlTagBuilder.surroundToken(scale, SCALE_TAG);
    return xml + XmlTagBuilder.getEndTag(this);
  }

}
