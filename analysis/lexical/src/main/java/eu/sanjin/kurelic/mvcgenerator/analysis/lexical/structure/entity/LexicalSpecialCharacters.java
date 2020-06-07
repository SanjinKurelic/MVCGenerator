package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

public enum LexicalSpecialCharacters {

  ONE_LINE_COMMENT_CHARACTER('-'), ONE_LINE_COMMENT_END('\n'),
  MULTIPLE_LINE_COMMENT_SLASH('/'), @SuppressWarnings("SpellCheckingInspection") MULTIPLE_LINE_COMMENT_ASTERIKS('*'),

  STRING_DELIMITER('\''), QUOTED_DELIMITER('"'),
  SPACE_DELIMITER(' '), UNDERSCORE_DELIMITER('_'),
  ESCAPE_CHARACTER('\\'), DECIMAL_POINT('.');

  private final char value;

  LexicalSpecialCharacters(char value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public char toChar() {
    return value;
  }
}
