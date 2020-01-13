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

        return; // TODO
      }
    }
  }
}
