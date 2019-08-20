package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity;

public enum LexicalSpecialCharacters {

    ONE_LINE_COMMENT_CHARACTER('-'), ONE_LINE_COMMENT_END('\n'),
    MULTIPLE_LINE_COMMENT_SLASH('/'), MULTIPLE_LINE_COMMENT_ASTERIKS('*'),

    // Used when column names are in collision
    PRIVATE_IDENTIFIER('#'),

    STRING_DELIMITER('\''), QUOTED_DELIMITER('"'),
    SPACE_DELIMITER(' '), UNDERSCORE_DELIMITER('_'),
    ESCAPE_CHARACTER('\\'), DECIMAL_POINT('.');

    private char value;
    private LexicalSpecialCharacters(char value) {
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
