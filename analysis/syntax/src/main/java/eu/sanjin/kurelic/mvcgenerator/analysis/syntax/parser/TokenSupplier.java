package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.SyntaxException;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.UnexpectedEndOfFileSyntaxException;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.UnexpectedTokenSyntaxException;

public class TokenSupplier {

  private Tokens tokens;
  private int index;
  private int size;

  public TokenSupplier(Tokens tokens) {
    this.tokens = tokens;
    this.size = tokens.size();
    clear();
  }

  public void clear() {
    index = 0;
  }

  public TokenSupplier equalsOrThrow(Enum<?> token) throws SyntaxException {
    String value = getValue().toLowerCase();
    if (!value.equals(token.toString())) {
      throw new UnexpectedTokenSyntaxException(getToken(), token.toString());
    }
    return this;
  }

  public boolean equalsToken(Enum<?> token) throws SyntaxException {
    return getValue().toLowerCase().equals(token.toString());
  }

  public boolean hasNextToken(Enum<?> token) throws SyntaxException {
    if (!hasNext()) {
      return false;
    }
    return getValue(index + 1).toLowerCase().equals(token.toString());
  }

  private String getValue(int index) throws SyntaxException {
    try {
      return tokens.get(index).getValue();
    } catch (IndexOutOfBoundsException e) {
      throw new UnexpectedEndOfFileSyntaxException();
    }
  }

  public String getValue() throws SyntaxException {
    return getValue(index);
  }

  public Token getToken() throws SyntaxException {
    try {
      return tokens.get(index);
    } catch (IndexOutOfBoundsException e) {
      throw new UnexpectedEndOfFileSyntaxException();
    }
  }

  private int getLastLineNumber() {
    return tokens.get(tokens.size() - 1).getLineNumber();
  }

  public Integer getLineNumber() {
    try {
      return getToken().getLineNumber();
    } catch (SyntaxException e) {
      return getLastLineNumber();
    }
  }

  public void next() {
    index++;
  }

  public boolean hasNext() {
    return index < size;
  }

}
