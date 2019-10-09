package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

public abstract class Constraint {

  static final String COLUMN_TAG = "Column";

  public String getClassName() {
    return super.getClass().getSimpleName();
  }

  public String toString(Tokens columnList) {
    StringBuilder xml = new StringBuilder();

    xml.append(XmlTagBuilder.getStartTag(getClassName()));
    for(Token column : columnList) {
      xml.append(XmlTagBuilder.surroundToken(column, COLUMN_TAG));
    }
    xml.append(XmlTagBuilder.getEndTag(getClassName()));

    return xml.toString();
  }

  @Override
  public String toString() {
    return XmlTagBuilder.getSelfClosingTags(getClassName());
  }

}
