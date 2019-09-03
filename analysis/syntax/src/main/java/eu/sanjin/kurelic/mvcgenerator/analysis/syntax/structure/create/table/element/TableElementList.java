package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element;

import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility.XmlTagBuilder;

import java.util.ArrayList;

public class TableElementList extends ArrayList<TableElement> {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(XmlTagBuilder.getStartTag(this));
    for (TableElement tableElement : this) {
      sb.append(tableElement);
    }
    sb.append(XmlTagBuilder.getEndTag(this));
    return sb.toString();
  }

}
