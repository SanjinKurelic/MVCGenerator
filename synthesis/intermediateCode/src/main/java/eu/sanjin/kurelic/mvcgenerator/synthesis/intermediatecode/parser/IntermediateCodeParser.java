package eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterClass;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.SemanticAttributeTable;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.ColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ColumnOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.ConstantOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.KeywordOperand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.Operand;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.Predicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception.IntermediateCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception.MinMaxIntermediateCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.exception.ValidationMismatchIntermediateCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure.ExtendedBooleanColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure.ExtendedDateColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure.ExtendedIntegerColumnAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.structure.ExtendedRealColumnAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntermediateCodeParser {

  private static final String NOT_NULL = "not null";
  private static final String IN_FUTURE = "in future";
  private static final String IN_PAST = "in past";

  private final SemanticAttributeTable semanticAttributeTable;
  private TableAttribute currentTable;
  private ColumnAttribute currentColumn;
  private boolean removeExpression;

  public IntermediateCodeParser(SemanticAttributeTable semanticAttributeTable) {
    this.semanticAttributeTable = semanticAttributeTable;
  }

  public void parse() throws IntermediateCodeException {
    simplifyCheckExpressions();
  }

  private void simplifyCheckExpressions() throws IntermediateCodeException {
    ArrayList<Expression> checkExpressions;
    for (TableAttribute table : semanticAttributeTable.getTables().values()) {
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
  private List<Expression> optimizeCheckExpressions(List<Expression> checkExpressions) throws IntermediateCodeException {
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
          } else if (secondOperand.getValue().equals(currentColumn.getColumnName().getValue())) {
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
    } else if (expression instanceof UnaryPredicate) {
      return getSimplePredicate((Predicate) expression);
    }
    return null;
  }

  /**
   * Only ColumnOperand, KeywordOperand and ConstantOperand are supported.
   * Only these keywords are supported in KeywordOperand: true, false, unknown, default, null, current_date, current_time, current_timestamp
   * <p>
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
    KeywordToken[] allowedKeywords = new KeywordToken[]{
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
   * <p>
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
      } else if (TokenType.CONSTANT_REAL_NUMBER_VALUE.equals(value.getTokenType())) {
        value.setValue(String.valueOf(-Double.parseDouble(value.getValue())));
      }
      // Minus on unsupported operand
      else {
        return null;
      }
    }
    // Not operator
    if (SpecialCharacterToken.NOT.equals(predicate.getOperator().getOperator())) {
      value = inverseBooleanValue(value, SpecialCharacterToken.NOT_EQUAL);
    }

    return value;
  }

  private Token inverseBooleanValue(Token value, SpecialCharacterToken operator) {
    if (!TokenType.KEYWORD.equals(value.getTokenType()) && !SpecialCharacterToken.EQUAL.equals(operator) &&
      !SpecialCharacterToken.NOT_EQUAL.equals(operator)) {
      return null;
    }
    // If equal do not inverse value
    if (SpecialCharacterToken.EQUAL.equals(operator)) {
      return value;
    }

    if (KeywordToken.TRUE.equals(value)) {
      value.setValue(KeywordToken.FALSE.toString());
    } else if (KeywordToken.FALSE.equals(value)) {
      value.setValue(KeywordToken.TRUE.toString());
    } else if (KeywordToken.UNKNOWN.equals(value) || KeywordToken.NULL.equals(value)) {
      value.setValue(NOT_NULL);
    } else if (value.getValue().equals(NOT_NULL)) {
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
   * operator => =, != -> assertTrue, assertFalse, assertUnknown, assertNotUnknown
   */
  private boolean appendAttributes(Token value, Operator operator) throws IntermediateCodeException {
    if (KeywordToken.NULL.equals(value) || NOT_NULL.equals(value.getValue())) {
      return appendNullAttribute(value, operator);
    }
    switch (currentColumn.getDataType()) {
      case BOOLEAN:
        return appendBooleanAttributes(value, operator);
      case INTEGER:
        return appendIntegerAttributes(value, operator);
      case REAL:
        return appendRealAttributes(value, operator);
      case DATE:
      case DATETIME:
      case TIME:
      case TIMESTAMP:
      case INTERVAL:
      case ZONED_DATETIME:
        return appendDateAttributes(value, operator);
    }
    return false;
  }

  private boolean appendBooleanAttributes(Token value, Operator operator) throws IntermediateCodeException {
    value = inverseBooleanValue(value, operator.getOperator());
    if (Objects.isNull(value)) {
      return false;
    }
    // Initialize extended attribute
    ExtendedBooleanColumnAttribute columnAttribute;
    if (currentColumn instanceof ExtendedBooleanColumnAttribute) {
      columnAttribute = (ExtendedBooleanColumnAttribute) currentColumn;
    } else {
      columnAttribute = new ExtendedBooleanColumnAttribute(currentColumn);
    }
    // Set attributes
    if (KeywordToken.TRUE.equals(value)) {
      if (columnAttribute.getAssertTrue().equals(Boolean.FALSE) || columnAttribute.getAssertFalse().equals(Boolean.TRUE)) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, KeywordToken.TRUE.toString(), KeywordToken.FALSE.toString());
      }
      columnAttribute.setAssertTrue(true);
      columnAttribute.setAssertFalse(false);
    } else if (KeywordToken.FALSE.equals(value)) {
      if (columnAttribute.getAssertTrue().equals(Boolean.TRUE) || columnAttribute.getAssertFalse().equals(Boolean.FALSE)) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, KeywordToken.TRUE.toString(), KeywordToken.FALSE.toString());
      }
      columnAttribute.setAssertTrue(false);
      columnAttribute.setAssertFalse(true);
    } else { // Unknown or null
      // If column is not null - throw validation mismatch
      if (!Objects.isNull(columnAttribute.isNotNull()) && columnAttribute.isNotNull()) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, KeywordToken.NULL.toString(), NOT_NULL);
      }
      columnAttribute.setNotNull(false);
    }

    // Save
    return saveAttributes(columnAttribute);
  }

  private boolean appendIntegerAttributes(Token value, Operator operator) throws IntermediateCodeException {
    int intValue = Integer.parseInt(value.getValue());
    // Initialize extended attribute
    ExtendedIntegerColumnAttribute columnAttribute;
    if (currentColumn instanceof ExtendedIntegerColumnAttribute) {
      columnAttribute = (ExtendedIntegerColumnAttribute) currentColumn;
    } else {
      columnAttribute = new ExtendedIntegerColumnAttribute(currentColumn);
    }
    // Set attributes
    if (SpecialCharacterToken.LESS_EQUAL.equals(operator.getOperator())) {
      // If current max is smaller than new one
      if (!Objects.isNull(columnAttribute.getMax()) && columnAttribute.getMax() < intValue) {
        return true;
      }
      columnAttribute.setMax(intValue);
    } else if (SpecialCharacterToken.LESS.equals(operator.getOperator())) {
      // If current max is smaller than new one
      if (!Objects.isNull(columnAttribute.getMax()) && columnAttribute.getMax() < (intValue - 1)) {
        return true;
      }
      columnAttribute.setMax(intValue - 1);
    } else if (SpecialCharacterToken.GREATER_EQUAL.equals(operator.getOperator())) {
      // If current min is bigger than new one
      if (!Objects.isNull(columnAttribute.getMin()) && columnAttribute.getMin() > intValue) {
        return true;
      }
      columnAttribute.setMin(intValue);
    } else if (SpecialCharacterToken.GREATER.equals(operator.getOperator())) {
      // If current min is bigger than new one
      if (!Objects.isNull(columnAttribute.getMin()) && columnAttribute.getMin() > (intValue + 1)) {
        return true;
      }
      columnAttribute.setMin(intValue + 1);
    }
    // Unsupported operator
    else {
      return false;
    }
    // Check min-max
    if (!Objects.isNull(columnAttribute.getMin()) && !Objects.isNull(columnAttribute.getMax())
      && columnAttribute.getMin() >= columnAttribute.getMax()) {
      throw new MinMaxIntermediateCodeException(columnAttribute, columnAttribute.getMin(), columnAttribute.getMax());
    }

    // Save
    return saveAttributes(columnAttribute);
  }

  private boolean appendRealAttributes(Token value, Operator operator) throws IntermediateCodeException {
    double realValue = Double.parseDouble(value.getValue());
    // Initialize extended attribute
    ExtendedRealColumnAttribute columnAttribute;
    if (currentColumn instanceof ExtendedRealColumnAttribute) {
      columnAttribute = (ExtendedRealColumnAttribute) currentColumn;
    } else {
      columnAttribute = new ExtendedRealColumnAttribute(currentColumn);
    }
    // Set attributes
    if (SpecialCharacterToken.LESS_EQUAL.equals(operator.getOperator())) {
      if (!Objects.isNull(columnAttribute.getMax()) && columnAttribute.getMax() < realValue) {
        return true;
      }
      columnAttribute.setMax(realValue);
      columnAttribute.setInclusiveMax(true);
    } else if (SpecialCharacterToken.LESS.equals(operator.getOperator())) {
      if (!Objects.isNull(columnAttribute.getMax()) && columnAttribute.getMax() < realValue) {
        return true;
      }
      // Inclusive is more specific operator
      if (columnAttribute.getMax().equals(realValue) && columnAttribute.isInclusiveMax()) {
        return true;
      }
      columnAttribute.setMax(realValue);
      columnAttribute.setInclusiveMax(false);
    } else if (SpecialCharacterToken.GREATER_EQUAL.equals(operator.getOperator())) {
      if (!Objects.isNull(columnAttribute.getMin()) && columnAttribute.getMin() > realValue) {
        return true;
      }
      columnAttribute.setMin(realValue);
      columnAttribute.setInclusiveMin(true);
    } else if (SpecialCharacterToken.GREATER.equals(operator.getOperator())) {
      if (!Objects.isNull(columnAttribute.getMin()) && columnAttribute.getMin() > realValue) {
        return true;
      }
      // Inclusive is more specific operator
      if (columnAttribute.getMin().equals(realValue) && columnAttribute.isInclusiveMin()) {
        return true;
      }
      columnAttribute.setMin(realValue);
      columnAttribute.setInclusiveMin(false);
    }
    // Unsupported operator
    else {
      return false;
    }
    // Check min-max
    if (!Objects.isNull(columnAttribute.getMin()) && !Objects.isNull(columnAttribute.getMax())
      && columnAttribute.getMin() >= columnAttribute.getMax()) {
      throw new MinMaxIntermediateCodeException(columnAttribute, columnAttribute.getMin(), columnAttribute.getMax());
    }

    // Save
    return saveAttributes(columnAttribute);
  }

  /**
   * Add attributes that check if date column attribute is in past or future.
   * This method only accepts values that are keywords: CURRENT_DATE, CURRENT_TIME and CURRENT_TIMESTAMP
   */
  private boolean appendDateAttributes(Token value, Operator operator) throws IntermediateCodeException {
    if (!TokenType.KEYWORD.equals(value.getTokenType())) {
      return false;
    }
    if (!KeywordToken.CURRENT_DATE.equals(value) && !KeywordToken.CURRENT_TIME.equals(value) &&
      !KeywordToken.CURRENT_TIMESTAMP.equals(value)) {
      return false;
    }
    // Initialize extended attribute
    ExtendedDateColumnAttribute columnAttribute;
    if (currentColumn instanceof ExtendedDateColumnAttribute) {
      columnAttribute = (ExtendedDateColumnAttribute) currentColumn;
    } else {
      columnAttribute = new ExtendedDateColumnAttribute(currentColumn);
    }
    // Set attributes
    // Past
    if (SpecialCharacterToken.LESS.equals(operator.getOperator())) {
      // If future is already set
      if (Boolean.TRUE.equals(columnAttribute.getFuture()) || Boolean.TRUE.equals(columnAttribute.getFutureOrPresent())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, IN_FUTURE, IN_PAST);
      }
      setDateAttributesOnColumn(columnAttribute, true, false, false, false);
    }
    // Past or present
    else if (SpecialCharacterToken.LESS_EQUAL.equals(operator.getOperator())) {
      // Past is more specific validation - we skip this validation
      if (Boolean.TRUE.equals(columnAttribute.getPast())) {
        return true;
      }
      // If future is already set
      if (Boolean.TRUE.equals(columnAttribute.getFuture()) || Boolean.TRUE.equals(columnAttribute.getFutureOrPresent())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, IN_FUTURE, IN_PAST);
      }
      setDateAttributesOnColumn(columnAttribute, false, true, false, false);
    }
    // Future
    else if (SpecialCharacterToken.GREATER.equals(operator.getOperator())) {
      // Future or present is more specific validation - we skip this validation
      if (Boolean.TRUE.equals(columnAttribute.getFutureOrPresent())) {
        return true;
      }
      // If past is already set
      if (Boolean.TRUE.equals(columnAttribute.getPast()) || Boolean.TRUE.equals(columnAttribute.getPastOrPresent())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, IN_FUTURE, IN_PAST);
      }
      setDateAttributesOnColumn(columnAttribute, false, false, true, false);
    }
    // Future or present
    else if (SpecialCharacterToken.GREATER_EQUAL.equals(operator.getOperator())) {
      // If past is already set
      if (Boolean.TRUE.equals(columnAttribute.getPast()) || Boolean.TRUE.equals(columnAttribute.getPastOrPresent())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, IN_FUTURE, IN_PAST);
      }
      setDateAttributesOnColumn(columnAttribute, false, false, false, true);
    }
    // Unsupported operator
    else {
      return false;
    }

    // Save
    return saveAttributes(columnAttribute);
  }

  private boolean appendNullAttribute(Token value, Operator operator) throws IntermediateCodeException {
    value = inverseBooleanValue(value, operator.getOperator());
    if (Objects.isNull(value)) {
      return false;
    }
    if (KeywordToken.NULL.equals(value)) {
      if (Boolean.TRUE.equals(currentColumn.isNotNull())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, KeywordToken.NULL.toString(), NOT_NULL);
      }
      currentColumn.setNotNull(false);
    } else {
      if (Boolean.FALSE.equals(currentColumn.isNotNull())) {
        throw new ValidationMismatchIntermediateCodeException(currentColumn, KeywordToken.NULL.toString(), NOT_NULL);
      }
      currentColumn.setNotNull(true);
    }
    return saveAttributes(currentColumn);
  }

  private void setDateAttributesOnColumn(ExtendedDateColumnAttribute column,
                                         boolean past, boolean pastOrPresent,
                                         boolean future, boolean futureOrPresent) {
    column.setPast(past);
    column.setPastOrPresent(pastOrPresent);
    column.setFuture(future);
    column.setFutureOrPresent(futureOrPresent);
  }

  private boolean saveAttributes(ColumnAttribute columnAttribute) {
    currentTable.getColumns().put(currentColumn.getColumnName().getValue(), columnAttribute);
    return true;
  }

  public SemanticAttributeTable getSemanticAttributeTable() {
    return semanticAttributeTable;
  }
}
