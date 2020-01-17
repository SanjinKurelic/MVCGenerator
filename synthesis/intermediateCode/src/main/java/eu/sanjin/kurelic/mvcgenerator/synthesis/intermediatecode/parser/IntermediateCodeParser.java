package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterClass;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.components.DataTypeAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ColumnOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ConstantOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.KeywordOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntermediateCodeParser {

  private SemanticAttributeTable semanticAttributeTable;
  private TableAttribute currentTable;
  private ColumnAttribute currentColumn;
  private boolean removeExpression;
  private static final String NOT_NULL = "NOT_NULL";

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

  /**
   * Optimize rational binary predicates
   */
  private List<Expression> optimizeCheckExpressions(List<Expression> checkExpressions) {
    Token firstOperand, secondOperand, operand;
    ArrayList<Expression> removeExpressions = new ArrayList<>();
    for (Expression expression : checkExpressions) {
      currentColumn = null;
      removeExpression = false;
      operand = null;

      if (expression instanceof BinaryPredicate
          && SpecialCharacterClass.RATIONAL.equals(((BinaryPredicate) expression).getOperator().getOperator().getRootType())) {
        firstOperand = getSimpleExpression(((BinaryPredicate) expression).getFirstExpression());
        secondOperand = getSimpleExpression(((BinaryPredicate) expression).getSecondExpression());

        if (!Objects.isNull(firstOperand) && !Objects.isNull(secondOperand) && !Objects.isNull(currentColumn)) {
          if (firstOperand.getValue().equals(currentColumn.getColumnName().getValue())) {
            operand = secondOperand;
          }
          else if (secondOperand.getValue().equals(currentColumn.getColumnName().getValue())){
            operand = firstOperand;
          }
          // Remove expression that is converted to attribute validation
          if (!Objects.isNull(operand) && appendAttributes(operand, ((BinaryPredicate) expression).getOperator())) {
            removeExpression = true;
          }
        }

        if (removeExpression) {
          removeExpressions.add(expression);
        }
      }
    }
    checkExpressions.removeAll(removeExpressions);

    return checkExpressions;
  }

  private Token getSimpleExpression(Expression expression) {
    if (expression instanceof Operand) {
      return getSimpleOperand((Operand) expression);
    }
    else if (expression instanceof UnaryPredicate) {
      return getSimplePredicate((Predicate) expression);
    }
    return null;
  }

  /**
   * Only ColumnOperand, KeywordOperand and ConstantOperand are supported.
   * Only these keywords are supported in KeywordOperand: true, false, unknown, default, null, current_date, current_time, current_timestamp
   *
   * Set flag for removing expression that have these keywords as they are part of database engine: user, current_user, system_user, session_user
   */
  private Token getSimpleOperand(Operand operand) {
    // Check valid operand instance
    if (!(operand instanceof ColumnOperand) && !(operand instanceof ConstantOperand) && !(operand instanceof KeywordOperand)) {
      return null;
    }
    // Remove expressions containing keywords user, current_user, system_user, session_user as they are part of database engine
    if (KeywordToken.USER.equals(operand.getOperand()) || KeywordToken.CURRENT_USER.equals(operand.getOperand()) ||
        KeywordToken.SYSTEM_USER.equals(operand.getOperand()) || KeywordToken.SESSION_USER.equals(operand.getOperand())) {
      removeExpression = true;
      return null;
    }
    // Check allowed keywords
    KeywordToken[] allowedKeywords = new KeywordToken[] {
        KeywordToken.TRUE, KeywordToken.FALSE, KeywordToken.UNKNOWN, KeywordToken.DEFAULT, KeywordToken.NULL,
        KeywordToken.CURRENT_DATE, KeywordToken.CURRENT_TIME, KeywordToken.CURRENT_TIMESTAMP
    };
    if (operand instanceof KeywordOperand && Arrays.stream(allowedKeywords).noneMatch(k -> k.equals(operand.getOperand()))) {
      return null;
    }
    // Replace keyword default with actual value
    if (KeywordToken.DEFAULT.equals(operand.getOperand())) {
      if (Objects.isNull(currentColumn)) {
        return null;
      }
      return currentColumn.getDefaultValue();
    }
    // Set current column
    if (operand instanceof ColumnOperand) {
      currentColumn = currentTable.getColumn(operand.getOperand().getValue());
    }

    return operand.getOperand();
  }

  /**
   * Calculate NOT, MINUS and PLUS unary operations. If operations are done on Column operand than this method
   * return null because that case is complex predicate. Only constant integer, constant real and keywords true,
   * false, unknown, null are supported
   *
   * MINUS => 5 -> -5, 5.5 -> -5.5
   * NOT => true -> false, false -> true, null/unknown -> not_null, not_null -> null
   */
  private Token getSimplePredicate(Predicate predicate) {
    if (!(predicate instanceof UnaryPredicate) ||
        (!SpecialCharacterToken.PLUS.equals(predicate.getOperator().getOperator()) &&
            !SpecialCharacterToken.MINUS.equals(predicate.getOperator().getOperator()) &&
            !SpecialCharacterToken.NOT.equals(predicate.getOperator().getOperator()))) {
      return null;
    }
    Token value = getSimpleExpression(((UnaryPredicate) predicate).getExpression());
    if (value == null) {
      return null;
    }
    // Minus operator (plus does not change value so we skip it)
    if (SpecialCharacterToken.MINUS.equals(predicate.getOperator().getOperator())) {
      if (TokenType.CONSTANT_INTEGER_VALUE.equals(value.getTokenType())) {
        value.setValue(String.valueOf(-Integer.parseInt(value.getValue())));
      }
      else if (TokenType.CONSTANT_REAL_NUMBER_VALUE.equals(value.getTokenType())) {
        value.setValue(String.valueOf(-Double.parseDouble(value.getValue())));
      }
      // Minus on unsupported operand
      else {
        return null;
      }
    }
    // Not operator
    if (SpecialCharacterToken.NOT.equals(predicate.getOperator().getOperator())) {
      value = inverseBooleanValue(value);
    }

    return value;
  }

  private Token inverseBooleanValue(Token value) {
    if (!TokenType.KEYWORD.equals(value.getTokenType())) {
      return null;
    }

    if (KeywordToken.TRUE.equals(value)) {
      value.setValue(KeywordToken.FALSE.toString());
    }
    else if (KeywordToken.FALSE.equals(value)) {
      value.setValue(KeywordToken.TRUE.toString());
    }
    else if (KeywordToken.UNKNOWN.equals(value) || KeywordToken.NULL.equals(value)) {
      value.setValue(NOT_NULL);
    }
    else if (value.getValue().equals(NOT_NULL)) {
      value.setValue(KeywordToken.NULL.toString());
    }
    // Not supported operand (only true, false, null, unknown and not_null are supported)
    else {
      return null;
    }

    return value;
  }

  /**
   * operator => <, > -> numericMin, numericMax, isFuture, isFutureOrPresent, isPast, isPastOrPresent
   * operator => =, != -> assertTrue, assertNotTrue, assertFalse, assertNotFalse, assertUnknown, assertNotUnknown
   */
  private boolean appendAttributes(Token value, Operator operator) {
    switch (currentColumn.getDataType()) {
      case BOOLEAN:
        return appendBooleanAttributes(value, operator);
      case INTEGER:
      case REAL:
        return  appendNumberAttributes(value, operator);
      case DATE:
      case DATETIME:
      case TIME:
      case TIMESTAMP:
      case INTERVAL:
      case ZONED_DATETIME:
        return appendDateAttributes(value, operator);
      default:
        return false;
    }
    // Save
    currentTable.getColumns().put(currentColumn.getColumnName().getValue(), null);
  }

  private boolean appendBooleanAttributes(Token value, Operator operator) {
    if (SpecialCharacterToken.NOT_EQUAL.equals(operator.getOperator())) {
      value = inverseBooleanValue(value);
    }
  }

  private boolean appendNumberAttributes(Token value, Operator operator) {
  }

  private boolean appendDateAttributes(Token value, Operator operator) {
  }
}
