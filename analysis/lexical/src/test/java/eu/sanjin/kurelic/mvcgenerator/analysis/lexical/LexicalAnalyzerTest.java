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
  void smallExample() {
    String input = "CREATE TABLE Student(\n" +
      "id INT PRIMARY KEY,\n" +
      "name VARCHAR(50) DEFAULT '' NOT NULL,\n" +
      "addressId SMALLINT REFERENCES \"Address\" (id)\n" +
      ");\n";
    Assertions.assertDoesNotThrow(() -> lexicalAnalyzer.parse(input));
    System.out.println(lexicalAnalyzer.getTokens());
  }

  @Test
  void bigExample() {
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
}