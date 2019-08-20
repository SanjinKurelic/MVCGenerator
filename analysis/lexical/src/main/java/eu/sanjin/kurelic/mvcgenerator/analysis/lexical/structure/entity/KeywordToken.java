package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public enum KeywordToken {

    ACTION, AND, BETWEEN, CASCADE, CHECK, COLLATE, CONSTRAINT, CREATE, CURRENT_USER,
    DEFAULT, DEFERRABLE, DEFERRED, DELETE, ESCAPE, FALSE, FOREIGN,
    FULL, IMMEDIATE, IN, INITIALLY, IS, KEY, LIKE, MATCH, NO, NOT, NULL,
    ON, OR, OVERLAPS, PARTIAL, PRECISION, PRIMARY, REFERENCES, SESSION_USER,
    SET, SYSTEM_USER, TABLE, TRUE, UNIQUE, UNKNOWN, UPDATE, USER, WITH, ZONE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static boolean contains(String value) {
        value = value.toLowerCase();
        for(KeywordToken token : values()) {
            if(token.name().toLowerCase().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Token token) {
        return toString().equals(token.getValue().toLowerCase());
    }

}
