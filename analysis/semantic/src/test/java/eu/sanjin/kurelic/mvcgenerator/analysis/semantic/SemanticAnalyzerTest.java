package eu.sanjin.kurelic.mvcgenerator.analysis.semantic;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.LexicalAnalyzer;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.SyntaxAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SemanticAnalyzerTest {

  private LexicalAnalyzer lexicalAnalyzer;
  private SyntaxAnalyzer syntaxAnalyzer;
  private SemanticAnalyzer semanticAnalyzer;

  @BeforeEach
  void setUp() {
    lexicalAnalyzer = new LexicalAnalyzer();
    syntaxAnalyzer = new SyntaxAnalyzer();
    semanticAnalyzer = new SemanticAnalyzer();
  }

  @Test
  void smallExample() {
    String sql = "CREATE TABLE Student (id INT CHECK(id > 5 OR id BETWEEN 0 AND 1));";
    try {
      lexicalAnalyzer.parse(sql);
      syntaxAnalyzer.parse(lexicalAnalyzer.getTokens());
      semanticAnalyzer.parse(syntaxAnalyzer.getSyntaxTree());
      System.out.println(semanticAnalyzer.getSemanticAttributeTable());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void bigExample() {
    StringBuilder code = new StringBuilder();
    code.append("CREATE TABLE Student(").append("\n");
    code.append("id INT PRIMARY KEY NOT NULL CHECK (id > 0),").append("\n");
    code.append("name VARCHAR(50) NOT NULL,").append("\n");
    code.append("surname VARCHAR(50) NOT NULL,").append("\n");
    code.append("dateOfBirth DATE CHECK (dateOfBirth <= '2001-01-01'),").append("\n");
    code.append("grade FLOAT DEFAULT 0.0 CHECK (grade BETWEEN 1 AND 5),").append("\n");
    code.append("address INT,").append("\n");
    code.append("CONSTRAINT FK_address FOREIGN KEY (address) REFERENCES \"Address Table\" (id)").append("\n");
    code.append(");");
    try {
      lexicalAnalyzer.parse(code.toString());
      syntaxAnalyzer.parse(lexicalAnalyzer.getTokens());
      semanticAnalyzer.parse(syntaxAnalyzer.getSyntaxTree());
      System.out.println(semanticAnalyzer.getSemanticAttributeTable());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
