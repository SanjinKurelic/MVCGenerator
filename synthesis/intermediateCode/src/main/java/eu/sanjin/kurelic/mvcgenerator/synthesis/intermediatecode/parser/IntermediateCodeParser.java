package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntermediateCodeParser {

  private SemanticAttributeTable semanticAttributeTable;
  private TableAttribute currentTable;

  public IntermediateCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse() {
    simplifyCheckExpressions();
  }

  private void simplifyCheckExpressions() {
    ArrayList<Expression> checkExpressions;
    for(TableAttribute table : semanticAttributeTable.getTables().values()) {
      checkExpressions = new ArrayList<>();
      currentTable = table;
      for (Expression expression : table.getCheckAttribute().getCheckExpressions()) {
        checkExpressions.addAll(checkExpression(expression));
      }
      table.getCheckAttribute().setCheckExpressions(optimizeCheckExpressions(checkExpressions));
    }
  }

  /**
   * Split by AND operator
   * ex: x > 5 AND y < 6 AND z != 7 => [x > 5, y < 6, z != 7]
   */
  private List<Expression> checkExpression(Expression expression) {
    if (expression instanceof BinaryPredicate &&
      SpecialCharacterToken.AND.equals(((BinaryPredicate) expression).getOperator().getOperator())) {
      return Stream.of(
        checkExpression(((BinaryPredicate) expression).getFirstExpression()),
        checkExpression(((BinaryPredicate) expression).getSecondExpression())
      ).flatMap(Collection::stream).collect(Collectors.toList());
    }
    return List.of(expression);
  }

  private List<Expression> optimizeCheckExpressions(List<Expression> checkExpressions) {
    for (Expression expression : checkExpressions) {
      if (expression instanceof BinaryPredicate) {
        if (((BinaryPredicate) expression).getFirstExpression() instanceof Operand) {
          // operand <operator> operand
          // operand => keywordOperand, columnOperand, constantOperand
          // keywordOperand => true, false, unknown, default, null, (user, current_user, system_user, session_user), current_date, current_time, current_timestamp
          // <operator> => rational (class)
          // operand => unaryPredicate (not, plus, minus) - return Token
        }
      }
    }
    return checkExpressions;
  }

  private boolean isSimple(Expression expression) {
    if (expression instanceof UnaryPredicate &&
      SpecialCharacterToken.PLUS.equals(((UnaryPredicate) expression).getOperator().getOperator()) &&
      SpecialCharacterToken.MINUS.equals(((UnaryPredicate) expression).getOperator().getOperator())) {
      return false;
    }
    return true;
  }
}
