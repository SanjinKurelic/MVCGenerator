package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public enum KeywordToken {

  ACTION, AND, AS, AT, BETWEEN, CASCADE, CAST, CHECK, COLLATE, CONSTRAINT, CREATE,
  CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP, CURRENT_USER,
  DAY, DEFAULT, DEFERRABLE, DEFERRED, DELETE, ESCAPE, FALSE, FOREIGN, FULL,
  HOUR, IMMEDIATE, IN, INITIALLY, IS, KEY, LIKE, LOCAL, MATCH, MINUTE, MONTH, NO, NOT, NULL,
  ON, OR, OVERLAPS, PARTIAL, PRIMARY, REFERENCES, SECOND, SESSION_USER,
  SET, SYSTEM_USER, TABLE, TO, TRUE, UNIQUE, UNKNOWN, UPDATE, USER, WITH, YEAR, ZONE;

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public static boolean contains(String value) {
    value = value.toLowerCase();
    for (KeywordToken token : values()) {
      if (token.name().toLowerCase().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public boolean equals(Token token) {
    return toString().equals(token.getValue().toLowerCase());
  }
}
