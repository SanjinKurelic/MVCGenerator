package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

public enum SpecialCharacterToken {

  // Delimiter
  SCHEMA_TABLE_DELIMITER('.', SpecialCharacterClass.DELIMITER),
  COMA(',', SpecialCharacterClass.DELIMITER), SEMICOLON(';', SpecialCharacterClass.DELIMITER),
  LEFT_BRACKET('(', SpecialCharacterClass.DELIMITER), RIGHT_BRACKET(')', SpecialCharacterClass.DELIMITER),
  // Binary
  PLUS('+', SpecialCharacterClass.BINARY), MINUS('-', SpecialCharacterClass.BINARY),
  DIVISION('/', SpecialCharacterClass.BINARY), MULTIPLY('*', SpecialCharacterClass.BINARY),
  MODULO('%', SpecialCharacterClass.BINARY), AND('&', SpecialCharacterClass.BINARY),
  OR('|', SpecialCharacterClass.BINARY), XOR('^', SpecialCharacterClass.BINARY),
  // Rational
  LESS('<', SpecialCharacterClass.RATIONAL), GREATER('>', SpecialCharacterClass.RATIONAL),
  EQUAL('=', SpecialCharacterClass.RATIONAL), NOT('!', SpecialCharacterClass.COMPOUND),
  // Compound rational
  LESS_EQUAL('≤', SpecialCharacterClass.RATIONAL), GREATER_EQUAL('≥', SpecialCharacterClass.RATIONAL),
  NOT_EQUAL('≠', SpecialCharacterClass.RATIONAL),
  // String
  CONCAT('∪', SpecialCharacterClass.STRING_MANIPULATION),
  // Compound binary
  OVERLAPS('∩', SpecialCharacterClass.COMPOUND), LIKE('⊆', SpecialCharacterClass.COMPOUND),
  NOT_LIKE('\\', SpecialCharacterClass.COMPOUND), CAST('→', SpecialCharacterClass.COMPOUND);

  private char value;
  private SpecialCharacterClass type;

  SpecialCharacterToken(char value, SpecialCharacterClass type) {
    this.value = value;
    this.type = type;
  }

  @Override
  public String toString() {
    return String.valueOf(toChar());
  }

  public char toChar() {
    return value;
  }

  public SpecialCharacterClass getRootType() {
    return type;
  }

  public static boolean contains(char token) {
    for (SpecialCharacterToken t : values()) {
      // Compound classes
      if (isCompound(t.toString())) {
        continue;
      }
      // Special internal characters
      if (t.toChar() == LESS_EQUAL.toChar()
        || t.toChar() == GREATER_EQUAL.toChar()
        || t.toChar() == NOT_EQUAL.toChar()) {
        continue;
      }
      // Compare
      if (t.toChar() == token) {
        return true;
      }
    }
    return false;
  }

  public static boolean isType(SpecialCharacterClass type, String value) {
    for (SpecialCharacterToken t : values()) {
      if (t.toChar() == value.charAt(0)) {
        return t.getRootType() == type;
      }
    }
    return false;
  }

  public static boolean isBinary(String value) {
    return isType(SpecialCharacterClass.BINARY, value);
  }

  public static boolean isRational(String value) {
    return isType(SpecialCharacterClass.RATIONAL, value);
  }

  public static boolean isCompound(String value) {
    return isType(SpecialCharacterClass.COMPOUND, value);
  }

  public static SpecialCharacterToken toSpecialCharacterToken(String value) {
    for (SpecialCharacterToken specialCharacterToken : values()) {
      if (specialCharacterToken.toString().equals(value)) {
        return specialCharacterToken;
      }
    }
    return null;
  }
}
