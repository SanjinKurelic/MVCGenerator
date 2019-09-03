package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure;

import java.util.ArrayList;
import java.util.Collections;

public class Tokens extends ArrayList<Token> {

  public Tokens() {
  }

  public Tokens(Token... tokens) {
      Collections.addAll(this, tokens);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Token t : this) {
      sb.append(t).append("\n");
    }
    return sb.toString();
  }

}
