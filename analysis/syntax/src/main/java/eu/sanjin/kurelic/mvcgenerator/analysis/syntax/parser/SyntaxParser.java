package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.DataTypeToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.LexicalSpecialCharacters;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.*;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.CreateTableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.TableDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.ColumnDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableConstraintDefinition;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElement;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.TableElementList;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.column.DataType;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.*;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.Expression;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operand.*;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.operator.Operator;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.BinaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.check.predicate.UnaryPredicate;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.create.table.element.constraint.reference.ReferenceAction;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class SyntaxParser {

  private final TokenSupplier token;
  private SyntaxTree syntaxTree;

  public SyntaxParser(TokenSupplier token) {
    this.token = token;
  }

  public SyntaxTree getSyntaxTree() {
    return syntaxTree;
  }

  public void parse() throws SyntaxException {
    syntaxTree = new SyntaxTree();
    token.clear();
    syntaxTree.setCreateDefinition(createDefinition());
  }

  // <CreateDefinition> ::= "CREATE" <CreateTableDefinition> ";" [ <CreateDefinition> ]
  private List<CreateDefinition> createDefinition() throws SyntaxException {
    List<CreateDefinition> createDefinitions = new ArrayList<>();
    token.equalsOrThrow(KeywordToken.CREATE).next(); // CREATE
    createDefinitions.add(createTableDefinition()); // <CreateTableDefinition>
    token.equalsOrThrow(SpecialCharacterToken.SEMICOLON).next(); // ;
    if (token.hasNext()) {
      createDefinitions.addAll(createDefinition()); // <CreateDefinition>
    }
    return createDefinitions;
  }

  // <CreateTableDefinition> ::= "TABLE" <TableDefinition>
  private CreateTableDefinition createTableDefinition() throws SyntaxException {
    token.equalsOrThrow(KeywordToken.TABLE).next(); // TABLE
    return new CreateTableDefinition(tableDefinition()); // <TableDefinition>
  }

  // <TableDefinition> ::= <TableName> "(" <TableElementList> ")"
  private TableDefinition tableDefinition() throws SyntaxException {
    TableDefinition tableDefinition = new TableDefinition();
    tableDefinition.setTableName(tableName()); // <TableName>
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // (
    tableDefinition.setTableElementList(tableElementList()); // <TableElementList>
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // )
    return tableDefinition;
  }

  // <TableElementList> ::= <TableElement> [ "," <TableElementList> ]
  private TableElementList tableElementList() throws SyntaxException {
    TableElementList tableElementList = new TableElementList();
    tableElementList.add(tableElement()); // <TableElement>
    if (token.equalsToken(SpecialCharacterToken.COMA)) {
      token.next(); // ,
      tableElementList.addAll(tableElementList()); // <TableElementList>
    }
    return tableElementList;
  }

  // <TableElement> ::= <ColumnDefinition> | <TableConstraintDefinition>
  private TableElement tableElement() throws SyntaxException {
    switch (token.getToken().getTokenType()) {
      case QUOTED_IDENTIFIER_VALUE:
      case IDENTIFIER:
        return columnDefinition(); // <ColumnDefinition>
      case KEYWORD:
        return tableConstraintDefinition(); // <TableConstraintDefinition>
      default:
        throw new UnexpectedTokenSyntaxException(token.getToken());
    }
  }

  // <ColumnDefinition> ::= <ColumnName> <DataType> [ <DefaultClause> ] [ <ColumnConstraintDefinitions> ] [ <CollateClause> ]
  // <ColumnConstraintDefinitions> ::= <ColumnConstraintDefinition> [ <ColumnConstraintDefinitions> ]
  private ColumnDefinition columnDefinition() throws SyntaxException {
    ColumnDefinition columnDefinition = new ColumnDefinition();
    columnDefinition.setColumnName(columnName()); // <ColumnName>
    columnDefinition.setDataType(dataType()); // <DataType>
    if (token.equalsToken(KeywordToken.DEFAULT)) {
      columnDefinition.setDefaultValue(defaultClause()); // <DefaultClause>
    }
    while (token.getToken().getTokenType().equals(TokenType.KEYWORD) && !token.equalsToken(KeywordToken.COLLATE)) {
      columnDefinition.getConstraintList().add(columnConstraintDefinition()); // <ColumnConstraintDefinitions>
    }
    if (token.equalsToken(KeywordToken.COLLATE)) {
      collateClause(); // <CollateClause>
    }
    return columnDefinition;
  }

  // <DataType> ::= <CharacterType> | <NationalCharacterType> | <BitType> | <NumericType> | <DatetimeType>
  private DataType dataType() throws SyntaxException {
    DataType dataType;
    String value = token.getToken().getValue();
    if (DataTypeToken.isCharacter(value)) {
      dataType = characterType();
    } else if (DataTypeToken.isNationalCharacter(value)) {
      dataType = nationalCharacterType();
    } else if (DataTypeToken.isBitType(value)) {
      dataType = bitType();
    } else if (DataTypeToken.isIntegerNumber(value) || DataTypeToken.isRealNumber(value)) {
      dataType = numericType();
    } else if (DataTypeToken.isDateTime(value)) {
      dataType = dateTimeType();
    } else {
      throw new InvalidDataTypeSyntaxException(token.getLineNumber());
    }
    return dataType;
  }

  // <CharacterType> ::= ("CHARACTER" | "CHAR" | "VARCHAR") [ <Length> ] [ "CHARACTER" "SET" <CharacterSetName> ]
  private DataType characterType() throws SyntaxException {
    DataType dataType = new DataType(token.getToken());
    token.next(); // value
    if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
      dataType.setPrecisionOrLength(length()); // <Length>
    }
    if (token.equalsToken(DataTypeToken.CHARACTER)) {
      token.next(); // "CHARACTER"
      token.equalsOrThrow(KeywordToken.SET).next(); // "SET"
      characterSetName(); // <CharacterSetName>
    }
    return dataType;
  }

  // <NationalCharacterType> ::= ("NCHAR" | "NVARCHAR") [ <Length> ]
  private DataType nationalCharacterType() throws SyntaxException {
    DataType dataType = new DataType(token.getToken());
    token.next(); // value
    if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
      dataType.setPrecisionOrLength(length());
    }
    return dataType;
  }

  // <BitType> ::= "BOOLEAN"
  private DataType bitType() throws SyntaxException {
    DataType dataType = new DataType(token.getToken());
    token.next();
    return dataType;
  }

  // <NumericType> ::=  <NumericTypeValue> | <NumericTypeValueWithPrecision> | <NumericTypeValueWithScale>
  // <NumericTypeValue> ::= "INTEGER" | "INT" | "SMALLINT" | "TINYINT" | "MEDIUMINT" | "BIGINT" | "REAL" | "DOUBLE"
  // <NumericTypeValueWithPrecision> ::= "FLOAT" [ <Precision> ]
  // <NumericTypeValueWithScale> ::= ( "NUMERIC" | "DECIMAL" | "DEC") [ <PrecisionWithScale> ]
  private DataType numericType() throws SyntaxException {
    DataType dataType = new DataType(token.getToken());
    if (token.equalsToken(DataTypeToken.FLOAT)) {
      token.next(); // value
      if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
        dataType.setPrecisionOrLength(precision()); // <Precision>
      }
    } else if (token.equalsToken(DataTypeToken.NUMERIC) || token.equalsToken(DataTypeToken.DECIMAL) || token.equalsToken(DataTypeToken.DEC)) {
      token.next(); // value
      if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
        Pair<Token, Token> precisionAndScale = precisionAndScale(); // <PrecisionWithScale>
        dataType.setPrecisionOrLength(precisionAndScale.getValue0());
        dataType.setScale(precisionAndScale.getValue1());
      }
    } else {
      token.next(); // value
    }
    return dataType;
  }

  // <DatetimeType> ::= "DATE" | ( "TIME" | "TIMESTAMP" | "DATETIME" ) [ <Precision> ] [ "WITH" "TIME" "ZONE" ]
  private DataType dateTimeType() throws SyntaxException {
    DataType dataType = new DataType(token.getToken());
    if (!token.equalsToken(DataTypeToken.DATE)) {
      token.next(); // value
      if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
        dataType.setPrecisionOrLength(precision()); // <Precision>
      }
      if (token.equalsToken(KeywordToken.WITH)) {
        token.next(); // "WITH"
        token.equalsOrThrow(DataTypeToken.TIME).next(); // "TIME"
        token.equalsOrThrow(KeywordToken.ZONE).next(); // "ZONE"
      }
    } else {
      token.next(); // value
    }
    return dataType;
  }

  // <Precision> ::= <Length>
  private Token precision() throws SyntaxException {
    return length();
  }

  // <Length> ::= "(" <UnsignedIntegerConstant> ")"
  private Token length() throws SyntaxException {
    Token value;
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    value = unsignedIntegerConstant(); // <UnsignedIntegerConstant>
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"
    return value;
  }

  // <PrecisionWithScale> ::= "(" <UnsignedIntegerConstant> [ "," <UnsignedIntegerConstant> ] ")"
  private Pair<Token, Token> precisionAndScale() throws SyntaxException {
    Token precision, scale = null;
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    precision = unsignedIntegerConstant(); // <UnsignedIntegerConstant>
    if (token.equalsToken(SpecialCharacterToken.COMA)) {
      token.next(); // ","
      scale = unsignedIntegerConstant(); // <UnsignedIntegerConstant>
    }
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next();
    return new Pair<>(precision, scale);
  }

  // <UnsignedIntegerConstant> ::= ( "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ) [ <UnsignedIntegerConstant> ]
  private Token unsignedIntegerConstant() throws SyntaxException {
    Token unsignedIntegerConstant = token.getToken();
    token.next(); // <UnsignedIntegerConstant>
    if (!unsignedIntegerConstant.getTokenType().equals(TokenType.CONSTANT_INTEGER_VALUE)) {
      throw new InvalidIntegerValueSyntaxException(token.getToken());
    }
    return unsignedIntegerConstant;
  }

  // <DefaultClause> ::= "DEFAULT" (Constant | "NULL" | "USER" | "CURRENT_USER" | "SESSION_USER" | "SYSTEM_USER")
  private Token defaultClause() throws SyntaxException {
    Token defaultValue;
    token.equalsOrThrow(KeywordToken.DEFAULT).next(); // "DEFAULT"
    defaultValue = token.getToken();
    switch (defaultValue.getTokenType()) {
      case CONSTANT_INTEGER_VALUE:
      case CONSTANT_QUOTED_VALUE:
      case CONSTANT_REAL_NUMBER_VALUE:
        break; // Constant is valid default value
      case KEYWORD:
        if (token.equalsToken(KeywordToken.NULL)
          || token.equalsToken(KeywordToken.USER)
          || token.equalsToken(KeywordToken.CURRENT_USER)
          || token.equalsToken(KeywordToken.SESSION_USER)
          || token.equalsToken(KeywordToken.SYSTEM_USER)) {
          break; // valid keywords as default value
        }
      default:
        throw new UnexpectedTokenSyntaxException(defaultValue);
    }
    token.next(); // run after switch
    return defaultValue;
  }

  // Not used in later processing
  // <CollateClause> ::= "COLLATE" <CollateName>
  private void collateClause() throws SyntaxException {
    token.equalsOrThrow(KeywordToken.COLLATE).next(); // "COLLATE"
    collateName(); // <CollateName>
  }

  // <ColumnConstraintDefinition> ::= [ <ConstraintNameDefinition> ] <ColumnConstraint> [ <ConstraintAttributes> ]
  private Constraint columnConstraintDefinition() throws SyntaxException {
    Constraint constraint;
    if (token.equalsToken(KeywordToken.CONSTRAINT)) {
      constraintNameDefinition(); // <ConstraintNameDefinition>
    }
    constraint = columnConstraint(); // <ColumnConstraint>
    if (token.equalsToken(KeywordToken.INITIALLY)
      || (token.equalsToken(KeywordToken.NOT) && token.hasNextToken(KeywordToken.DEFERRABLE))
      || token.equalsToken(KeywordToken.DEFERRABLE)) {
      constraintAttributes(); // <ConstraintAttributes>
    }
    return constraint;
  }

  // Not used in later processing
  // <ConstraintNameDefinition> ::= "CONSTRAINT" <ConstraintName>
  private void constraintNameDefinition() throws SyntaxException {
    token.equalsOrThrow(KeywordToken.CONSTRAINT).next(); // "CONSTRAINT"
    constraintName(); // <ConstraintName>
  }

  // Not used in later processing
  // <ConstraintAttributes> ::= <ConstraintCheckTime> [ [ "NOT" ] "DEFERRABLE" ] | [ "NOT" ] "DEFERRABLE" [ <ConstraintCheckTime> ]
  private void constraintAttributes() throws SyntaxException {
    if (token.equalsToken(KeywordToken.INITIALLY)) {
      constraintCheckTime();
      if (token.equalsToken(KeywordToken.NOT)) {
        token.next(); // "NOT"
      }
      if (token.equalsToken(KeywordToken.DEFERRABLE)) {
        token.next(); // "DEFERRABLE"
      }
    } else {
      if (token.equalsToken(KeywordToken.NOT)) {
        token.next(); // "NOT"
      }
      token.equalsOrThrow(KeywordToken.DEFERRABLE).next();
      if (token.equalsToken(KeywordToken.INITIALLY)) {
        constraintCheckTime();
      }
    }

  }

  // Not used in later processing
  // <ConstraintCheckTime> ::=  "INITIALLY" ( "DEFERRED" | "IMMEDIATE" )
  private void constraintCheckTime() throws SyntaxException {
    token.equalsOrThrow(KeywordToken.INITIALLY).next(); // "INITIALLY"
    if (token.equalsToken(KeywordToken.DEFERRED) || token.equalsToken(KeywordToken.IMMEDIATE)) {
      token.next(); // "DEFERRED" | "IMMEDIATE"
    } else {
      String expected = String.format("%s or %s", KeywordToken.DEFERRED, KeywordToken.IMMEDIATE);
      throw new UnexpectedTokenSyntaxException(token.getToken(), expected);
    }
  }

  // <ColumnConstraint> ::= "NOT" "NULL" | "UNIQUE" | "PRIMARY KEY" | <ReferencesClause> | <CheckClause>
  private Constraint columnConstraint() throws SyntaxException {
    if (token.equalsToken(KeywordToken.NOT)) {
      token.next(); // "NOT"
      token.equalsOrThrow(KeywordToken.NULL).next(); // "NULL"
      return new NotNullConstraint();
    }
    if (token.equalsToken(KeywordToken.UNIQUE)) {
      token.next(); // "UNIQUE"
      return new UniqueConstraint();
    }
    if (token.equalsToken(KeywordToken.PRIMARY)) {
      token.next(); // "PRIMARY"
      token.equalsOrThrow(KeywordToken.KEY).next();
      return new PrimaryKeyConstraint();
    }
    if (token.equalsToken(KeywordToken.REFERENCES)) {
      return referencesClause();
    }
    if (token.equalsToken(KeywordToken.CHECK)) {
      return checkClause();
    }
    throw new UnexpectedTokenSyntaxException(token.getToken());
  }

  // <TableConstraintDefinition> ::= [ <ConstraintNameDefinition> ] <TableConstraint> [ <ConstraintCheckTime> ]
  private TableConstraintDefinition tableConstraintDefinition() throws SyntaxException {
    TableConstraintDefinition tableConstraintDefinition;
    if (token.equalsToken(KeywordToken.CONSTRAINT)) {
      constraintNameDefinition(); // <ConstraintNameDefinition>
    }
    tableConstraintDefinition = tableConstraint(); // <ColumnConstraint>
    if (token.equalsToken(KeywordToken.INITIALLY)
      || (token.equalsToken(KeywordToken.NOT) && token.hasNextToken(KeywordToken.DEFERRABLE))
      || token.equalsToken(KeywordToken.DEFERRABLE)) {
      constraintAttributes(); // <ConstraintAttributes>
    }
    return tableConstraintDefinition;
  }

  // <TableConstraint> ::= <UniqueConstraintDefinition> | <ReferentialConstraintDefinition> | <CheckClause>
  // <UniqueConstraintDefinition> ::= ("UNIQUE" | "PRIMARY" "KEY") "(" <ColumnNameList> ")"
  // <ReferentialConstraintDefinition> ::= "FOREIGN" "KEY" "(" <ColumnNameList> ")" <ReferencesClause>
  private TableConstraintDefinition tableConstraint() throws SyntaxException {
    TableConstraintDefinition tableConstraintDefinition = new TableConstraintDefinition();
    boolean references = false;
    if (token.equalsToken(KeywordToken.UNIQUE)) {
      token.next(); // "UNIQUE"
      tableConstraintDefinition.setConstraintType(new UniqueConstraint());
    } else if (token.equalsToken(KeywordToken.PRIMARY)) {
      token.next(); // "PRIMARY"
      token.equalsOrThrow(KeywordToken.KEY).next(); // "KEY"
      tableConstraintDefinition.setConstraintType(new PrimaryKeyConstraint());
    } else if (token.equalsToken(KeywordToken.FOREIGN)) {
      token.next(); // "FOREIGN"
      token.equalsOrThrow(KeywordToken.KEY).next(); // "KEY"
      references = true;
    } else if (token.equalsToken(KeywordToken.CHECK)) {
      tableConstraintDefinition.setConstraintType(checkClause());
      return tableConstraintDefinition;
    }
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    tableConstraintDefinition.setColumnList(columnNameList()); // <ColumnNameList>
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"

    if (references) {
      ReferenceConstraint referenceConstraint = referencesClause();
      tableConstraintDefinition.setConstraintType(referenceConstraint);
      // Check column count
      if (tableConstraintDefinition.getColumnList().size() != referenceConstraint.getColumnList().size()) {
        throw new InvalidReferenceColumnCountSyntaxException(token.getLineNumber());
      }
    }
    return tableConstraintDefinition;
  }

  // <ReferencesClause> ::= "REFERENCES" <TableName> [ "(" <ColumnNameList> ")" ] [ "MATCH" ("FULL" | "PARTIAL") ] [ <ReferentialTriggeredAction> ]
  private ReferenceConstraint referencesClause() throws SyntaxException {
    ReferenceConstraint referenceConstraint = new ReferenceConstraint();
    token.equalsOrThrow(KeywordToken.REFERENCES).next(); // "REFERENCES"
    referenceConstraint.setTableName(tableName()); // <TableName>
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    referenceConstraint.setColumnList(columnNameList()); // <ColumnNameList>
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"
    if (token.equalsToken(KeywordToken.MATCH)) {
      token.next(); // "MATCH"
      if (token.equalsToken(KeywordToken.FULL) || token.equalsToken(KeywordToken.PARTIAL)) {
        token.next(); // "FULL" | "PARTIAL"
      } else {
        String expected = String.format("%s or %s", KeywordToken.FULL, KeywordToken.PARTIAL);
        throw new UnexpectedTokenSyntaxException(token.getToken(), expected);
      }
    }
    if (token.equalsToken(KeywordToken.ON)) {
      Pair<ReferenceAction, ReferenceAction> actions = referentialTriggeredAction(); // <ReferentialTriggeredAction>
      referenceConstraint.setUpdateAction(actions.getValue0());
      referenceConstraint.setDeleteAction(actions.getValue1());
    }
    return referenceConstraint;
  }

  /**
   * <ReferentialTriggeredAction> ::= <UpdateRule> [ <DeleteRule> ] | <DeleteRule> [ <UpdateRule> ]
   * <UpdateRule> ::= "ON" "UPDATE" <ReferentialAction>
   * <DeleteRule> ::= "ON" "DELETE" <ReferentialAction>
   *
   * @return Pair - first item is Update trigger second is Delete trigger
   * @throws SyntaxException - for unexpected token
   */
  private Pair<ReferenceAction, ReferenceAction> referentialTriggeredAction() throws SyntaxException {
    Pair<ReferenceAction, ReferenceAction> actions = Pair.with(ReferenceAction.NO_ACTION, ReferenceAction.NO_ACTION);
    token.equalsOrThrow(KeywordToken.ON).next(); // "ON"
    if (token.equalsToken(KeywordToken.UPDATE)) {
      token.next(); // "UPDATE"
      actions = actions.setAt0(referentialAction()); // <ReferentialAction>
      if (token.equalsToken(KeywordToken.DELETE)) {
        token.next(); // "DELETE"
        actions = actions.setAt1(referentialAction()); // <ReferentialAction>
      }
    } else if (token.equalsToken(KeywordToken.DELETE)) {
      token.next(); // "DELETE"
      actions = actions.setAt1(referentialAction()); // <ReferentialAction>
      if (token.equalsToken(KeywordToken.UPDATE)) {
        token.next(); // "UPDATE"
        actions = actions.setAt0(referentialAction()); // <ReferentialAction>
      }
    } else {
      String expected = String.format("%s or %s", KeywordToken.UPDATE, KeywordToken.DELETE);
      throw new UnexpectedTokenSyntaxException(token.getToken(), expected);
    }
    return actions;
  }

  // <ReferentialAction> ::= "CASCADE" | "SET NULL" | "SET DEFAULT" | "NO ACTION"
  private ReferenceAction referentialAction() throws SyntaxException {
    if (token.equalsToken(KeywordToken.CASCADE)) {
      token.next(); // "CASCADE"
      return ReferenceAction.CASCADE;
    }
    if (token.equalsToken(KeywordToken.SET)) {
      token.next(); // "SET"
      if (token.equalsToken(KeywordToken.NULL)) {
        token.next(); // "NULL"
        return ReferenceAction.SET_NULL;
      }
      if (token.equalsToken(KeywordToken.DEFAULT)) {
        token.next(); // "DEFAULT"
        return ReferenceAction.SET_DEFAULT;
      }
      String expected = String.format("%s or %s", KeywordToken.NULL, KeywordToken.DEFAULT);
      throw new UnexpectedTokenSyntaxException(token.getToken(), expected);
    }
    if (token.equalsToken(KeywordToken.NO)) {
      token.next(); // "NO"
      token.equalsOrThrow(KeywordToken.ACTION).next(); // "ACTION"
      return ReferenceAction.NO_ACTION;
    }
    String expected = String.format("%s, %s %s, %s %s or %s %s", KeywordToken.CASCADE, KeywordToken.SET,
      KeywordToken.NULL, KeywordToken.SET, KeywordToken.DEFAULT, KeywordToken.NO, KeywordToken.ACTION);
    throw new UnexpectedTokenSyntaxException(token.getToken(), expected);
  }

  // <CheckClause> ::= "CHECK" "(" <SearchCondition> ")"
  private CheckConstraint checkClause() throws SyntaxException {
    CheckConstraint checkConstraint = new CheckConstraint();

    token.equalsOrThrow(KeywordToken.CHECK).next();
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    checkConstraint.setCheckExpression(searchCondition()); // <SearchCondition>
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"

    return checkConstraint;
  }

  // <SearchCondition> ::= <BooleanFactor> [ ("OR" | "AND") <SearchCondition> ]
  private Expression searchCondition() throws SyntaxException {
    Expression expression = booleanFactor(); // <BooleanFactor>
    if (token.equalsToken(KeywordToken.OR) || token.equalsToken(KeywordToken.AND)) {
      BinaryPredicate combineNode = new BinaryPredicate();
      Operator operator = new Operator();

      // operator
      operator.setLineNumber(token.getLineNumber());
      if (token.equalsToken(KeywordToken.AND)) {
        operator.setOperator(SpecialCharacterToken.AND);
      } else if (token.equalsToken(KeywordToken.OR)) {
        operator.setOperator(SpecialCharacterToken.OR);
      }
      token.next(); // OR | AND
      combineNode.setOperator(operator);

      // expressions
      combineNode.setFirstExpression(expression);
      combineNode.setSecondExpression(searchCondition()); // <SearchCondition>
      return combineNode;
    }
    return expression;
  }

  // <BooleanFactor> ::= [ "NOT" ] <BooleanTest>
  private Expression booleanFactor() throws SyntaxException {
    if (token.equalsToken(KeywordToken.NOT)) {
      UnaryPredicate notNode = new UnaryPredicate();
      notNode.setOperator(new Operator(SpecialCharacterToken.NOT, token.getLineNumber()));
      token.next(); // "NOT"
      notNode.setExpression(booleanTest()); // <BooleanTest>
      return notNode;
    }
    return booleanTest();
  }

  // <BooleanTest> ::= <BooleanPrimary> [ "IS"  [ "NOT" ] ( "TRUE" | "FALSE" | "UNKNOWN") ]
  private Expression booleanTest() throws SyntaxException {
    Expression booleanPrimary = booleanPrimary();
    if (token.equalsToken(KeywordToken.IS)) {
      BinaryPredicate testNode = new BinaryPredicate();
      testNode.setFirstExpression(booleanPrimary);

      // Operator
      Operator operator = new Operator(SpecialCharacterToken.EQUAL, token.getLineNumber());
      token.next(); // "IS"
      if (token.equalsToken(KeywordToken.NOT)) {
        token.next(); // "NOT"
        operator.setOperator(SpecialCharacterToken.NOT_EQUAL);
      }

      // Second expression
      if (token.equalsToken(KeywordToken.TRUE) || token.equalsToken(KeywordToken.FALSE)
        || token.equalsToken(KeywordToken.UNKNOWN)) {
        testNode.setSecondExpression(new KeywordOperand(token.getToken()));
        token.next(); // "TRUE" | "FALSE" | "UNKNOWN"
      }
      return testNode;
    }
    return booleanPrimary;
  }

  // <BooleanPrimary> ::= <Predicate> | "(" <SearchCondition> ")"
  private Expression booleanPrimary() throws SyntaxException {
    Expression expression;
    if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
      token.next(); // "("
      expression = searchCondition(); // <SearchCondition>
      token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"
    } else {
      expression = predicate(); // <Predicate>
    }
    return expression;
  }

  // <Predicate> ::= <RowValue> ( <ComparisonPredicate> | <BetweenPredicate> | <InPredicate> | <LikePredicate> | <NullPredicate> | <OverlapsPredicate> )
  private Expression predicate() throws SyntaxException {
    Expression rowValue = rowValue(); // <RowValue>
    Token notToken = null;

    if (token.equalsToken(KeywordToken.NOT)) {
      notToken = token.getToken();
      token.next(); // "NOT"
    }

    if (SpecialCharacterToken.isRational(token.getValue())) {
      return comparisonPredicate(rowValue, notToken); // <ComparisonPredicate>
    }
    if (token.equalsToken(KeywordToken.BETWEEN)) {
      return betweenPredicate(rowValue, notToken); // <BetweenPredicate>
    }
    if (token.equalsToken(KeywordToken.IN)) {
      return inPredicate(rowValue, notToken); // <InPredicate>
    }
    if (token.equalsToken(KeywordToken.LIKE)) {
      return likePredicate(rowValue, notToken); // <LikePredicate>
    }
    if (token.equalsToken(KeywordToken.IS)) {
      return nullPredicate(rowValue, notToken); // <NullPredicate>
    }
    if (token.equalsToken(KeywordToken.OVERLAPS)) {
      return overlapsPredicate(rowValue, notToken); // <OverlapsPredicate>
    }
    throw new UnexpectedTokenSyntaxException(token.getToken());
  }

  // <ComparisonPredicate> ::= <RowValue> <RationalOperator> <RowValue>
  private Expression comparisonPredicate(Expression rowValue, Token notToken) throws SyntaxException {
    if (notToken != null) {
      throw new UnexpectedTokenSyntaxException(notToken);
    }
    BinaryPredicate comparisonPredicate = new BinaryPredicate();
    comparisonPredicate.setFirstExpression(rowValue); // <RowValue>

    // Operator
    SpecialCharacterToken specialCharacterToken = SpecialCharacterToken.toSpecialCharacterToken(token.getValue());
    if (specialCharacterToken == null) {
      throw new UnexpectedTokenSyntaxException(token.getToken());
    }
    Operator rationalOperator = new Operator(specialCharacterToken, token.getLineNumber());
    token.next(); // <RationalOperator>
    comparisonPredicate.setOperator(rationalOperator);

    comparisonPredicate.setSecondExpression(rowValue()); // <RowValue>
    return comparisonPredicate;
  }

  // <BetweenPredicate> ::= <RowValue> [ "NOT" ] "BETWEEN" <RowValue> "AND" <RowValue>
  //
  // Between (x BETWEEN 1 AND 5) => (x >= 1 AND x <= 5)
  // Not between (x NOT BETWEEN 1 AND 5) => (x < 1 OR x > 5)
  private Expression betweenPredicate(Expression rowValue, Token notToken) throws SyntaxException {
    int lineNumber = token.getLineNumber();
    boolean not = notToken != null; // [ "NOT" ]
    token.equalsOrThrow(KeywordToken.BETWEEN).next(); // "BETWEEN"
    Expression beginLimit = rowValue(); // <RowValue>
    token.equalsOrThrow(KeywordToken.AND).next(); // "AND"
    Expression endLimit = rowValue(); // <RowValue>

    // Build predicates
    BinaryPredicate beginPredicate = new BinaryPredicate();
    beginPredicate.setFirstExpression(rowValue);
    beginPredicate.setSecondExpression(beginLimit);
    Operator beginOperator = new Operator();
    beginOperator.setLineNumber(lineNumber);
    beginOperator.setOperator((not ? SpecialCharacterToken.LESS : SpecialCharacterToken.GREATER_EQUAL));
    beginPredicate.setOperator(beginOperator);

    BinaryPredicate endPredicate = new BinaryPredicate();
    endPredicate.setFirstExpression(rowValue);
    endPredicate.setSecondExpression(endLimit);
    Operator endOperator = new Operator();
    endOperator.setLineNumber(lineNumber);
    endOperator.setOperator((not ? SpecialCharacterToken.GREATER : SpecialCharacterToken.LESS_EQUAL));
    endPredicate.setOperator(endOperator);

    BinaryPredicate betweenPredicate = new BinaryPredicate();
    betweenPredicate.setFirstExpression(beginPredicate);
    betweenPredicate.setSecondExpression(endPredicate);
    Operator betweenOperator = new Operator();
    betweenOperator.setLineNumber(lineNumber);
    betweenOperator.setOperator((not ? SpecialCharacterToken.OR : SpecialCharacterToken.AND));
    betweenPredicate.setOperator(betweenOperator);

    return betweenPredicate;
  }

  // <InPredicate> ::= <RowValue> [ "NOT" ] "IN" "(" <InPredicateValue> ")"
  //
  // In (x IN (1,2,3)) => (x = 1 OR x = 2 OR x = 3)
  // Not in (x NOT IN (1,2,3)) => (x != 1 AND x != 2 AND x != 3)
  private Expression inPredicate(Expression rowValue, Token notToken) throws SyntaxException {
    // Combine operator
    Operator combineOperator = new Operator();
    combineOperator.setLineNumber(token.getLineNumber());
    combineOperator.setOperator((notToken == null ? SpecialCharacterToken.OR : SpecialCharacterToken.AND));
    // Equality operator
    Operator equalityOperator = new Operator();
    equalityOperator.setLineNumber(token.getLineNumber());
    equalityOperator.setOperator((notToken == null ? SpecialCharacterToken.EQUAL : SpecialCharacterToken.NOT_EQUAL));
    // Build equality predicates
    token.equalsOrThrow(KeywordToken.IN).next(); // "IN"
    Stack<BinaryPredicate> equalities = new Stack<>();
    BinaryPredicate equalityPredicate;
    for (Expression value : inPredicateValue()) { // <InPredicateValue>
      equalityPredicate = new BinaryPredicate();
      equalityPredicate.setFirstExpression(rowValue);
      equalityPredicate.setOperator(equalityOperator);
      equalityPredicate.setSecondExpression(value);
      equalities.push(equalityPredicate);
    }
    // Build combine operators
    if (equalities.size() == 1) {
      return equalities.pop();
    }
    Expression secondEquality;
    Expression firstEquality = equalities.pop();
    BinaryPredicate combinedExpression = null;
    while (!equalities.isEmpty()) {
      combinedExpression = new BinaryPredicate();
      secondEquality = equalities.pop();

      combinedExpression.setFirstExpression(firstEquality);
      combinedExpression.setOperator(combineOperator);
      combinedExpression.setSecondExpression(secondEquality);

      firstEquality = combinedExpression;
    }
    if (Objects.isNull(combinedExpression)) {
      throw new UnexpectedTokenSyntaxException(token.getToken()); // should not happen
    }
    return combinedExpression;
  }

  // <InPredicateValue> ::= <Expression> [ "," <InPredicateValue> ]
  private List<Expression> inPredicateValue() throws SyntaxException {
    List<Expression> expressions = new ArrayList<>();
    expressions.add(expression()); // <Expression>
    if (token.equalsToken(SpecialCharacterToken.COMA)) {
      token.next(); // ","
      expressions.addAll(inPredicateValue()); // <InPredicateValue>
    }
    return expressions;
  }

  // <LikePredicate> ::= <RowValue> [ "NOT" ] "LIKE" <QuotedString> [ "ESCAPE" <QuotedString> ]
  private Expression likePredicate(Expression rowValue, Token notToken) throws SyntaxException {
    BinaryPredicate likePredicate = new BinaryPredicate();
    likePredicate.setFirstExpression(rowValue);
    // Operator
    Operator likeOperator = new Operator(); // <RowValue>
    likeOperator.setOperator((notToken == null ? SpecialCharacterToken.LIKE : SpecialCharacterToken.NOT_LIKE)); // [ "NOT" ]
    likeOperator.setLineNumber(token.getLineNumber());
    token.equalsOrThrow(KeywordToken.LIKE).next(); // "LIKE"
    likePredicate.setOperator(likeOperator);
    // Second parameter
    Token quotedString = token.getToken();
    token.next(); // <QuotedString>
    if (quotedString.getTokenType() != TokenType.CONSTANT_QUOTED_VALUE) {
      throw new UnexpectedTokenSyntaxException(token.getToken());
    }
    if (token.equalsToken(KeywordToken.ESCAPE)) {
      token.next(); // "ESCAPE"
      if (token.getToken().getTokenType() != TokenType.CONSTANT_QUOTED_VALUE || token.getValue().length() != 1) {
        throw new UnexpectedTokenSyntaxException(token.getToken(), "1 quoted escape character");
      }
      // transform escape character to standard escape character '\'
      if (!token.getValue().equals("\\")) {
        String escapeCharacter = token.getValue();
        String quotedStringValue = quotedString.getValue();
        // if we have standard escape character '\' in quoted string, escape it with standard escape character '\\'
        quotedStringValue = quotedStringValue.replaceAll("\\\\", "\\\\\\\\");
        // set '\' as new escape character
        quotedStringValue = quotedStringValue.replaceAll(escapeCharacter, "\\\\");
        quotedString.setValue(quotedStringValue);
      }
      token.next(); // <QuotedString>
    }
    likePredicate.setSecondExpression(new ConstantOperand(quotedString));
    return likePredicate;
  }

  // <NullPredicate> ::= <RowValue> "IS" [ "NOT" ] "NULL"
  private Expression nullPredicate(Expression rowValue, Token notToken) throws SyntaxException {
    if (notToken != null) {
      throw new UnexpectedTokenSyntaxException(notToken);
    }
    BinaryPredicate nullPredicate = new BinaryPredicate();
    nullPredicate.setFirstExpression(rowValue); // <RowValue>

    // Operator
    Operator operator = new Operator(SpecialCharacterToken.EQUAL, token.getLineNumber());
    token.equalsOrThrow(KeywordToken.IS).next(); // "IS"
    if (token.equalsToken(KeywordToken.NOT)) {
      operator.setOperator(SpecialCharacterToken.NOT_EQUAL);
      token.next(); // "NOT"
    }
    nullPredicate.setOperator(operator);
    // Null operand
    token.equalsOrThrow(KeywordToken.NULL);
    nullPredicate.setSecondExpression(new KeywordOperand(token.getToken()));
    token.next(); // "NULL"
    return nullPredicate;
  }

  // <OverlapsPredicate> ::= <RowValue> "OVERLAPS" <RowValue>
  private Expression overlapsPredicate(Expression rowValue, Token notToken) throws SyntaxException {
    if (notToken != null) {
      throw new UnexpectedTokenSyntaxException(notToken);
    }
    BinaryPredicate overlapsPredicate = new BinaryPredicate();
    overlapsPredicate.setFirstExpression(rowValue); // <RowValue>

    // Operator
    overlapsPredicate.setOperator(new Operator(SpecialCharacterToken.OVERLAPS, token.getLineNumber()));
    token.equalsOrThrow(KeywordToken.OVERLAPS).next(); // "OVERLAPS"

    overlapsPredicate.setSecondExpression(rowValue()); // <RowValue>
    return overlapsPredicate;
  }

  // <RowValue> ::= <RowValueElement> | "(" <RowValue> ")"
  private Expression rowValue() throws SyntaxException {
    Expression rowValue;
    if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
      token.next(); // "("
      rowValue = rowValue(); // <RowValue>
      token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"
    } else {
      rowValue = rowValueElement(); // <RowValueElement>
    }
    return rowValue;
  }

  // <RowValueElement> ::= <Expression> | "NULL" | "DEFAULT"
  private Expression rowValueElement() throws SyntaxException {
    if (token.equalsToken(KeywordToken.NULL) | token.equalsToken(KeywordToken.DEFAULT)) {
      Token keyword = token.getToken();
      token.next(); // "NULL" | "DEFAULT"
      return new KeywordOperand(keyword);
    }
    return expression(); // <Expression>
  }

  // <Expression> ::= <Term> [ ( "+" | "-" | "||" ) <Expression> ]
  private Expression expression() throws SyntaxException {
    BinaryPredicate expression = new BinaryPredicate();
    expression.setFirstExpression(term()); // <Term>
    if (token.equalsToken(SpecialCharacterToken.PLUS)) {
      expression.setOperator(new Operator(SpecialCharacterToken.PLUS, token.getLineNumber()));
    } else if (token.equalsToken(SpecialCharacterToken.MINUS)) {
      expression.setOperator(new Operator(SpecialCharacterToken.MINUS, token.getLineNumber()));
    } else if (token.equalsToken(SpecialCharacterToken.CONCAT)) {
      expression.setOperator(new Operator(SpecialCharacterToken.CONCAT, token.getLineNumber()));
    } else {
      return expression.getFirstExpression(); // return term only
    }
    token.next(); // "+" | "-" | "||"
    expression.setSecondExpression(expression());

    return expression;
  }

  // <Term> ::= <Factor> [ <BinaryOperator> <Term> ]
  private Expression term() throws SyntaxException {
    Expression factor = factor(); // <Factor>
    if (SpecialCharacterToken.isBinary(token.getToken().getValue())) {
      BinaryPredicate term = new BinaryPredicate();
      term.setFirstExpression(factor);
      // Operator
      SpecialCharacterToken specialCharacterToken = SpecialCharacterToken.toSpecialCharacterToken(token.getValue());
      if (Objects.isNull(specialCharacterToken)) {
        throw new UnexpectedTokenSyntaxException(token.getToken());
      }
      term.setOperator(new Operator(specialCharacterToken, token.getLineNumber()));
      token.next(); // <BinaryOperator>
      term.setSecondExpression(term()); // <Term>
      return term;
    }
    return factor;
  }

  // <Factor> ::= [ ( "+" | "-" ) ] <Value> [ ( <TimeZone> | <IntervalQualifier> ) ]
  private Expression factor() throws SyntaxException {
    Expression factor;
    if (token.equalsToken(SpecialCharacterToken.MINUS)) {
      factor = new UnaryPredicate();
      // Operator
      ((UnaryPredicate) factor).setOperator(new Operator(SpecialCharacterToken.MINUS, token.getLineNumber()));
      token.next(); // "-"
      // Expression
      ((UnaryPredicate) factor).setExpression(value());
    } else {
      if (token.equalsToken(SpecialCharacterToken.PLUS)) {
        token.next(); // "+"
      }
      factor = value(); // <Value>
    }
    if (token.equalsToken(KeywordToken.AT) || isIntervalStartField()) {
      BinaryPredicate castPredicate = new BinaryPredicate();
      castPredicate.setFirstExpression(factor);
      castPredicate.setOperator(new Operator(SpecialCharacterToken.CAST, token.getLineNumber()));

      if (token.equalsToken(KeywordToken.AT)) {
        castPredicate.setSecondExpression(timeZone()); // <TimeZone>
      } else {
        castPredicate.setSecondExpression(intervalQualifier()); // <IntervalQualifier>
      }
      return castPredicate;
    }
    return factor;
  }

  // <TimeZone> ::= "AT" ( "LOCAL" | ( "TIME" "ZONE" <Value> ) )
  private Operand timeZone() throws SyntaxException {
    ZonedDataTypeOperand zonedDataTypeOperand = new ZonedDataTypeOperand(token.getLineNumber());
    token.equalsOrThrow(KeywordToken.AT).next(); // "AT"
    if (token.equalsToken(KeywordToken.LOCAL)) {
      zonedDataTypeOperand.setZone(new KeywordOperand(token.getToken())); // "LOCAL"
    } else {
      token.equalsOrThrow(DataTypeToken.TIME).next(); // "TIME"
      token.equalsOrThrow(KeywordToken.ZONE).next(); // "ZONE"
      zonedDataTypeOperand.setZone(value());
    }
    return zonedDataTypeOperand;
  }

  // <IntervalQualifier> ::= <StartField> "TO" <EndField>
  // <StartField> ::= ( "YEAR" | "MONTH" | "DAY" | "HOUR" | "MINUTE" ) [ <Precision> ]
  // <EndField> ::= ( "SECOND" [ <Precision> ] ) | <StartField>
  private Operand intervalQualifier() throws SyntaxException {
    IntervalDataTypeOperand intervalDataTypeOperand = new IntervalDataTypeOperand(token.getLineNumber());
    if (!isIntervalStartField()) {
      throw new InvalidIntervalTypeSyntaxException(token.getLineNumber());
    }
    intervalDataTypeOperand.setFrom(token.getToken());
    token.next(); // <StartField>
    token.equalsOrThrow(KeywordToken.TO).next(); // "TO"
    if (!token.equalsToken(KeywordToken.SECOND) && !isIntervalStartField()) {
      throw new InvalidIntervalTypeSyntaxException(token.getLineNumber());
    }
    intervalDataTypeOperand.setTo(token.getToken());
    token.next(); // <EndField>

    return intervalDataTypeOperand;
  }

  private boolean isIntervalStartField() throws SyntaxException {
    return token.equalsToken(KeywordToken.YEAR) || token.equalsToken(KeywordToken.MONTH)
      || token.equalsToken(KeywordToken.DAY) || token.equalsToken(KeywordToken.HOUR)
      || token.equalsToken(KeywordToken.MINUTE);
  }

  // <Value> ::= <ColumnName> | <NumericValue> | <QuotedString> | "(" <ValueExpression> ")" | <UserValue> | <DateTimeValue> | <CastSpecification>
  // <UserValue> ::= "USER" | "CURRENT_USER" | "SESSION_USER" | "SYSTEM_USER"
  // <DateTimeValue> ::= "CURRENT_DATE" | "CURRENT_TIME" [ <Precision> ] | "CURRENT_TIMESTAMP" [ <Precision> ]
  private Expression value() throws SyntaxException {
    Expression value;
    if (token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
      token.next(); // "("
      value = expression(); // <Expression>
      token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"
    } else if (token.equalsToken(KeywordToken.USER) || token.equalsToken(KeywordToken.CURRENT_USER)
      || token.equalsToken(KeywordToken.SESSION_USER) || token.equalsToken(KeywordToken.SYSTEM_USER)) {
      value = new KeywordOperand(token.getToken());
      token.next(); // "USER" | "CURRENT_USER" | "SESSION_USER" | "SYSTEM_USER"
    } else if (token.equalsToken(KeywordToken.CURRENT_DATE) || token.equalsToken(KeywordToken.CURRENT_TIME)
      || token.equalsToken(KeywordToken.CURRENT_TIMESTAMP)) {
      value = new KeywordOperand(token.getToken());
      token.next(); // "CURRENT_DATE" | "CURRENT_TIME" | "CURRENT_TIMESTAMP"
      if (!token.equalsToken(KeywordToken.CURRENT_DATE) && token.equalsToken(SpecialCharacterToken.LEFT_BRACKET)) {
        precision(); // [ <Precision> ]
      }
    } else if (token.getToken().getTokenType() == TokenType.CONSTANT_INTEGER_VALUE
      || token.getToken().getTokenType() == TokenType.CONSTANT_REAL_NUMBER_VALUE
      || token.getToken().getTokenType() == TokenType.CONSTANT_QUOTED_VALUE) {
      value = new ConstantOperand(token.getToken());
      token.next(); // <NumericConstant>
    } else if (token.equalsToken(KeywordToken.CAST)) {
      value = castSpecification();
    } else {
      value = new ColumnOperand(columnName()); // <ColumnName>
    }
    return value;
  }

  // <CastSpecification> ::= "CAST" "(" <CastOperand> "AS" <CastTarget> ")"
  // <CastOperand> ::= <Expression> | "NULL"
  // <CastTarget> ::= <DataType> | <ColumnName>
  private BinaryPredicate castSpecification() throws SyntaxException {
    BinaryPredicate binaryPredicate = new BinaryPredicate();
    binaryPredicate.setOperator(new Operator(SpecialCharacterToken.CAST, token.getLineNumber()));

    token.equalsOrThrow(KeywordToken.CAST).next(); // "CAST"
    token.equalsOrThrow(SpecialCharacterToken.LEFT_BRACKET).next(); // "("
    // <CastOperand>
    if (token.equalsToken(KeywordToken.NULL)) {
      binaryPredicate.setFirstExpression(new KeywordOperand(token.getToken()));
      token.next(); // "NULL"
    } else {
      binaryPredicate.setFirstExpression(expression()); // <Expression>
    }
    token.equalsOrThrow(KeywordToken.AS).next(); // "AS"
    // <CastTarget>
    if (token.getToken().getTokenType() == TokenType.DATA_TYPE) {
      binaryPredicate.setSecondExpression(new DataTypeOperand(dataType())); // <DataType>
    } else {
      binaryPredicate.setSecondExpression(new ColumnOperand(columnName())); // <ColumnName>
    }
    token.equalsOrThrow(SpecialCharacterToken.RIGHT_BRACKET).next(); // ")"

    return binaryPredicate;
  }

  // <ColumnNameList> ::= <columnName> [ "," <columnNameList> ]
  private Tokens columnNameList() throws SyntaxException {
    Tokens columns = new Tokens();
    columns.add(columnName()); // <columnName>
    if (token.equalsToken(SpecialCharacterToken.COMA)) {
      token.next(); // ","
      columns.addAll(columnNameList());
    }
    return columns;
  }

  // <ColumnName> ::= <Identifier>
  private Token columnName() throws SyntaxException {
    return identifier();
  }

  // <TableName> ::= <QualifiedName>
  private Token tableName() throws SyntaxException {
    return qualifiedName();
  }

  // <CharacterSetName> ::= <QualifiedName>
  @SuppressWarnings("UnusedReturnValue")
  private Token characterSetName() throws SyntaxException {
    return qualifiedName();
  }

  // <CollateName> ::= <QualifiedName>
  @SuppressWarnings("UnusedReturnValue")
  private Token collateName() throws SyntaxException {
    return qualifiedName();
  }

  // <ConstraintName> ::= <QualifiedName>
  @SuppressWarnings("UnusedReturnValue")
  private Token constraintName() throws SyntaxException {
    return qualifiedName();
  }

  // <QualifiedName> ::= <Identifier> [ "." <Identifier> [ "." <Identifier> ] ]
  private Token qualifiedName() throws SyntaxException {
    Token identifier = identifier(); // <Identifier>
    if (token.equalsToken(SpecialCharacterToken.SCHEMA_TABLE_DELIMITER)) {
      token.next(); // "."
      identifier = identifier(); // override - only last one matters
      if (token.equalsToken(SpecialCharacterToken.SCHEMA_TABLE_DELIMITER)) {
        token.next(); // "."
        identifier = identifier(); // override - only last one matters
      }
    }
    return identifier;
  }

  // Allowed characters: a -> z, A -> Z, 0 -> 9, _, (space)
  private Token identifier() throws SyntaxException {
    Token identifier = token.getToken();
    token.next(); // <Identifier>
    if (identifier.getTokenType() != TokenType.IDENTIFIER && identifier.getTokenType() != TokenType.QUOTED_IDENTIFIER_VALUE) {
      throw new InvalidIdentifierSyntaxException(identifier.getLineNumber(), InvalidIdentifierSyntaxException.ErrorType.RESERVED_WORD);
    }
    // Check identifier value
    String identifierValue = identifier.getValue();
    boolean firstLetter = true;
    for (char character : identifierValue.toCharArray()) {
      if (firstLetter && !Character.isLetter(character)) {
        throw new InvalidIdentifierSyntaxException(identifier.getLineNumber(), InvalidIdentifierSyntaxException.ErrorType.INVALID_FIRST_CHARACTER);
      }
      if (!firstLetter && !(Character.isLetterOrDigit(character)
        || character == LexicalSpecialCharacters.UNDERSCORE_DELIMITER.toChar()
        || character == LexicalSpecialCharacters.SPACE_DELIMITER.toChar())) {
        throw new InvalidIdentifierSyntaxException(identifier.getLineNumber(), InvalidIdentifierSyntaxException.ErrorType.INVALID_CHARACTER);
      }
      firstLetter = false;
    }
    return identifier;
  }

}
