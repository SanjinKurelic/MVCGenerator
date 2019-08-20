package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidKeyNameSyntaxException extends SyntaxException {

    private static final String ERROR_STRING = "Key name is not valid, at line %d";
    private String message;

    public InvalidKeyNameSyntaxException(int lineNumber) {
        message = String.format(ERROR_STRING, lineNumber);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
