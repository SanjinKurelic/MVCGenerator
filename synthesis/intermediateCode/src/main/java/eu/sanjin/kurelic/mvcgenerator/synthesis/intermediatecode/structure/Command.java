package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure;

public enum Command {

  // Binary operator and plus-minus unary predicate
  PLUS, MINUS, DIVISION, MULTIPLY, MODULO, XOR,
  // Rational operator
  LESS, GREATER, EQUAL, LESS_EQUAL, GREATER_EQUAL, NOT_EQUAL,
  // Cast operator
  TO_BOOLEAN, TO_INTEGER, TO_REAL, TO_STRING, TO_DATE, TO_TIME, TO_DATETIME, TO_TIMESTAMP,
  // Other operators
  NOT, LIKE, OR, NEXT_IF, CONCAT,
  // Brackets TODO - if we return more than 2 elements (ex: 1 TO_REAL - does not need brackets)
  OPEN_BRACKET, CLOSED_BRACKET
}
