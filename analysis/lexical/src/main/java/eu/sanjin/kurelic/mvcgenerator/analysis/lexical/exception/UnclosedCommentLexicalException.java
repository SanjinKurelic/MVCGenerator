package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class UnclosedCommentLexicalException extends LexicalException {

  public static final String ERROR_STRING = "Unclosed comment started at line %d";

  public UnclosedCommentLexicalException(int lineNumber) {
    super(String.format(ERROR_STRING, lineNumber));
  }
}
