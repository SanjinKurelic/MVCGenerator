package eu.sanjin.kurelic.mvcgenerator.analysis.syntax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SyntaxAnalyzerTest {

  private SyntaxAnalyzer syntaxAnalyzer;

  @BeforeEach
  void setUp() {
    syntaxAnalyzer = new SyntaxAnalyzer();
  }

  @Test
  void smallExample() {
    String sql = "CREATE TABLE Student (id INT CHECK(id > 5 OR id BETWEEN 0 AND 1));";
    try {
      syntaxAnalyzer.parse(sql);
      System.out.println(syntaxAnalyzer.getSyntaxTree());
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
      syntaxAnalyzer.parse(code.toString());
      System.out.println(syntaxAnalyzer.getSyntaxTree());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
