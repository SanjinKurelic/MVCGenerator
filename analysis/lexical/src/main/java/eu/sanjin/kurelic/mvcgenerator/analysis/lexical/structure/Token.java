package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure;

public class Token {

  private static final String TO_STRING_FORMAT = "%d. {%s} => %s";
  private TokenType tokenType;
  private String value;
  private int lineNumber;

  public Token() {
  }

  public Token(TokenType tokenType, String value) {
    this(tokenType, value, -1);
  }

  public Token(TokenType tokenType, String value, int lineNumber) {
    this.tokenType = tokenType;
    this.value = value;
    this.lineNumber = lineNumber;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Integer getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT, lineNumber, value, tokenType);
  }
}
