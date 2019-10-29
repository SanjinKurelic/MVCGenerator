package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.TokenSupplierException;

public class CharacterSupplier {

  private int currentIndex;
  private int beginIndex;
  private int textLength;
  private int lineNumber;
  private String text;

  public CharacterSupplier(String text) {
    this.text = text;
    beginIndex = currentIndex = 0;
    textLength = text.length();
    lineNumber = 1;
  }

  // Helpers
  private boolean hasCharacter(int index) {
    return index < textLength;
  }

  private char getCharacter(int index) {
    if (!hasCharacter(index)) {
      throw new TokenSupplierException(currentIndex, beginIndex, textLength, text);
    }
    return text.charAt(index);
  }

  // Booleans
  public boolean hasCharacter() {
    return hasCharacter(currentIndex);
  }

  private boolean hasNextCharacter() {
    return hasCharacter(currentIndex + 1);
  }

  boolean isCharacter(char character) {
    return hasCharacter() && character == getCharacter();
  }

  boolean isNextCharacter(char character) {
    return hasNextCharacter() && character == peekCharacter();
  }

  // Getters
  public String getWord() {
    String r;
    if (beginIndex == currentIndex) {
      r = String.valueOf(text.charAt(currentIndex));
    } else {
      r = text.substring(beginIndex, currentIndex);
    }
    beginIndex = currentIndex;
    return r;
  }

  public char getCharacter() {
    return getCharacter(currentIndex);
  }

  private char peekCharacter() {
    return getCharacter(currentIndex + 1);
  }

  public int getLineNumber() {
    return lineNumber;
  }

  // Setters
  int nextCharacter() {
    currentIndex++;
    if (isCharacter('\n')) {
      lineNumber++;
    }
    return currentIndex;
  }

  void skipCharacter() {
    beginIndex = nextCharacter();
  }
}
