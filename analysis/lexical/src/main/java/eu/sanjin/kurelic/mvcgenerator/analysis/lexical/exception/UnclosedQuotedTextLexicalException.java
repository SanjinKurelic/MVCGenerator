package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser.CharacterSupplier;

public class UnclosedQuotedTextLexicalException extends LexicalException {

  private static final String ERROR_STRING = "Unclosed quoted value started at line %d";

  public UnclosedQuotedTextLexicalException(CharacterSupplier characterSupplier) {
    this(characterSupplier.getLineNumber());
  }

  public UnclosedQuotedTextLexicalException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
