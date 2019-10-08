package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser.CharacterSupplier;

public class UnexpectedCharacterLexicalException extends LexicalException {

  private static final String ERROR_MESSAGE = "Unexpected character '%c' near word '%s' at line %d";

  public UnexpectedCharacterLexicalException(CharacterSupplier characterSupplier) {
    this(characterSupplier.getCharacter(), characterSupplier.getWord(), characterSupplier.getLineNumber());
  }

  public UnexpectedCharacterLexicalException(char character, String word, int lineNumber) {
    super(String.format(ERROR_MESSAGE, character, word, lineNumber));
  }
}
