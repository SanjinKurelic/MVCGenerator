package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class UnclosedQuotedTextLexicalException extends LexicalException {

  private static final String ERROR_STRING = "Unclosed quoted value started at line %d";

  public UnclosedQuotedTextLexicalException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
