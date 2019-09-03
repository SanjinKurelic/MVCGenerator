package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

import java.util.Objects;

public class XmlTagBuilder {

  private static final String NEW_LINE = "\n";
  private static final String OPEN_START_TAG = "<";
  private static final String OPEN_END_TAG = "</";
  private static final String CLOSE_TAG = ">";
  private static final String SELF_CLOSE_TAG = "/>" + NEW_LINE;
  private static int ident = 0;

  // utils
  private static String getIdent(String value) {
    return "\t".repeat(Math.max(0, ident)) + value;
  }

  // tags
  public static String getStartTag(Object object) {
    return getStartTag(object.getClass().getSimpleName());
  }

  public static String getStartTag(String tagName) {
    String openTag = getIdent(OPEN_START_TAG + tagName + CLOSE_TAG + NEW_LINE);
    ident++;
    return openTag;
  }

  public static String getEndTag(Object object) {
    return getEndTag(object.getClass().getSimpleName());
  }

  public static String getEndTag(String tagName) {
    String closeTag = OPEN_END_TAG + tagName + CLOSE_TAG + NEW_LINE;
    ident--;
    return getIdent(closeTag);
  }

  public static String getSelfClosingTags(String tagName) {
    return getIdent(OPEN_START_TAG + tagName + SELF_CLOSE_TAG);
  }

  // values
  private static String getTokenValue(Token token) {
    if (Objects.isNull(token) || Objects.isNull(token.getValue())) {
      return "";
    }
    return token.getValue();
  }

  public static String surroundToken(Token token, String tagName) {
    return surroundToken(getTokenValue(token), tagName);
  }

  public static String surroundToken(String tokenValue, String tagName) {
    if (Objects.isNull(tokenValue) || tokenValue.isEmpty()) {
      return "";
    }
    return getIdent(OPEN_START_TAG + tagName + CLOSE_TAG + tokenValue + OPEN_END_TAG + tagName + CLOSE_TAG + NEW_LINE);
  }

  public static String surroundToken(Token token, Object object) {
    return surroundToken(getTokenValue(token), object.getClass().getSimpleName());
  }

  public static String surroundToken(String tokenValue, Object object) {
    return surroundToken(tokenValue, object.getClass().getSimpleName());
  }
}
