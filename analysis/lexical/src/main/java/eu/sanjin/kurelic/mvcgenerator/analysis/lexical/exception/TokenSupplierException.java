package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception;

public class TokenSupplierException extends RuntimeException {

  private static final String ERROR_MESSAGE = "Token Supplier [current index=%d, begin index=%d, text length=%d, text='%s']";

  public TokenSupplierException(int currentIndex, int beginIndex, int textLength, String text) {
    super(String.format(ERROR_MESSAGE, currentIndex, beginIndex, textLength, text));
  }
}
