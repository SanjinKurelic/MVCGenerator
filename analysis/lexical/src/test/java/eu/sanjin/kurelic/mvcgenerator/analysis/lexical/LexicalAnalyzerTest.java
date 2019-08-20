package eu.sanjin.kurelic.mvcgenerator.analysis.lexical;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.UnclosedCommentLexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.UnclosedQuotedTextLexicalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LexicalAnalyzerTest {

    private LexicalAnalyzer lexicalAnalyzer;
    private static final boolean PRINT_RESULT = true;

    @BeforeEach
    void setUp() {
        lexicalAnalyzer = new LexicalAnalyzer();
    }

    @Test
    void constantTest() {
        String input = "5 -5.5 '5.5.2015.' 'CREATE TABLE 'identifier'' '\"\"' DEFAULT '' identifier_abc identifier.abc";
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(input));
        System.out.println(lexicalAnalyzer.getTokens());
    }

    @Test
    void constantErrorTest() {
        Assertions.assertThrows(UnclosedQuotedTextLexicalException.class, () -> lexicalAnalyzer.parse("5.5 10 'Unclosed string 10 5.5"));
    }

    @Test
    void smallValidTest() {
        String input = "CREATE TABLE schema.table.column(\n" +
                "id INT PRIMARY KEY,\n" +
                "name VARCHAR(50) DEFAULT '' NOT NULL,\n" +
                "address SMALLINT REFERENCES \"table\" (column1, column2),\n" +
                ");\n";
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(input));
        System.out.println(lexicalAnalyzer.getTokens());
    }

    @Test
    void bigValidTest() {
        String input = "/*\nStudent database\n*/\n" +
                "CREATE TABLE school_db.Student(\n" +
                "id INT PRIMARY KEY,\n" +
                "name VARCHAR(20) NOT NULL,\n" +
                "surname VARCHAR(20) DEFAULT '',\n" +
                "dateOfBirth DATE DEFAULT '1.1.1901',\n" +
                "-- change default value\n" +
                "grade DOUBLE DEFAULT 1.0 CHECK (grade >= 1.0 AND <= 5.0),\n" +
                "address SMALLINT UNIQUE,\n" +
                "CONSTRAINT 'address_FK' FOREIGN KEY (address) REFERENCES \"addressTable\" (addressId),\n" +
                ");\n";
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(input));
        System.out.println(lexicalAnalyzer.getTokens());
    }


    @Test
    void validIdentifierTest() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("ab_cd.ef.gh9k"));
        System.out.println(lexicalAnalyzer.getTokens());
    }

    // LINE COMMENT

    @Test
    void lineCommentEmpty() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("--"));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void lineCommentSpace() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("-- "));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void lineCommentBreakAfter() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("--\nidentifier"));
        Assertions.assertFalse(lexicalAnalyzer.getTokens().isEmpty());
        if (PRINT_RESULT) {
            System.out.println(lexicalAnalyzer.getTokens());
        }
    }

    @Test
    void lineCommentBreakBefore() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("identifier--\n"));
        Assertions.assertFalse(lexicalAnalyzer.getTokens().isEmpty());
        if (PRINT_RESULT) {
            System.out.println(lexicalAnalyzer.getTokens());
        }
    }

    // MULTILINE COMMENT

    @Test
    void multilineCommentOpened() {
        Assertions.assertThrows(UnclosedCommentLexicalException.class, () -> lexicalAnalyzer.parse("/*"));
    }

    @Test
    void multilineCommentUnclosed() {
        Assertions.assertThrows(UnclosedCommentLexicalException.class, () -> lexicalAnalyzer.parse("/**"));
    }

    @Test
    void multilineCommentUnclosedEscaped() {
        Assertions.assertThrows(UnclosedCommentLexicalException.class, () -> lexicalAnalyzer.parse("/*\\*/"));
    }

    @Test
    void multilineCommentUnclosedEscaped2() {
        Assertions.assertThrows(UnclosedCommentLexicalException.class, () -> lexicalAnalyzer.parse("/**\\/"));
    }

    @Test
    void multilineCommentEmpty() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("/**/"));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void multilineCommentSpace() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("/* */"));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void multilineCommentStar() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("/***/"));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    // QUOTED TEXT

    @Test
    void quotedTextOpened() {
        Assertions.assertThrows(UnclosedQuotedTextLexicalException.class, () -> lexicalAnalyzer.parse("'"));
    }

    @Test
    void quotedTextUnclosedEscaped() {
        Assertions.assertThrows(UnclosedQuotedTextLexicalException.class, () -> lexicalAnalyzer.parse("'\\'"));
    }

    @Test
    void quotedTextEmpty() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("''"));
        Assertions.assertFalse(lexicalAnalyzer.getTokens().isEmpty());
        if (PRINT_RESULT) {
            System.out.println(lexicalAnalyzer.getTokens());
        }
    }

    @Test
    void quotedTextFull() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse("'Don\\'t use this kind of identifier.'"));
        Assertions.assertFalse(lexicalAnalyzer.getTokens().isEmpty());
        if (PRINT_RESULT) {
            System.out.println(lexicalAnalyzer.getTokens());
        }
    }

    // INPUT

    @Test
    void inputEmpty() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(""));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void inputSpace() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(" "));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

    @Test
    void inputNull() {
        Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(null));
        Assertions.assertTrue(lexicalAnalyzer.getTokens().isEmpty());
    }

}