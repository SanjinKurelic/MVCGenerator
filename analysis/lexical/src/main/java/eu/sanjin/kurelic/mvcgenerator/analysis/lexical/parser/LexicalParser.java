package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.LexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.UnclosedCommentLexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.UnclosedQuotedTextLexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.UnexpectedCharacterLexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.TokenType;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.DataTypeToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.KeywordToken;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.LexicalSpecialCharacters;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.entity.SpecialCharacterToken;

import java.util.Optional;

public class LexicalParser {

    private CharacterSupplier characterSupplier;
    private int lineNumber;

    public LexicalParser(CharacterSupplier characterSupplier) {
        this.characterSupplier = characterSupplier;
    }

    // Helpers
    private boolean isNotDelimiter(char character) {
        return !Character.isWhitespace(character) && !SpecialCharacterToken.contains(character);
    }

    // Parse text like: "text text", 'text text'
    private String parseScopedValue() throws LexicalException {
        boolean closedQuotes = false;
        char delimiter = characterSupplier.getCharacter();
        characterSupplier.nextCharacter(); // skip delimiter
        while(characterSupplier.hasCharacter()) {
            if(characterSupplier.isCharacter(LexicalSpecialCharacters.ESCAPE_CHARACTER.toChar())) {
                characterSupplier.nextCharacter(); // escape character
            }
            else if(characterSupplier.isCharacter(delimiter)) {
                characterSupplier.nextCharacter(); // skip delimiter
                closedQuotes = true;
                break;
            }
            characterSupplier.nextCharacter();
        }
        if(!closedQuotes) { // EOF without closing quoted text
            throw new UnclosedQuotedTextLexicalException(lineNumber);
        }

        // Remove quotes
        String value = characterSupplier.getWord();
        value = value.substring(1, value.length() - 1);
        return value;
    }

    // Parser
    public Optional<Token> getToken() throws LexicalException {
        Optional<Token> token = Optional.empty();
        lineNumber = characterSupplier.getLineNumber();

        while(characterSupplier.hasCharacter()) {
            // Remove all spaces from beginning of a text
            // start -> {space} clear_whitespace_state
            if(Character.isWhitespace(characterSupplier.getCharacter())) {
                clearWhitespace();
            }
            // One line comment --
            else if(characterSupplier.isCharacter(LexicalSpecialCharacters.ONE_LINE_COMMENT_CHARACTER.toChar())
                    && characterSupplier.isNextCharacter(LexicalSpecialCharacters.ONE_LINE_COMMENT_CHARACTER.toChar())) {
                // start -> (--) one_line_comment
                removeOneLineComment();
            }
            // Multiple line comment /*
            else if (characterSupplier.isCharacter(LexicalSpecialCharacters.MULTIPLE_LINE_COMMENT_SLASH.toChar())
                    && characterSupplier.isNextCharacter(LexicalSpecialCharacters.MULTIPLE_LINE_COMMENT_ASTERIKS.toChar())) {
                // start -> (/*) multiple_line_comment
                characterSupplier.skipCharacter(); // /
                characterSupplier.skipCharacter(); // *
                removeMultipleLineComment();
            }
            // start -> integer_number
            else if(Character.isDigit(characterSupplier.getCharacter())) {
                token = Optional.of(integerNumberState());
            }
            // start -> (") quoted_text
            else if(characterSupplier.isCharacter(LexicalSpecialCharacters.QUOTED_DELIMITER.toChar())) {
                token = Optional.of(quotedIdentifierState());
            }
            // start -> (') quoted_constant
            else if(characterSupplier.isCharacter(LexicalSpecialCharacters.STRING_DELIMITER.toChar())) {
                token = Optional.of(quotedConstantState());
            }
            // start -> special_character
            else if(SpecialCharacterToken.contains(characterSupplier.getCharacter())) {
                token = Optional.of(specialCharacterState());
            }
            // start -> alphabetic
            else if(Character.isLetter(characterSupplier.getCharacter()) || (characterSupplier.isCharacter(LexicalSpecialCharacters.UNDERSCORE_DELIMITER.toChar()))) {
                token = Optional.of(alphabeticState());
            }
            // Undefined character
            else {
                throw new UnexpectedCharacterLexicalException(characterSupplier);
            }

            // Token found
            if(token.isPresent()) {
                break;
            }
        }

        token.ifPresent(value -> value.setLineNumber(lineNumber));
        return token;
    }

    /**
     * White space state
     */
    private void clearWhitespace() {
        while(characterSupplier.hasCharacter()) {
            if(!Character.isWhitespace(characterSupplier.getCharacter())) {
                break;
            }
            characterSupplier.skipCharacter();
        }
    }

    /**
     * One line state
     */
    private void removeOneLineComment() {
        while(characterSupplier.hasCharacter()
                && !characterSupplier.isCharacter(LexicalSpecialCharacters.ONE_LINE_COMMENT_END.toChar())) {
            characterSupplier.skipCharacter();
        }
        characterSupplier.skipCharacter(); // COMMENT END
    }

    /**
     * Multiple line state
     * @throws LexicalException if comment is unclosed
     */
    private void removeMultipleLineComment() throws LexicalException {
        boolean closedComment = false;
        while(characterSupplier.hasCharacter()) {
            if(characterSupplier.isCharacter(LexicalSpecialCharacters.ESCAPE_CHARACTER.toChar())) {
                characterSupplier.skipCharacter(); // escape character
            }
            // Possible end of comment
            else if(characterSupplier.isCharacter(LexicalSpecialCharacters.MULTIPLE_LINE_COMMENT_ASTERIKS.toChar())) {
                characterSupplier.skipCharacter(); // *
                // End of comment
                if(characterSupplier.isCharacter(LexicalSpecialCharacters.MULTIPLE_LINE_COMMENT_SLASH.toChar())) {
                    characterSupplier.skipCharacter(); // /
                    closedComment = true;
                    break;
                }
                else {
                    continue;
                }
            }
            characterSupplier.skipCharacter();
        }
        // Unclosed comment
        if(!closedComment) {
            throw new UnclosedCommentLexicalException(lineNumber);
        }
    }

    /**
     * Integer number state
     * @return Token
     * @throws LexicalException if number is invalid
     */
    private Token integerNumberState() throws LexicalException {
        while(characterSupplier.hasCharacter()
                && (isNotDelimiter(characterSupplier.getCharacter())
                || characterSupplier.isCharacter(LexicalSpecialCharacters.DECIMAL_POINT.toChar()))) {
            // integer_number -> real_number
            if(characterSupplier.isCharacter(LexicalSpecialCharacters.DECIMAL_POINT.toChar())) {
                return realNumberState();
            }
            // token is invalid number
            else if(!Character.isDigit(characterSupplier.getCharacter())) {
                throw new UnexpectedCharacterLexicalException(characterSupplier);
            }
            characterSupplier.nextCharacter();
        }
        return new Token(TokenType.CONSTANT_INTEGER_VALUE, characterSupplier.getWord());
    }

    /**
     * Real number state
     * @return Token
     * @throws LexicalException if number is invalid
     */
    private Token realNumberState() throws LexicalException {
        characterSupplier.nextCharacter(); // . (real number delimiter)
        while(characterSupplier.hasCharacter()
                && isNotDelimiter(characterSupplier.getCharacter())) {
            // token is invalid number
            if(!Character.isDigit(characterSupplier.getCharacter())) {
                throw new UnexpectedCharacterLexicalException(characterSupplier);
            }
            characterSupplier.nextCharacter();
        }
        return new Token(TokenType.CONSTANT_REAL_NUMBER_VALUE, characterSupplier.getWord());
    }

    /**
     * Quoted text state (")
     * @return Token (removed quotes)
     * @throws LexicalException if quoted text is unclosed
     */
    private Token quotedIdentifierState() throws LexicalException {
        return new Token(TokenType.QUOTED_IDENTIFIER_VALUE, parseScopedValue());
    }

    /**
     * Quoted constant state (')
     * @return Token
     * @throws LexicalException if quoted text is unclosed
     */
    private Token quotedConstantState() throws LexicalException {
        return new Token(TokenType.CONSTANT_QUOTED_VALUE, parseScopedValue());
    }

    /**
     * Special character state
     * Change <=, >=, <>, != to one character token
     * @return Token
     * @throws LexicalException if wrong combination of special characters is presented
     */
    private Token specialCharacterState() throws LexicalException {
        String value = null;
        if(characterSupplier.isCharacter(SpecialCharacterToken.LESS.toChar())) {
            if(characterSupplier.isNextCharacter(SpecialCharacterToken.EQUAL.toChar())) {
                characterSupplier.nextCharacter(); // <=
                value = SpecialCharacterToken.LESS_EQUAL.toString();
            }
            else if(characterSupplier.isNextCharacter(SpecialCharacterToken.GREATER.toChar())) {
                characterSupplier.nextCharacter(); // <>
                value = SpecialCharacterToken.NOT_EQUAL.toString();
            }
        } else if(characterSupplier.isCharacter(SpecialCharacterToken.GREATER.toChar())) {
            if(characterSupplier.isNextCharacter(SpecialCharacterToken.EQUAL.toChar())) {
                characterSupplier.nextCharacter(); // >=
                value = SpecialCharacterToken.GREATER_EQUAL.toString();
            }
        } else if(characterSupplier.isCharacter(SpecialCharacterToken.NOT.toChar())) {
            // Accept only !=
            if(characterSupplier.isNextCharacter(SpecialCharacterToken.EQUAL.toChar())) {
                characterSupplier.nextCharacter(); // !=
                value = SpecialCharacterToken.NOT_EQUAL.toString();
            } else {
                throw new UnexpectedCharacterLexicalException(characterSupplier);
            }
        }
        if(value == null) {
            // move pointer to non special character character
            characterSupplier.nextCharacter();
            value = characterSupplier.getWord();
        } else {
            // Value is stored, skip word/character
            characterSupplier.skipCharacter();
        }
        return new Token(TokenType.SPECIAL_CHARACTER, value);
    }

    /**
     * Alphabetic state -> keyword, data type, identifier
     * @return Token [a-z] [A-Z] [0-9] _
     */
    private Token alphabeticState() {
        while(characterSupplier.hasCharacter() &&
                (Character.isLetterOrDigit(characterSupplier.getCharacter())
                        || characterSupplier.isCharacter(LexicalSpecialCharacters.UNDERSCORE_DELIMITER.toChar()))) {
            characterSupplier.nextCharacter();
        }
        String token = characterSupplier.getWord();
        if(DataTypeToken.contains(token)) {
            return new Token(TokenType.DATA_TYPE, token);
        } else if(KeywordToken.contains(token)) {
            return new Token(TokenType.KEYWORD, token);
        }
        return new Token(TokenType.IDENTIFIER, token);
    }

}
