package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception;

public class InvalidReferenceColumnCountSyntaxException extends SyntaxException {

    private static final String ERROR_STRING = "Reference clause has to many or to few columns at line %d";
    private String message;

    public InvalidReferenceColumnCountSyntaxException(int lineNumber) {
        message = String.format(ERROR_STRING, lineNumber);
    }

    @Override
    public String getMessage() {
        return message;
    }

}
