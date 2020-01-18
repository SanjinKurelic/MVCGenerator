package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntermediateCodeSynthesisTest {

  private IntermediateCodeSynthesis intermediateCodeSynthesis;

  @BeforeEach
  void setUp() {
    intermediateCodeSynthesis = new IntermediateCodeSynthesis();
  }

  @Test
  void bigExample() {
    StringBuilder code = new StringBuilder();
    code.append("CREATE TABLE Student(").append("\n");
    code.append("id INT PRIMARY KEY NOT NULL,").append("\n");
    code.append("name VARCHAR(50),").append("\n");
    code.append("surname VARCHAR(50) NOT NULL,").append("\n");
    code.append("dateOfBirth DATE CHECK (dateOfBirth <= CAST('2001-01-01' AS DATE) AND dateOfBirth > CURRENT_DATE),").append("\n");
    code.append("grade FLOAT DEFAULT 0.0,").append("\n");
    code.append("address INT,").append("\n");
    code.append("CHECK (id > -5 AND grade BETWEEN DEFAULT AND +5.0 AND name IS NOT NULL),").append("\n");
    code.append("CONSTRAINT FK_address FOREIGN KEY (address) REFERENCES \"Address Table\" (id)").append("\n");
    code.append(");").append("\n");
    code.append("CREATE TABLE \"Address Table\"(").append("\n");
    code.append("id INT PRIMARY KEY").append("\n");
    code.append(");");
    try {
      intermediateCodeSynthesis.parse(code.toString());
      System.out.println(intermediateCodeSynthesis.getSemanticAttributeTable());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
