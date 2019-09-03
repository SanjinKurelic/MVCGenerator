package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;

import java.util.ArrayList;

public class ConstraintList extends ArrayList<Constraint> {

  public String toString(Token columnName) {
    StringBuilder xml = new StringBuilder();
    for(Constraint constraint : this) {
      if (constraint instanceof ReferenceConstraint) {
        xml.append(constraint.toString(new Tokens(columnName)));
      } else {
        xml.append(constraint);
      }
    }
    return xml.toString();
  }
}
