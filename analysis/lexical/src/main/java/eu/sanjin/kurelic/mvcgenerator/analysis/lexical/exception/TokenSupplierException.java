package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class TokenSupplierException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Token Supplier [current index=%d, begin index=%d, text length=%d, text='%s']";
    private String message;

    public TokenSupplierException(String message) {
        this.message = message;
    }

    public TokenSupplierException(int currentIndex, int beginIndex, int textLength, String text) {
        this(String.format(ERROR_MESSAGE, currentIndex, beginIndex, textLength, text));
    }

    @Override
    public String getMessage() {
        return message;
    }

}
