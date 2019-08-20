package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser.CharacterSupplier;

public class UnclosedCommentLexicalException extends LexicalException {

    public static final String ERROR_STRING = "Unclosed comment started at line %d";

    public UnclosedCommentLexicalException(CharacterSupplier characterSupplier) {
        this(characterSupplier.getLineNumber());
    }

    public UnclosedCommentLexicalException(int lineNumber) {
        super(String.format(ERROR_STRING, lineNumber));
    }

}
