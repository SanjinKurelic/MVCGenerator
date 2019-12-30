package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util;

public class CodePrettifyUtil {

  public static String indentCode(String code) {
    StringBuilder indentedCode = new StringBuilder();
    for (String line : code.split("\n")) {
      indentedCode.append("\t").append(line).append("\n");
    }
    return indentedCode.toString();
  }
}
