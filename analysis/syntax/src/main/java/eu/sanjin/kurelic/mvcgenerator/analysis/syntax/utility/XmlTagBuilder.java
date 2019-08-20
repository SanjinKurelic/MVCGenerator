package eu.sanjin.kurelic.mvcgenerator.analysis.syntax.utility;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;

public class XmlTagBuilder {

    private static final String OPEN_START_TAG = "<";
    private static final String OPEN_END_TAG = "</";
    private static final String CLOSE_TAG = ">\n";

    public static String getStartTag(Object object) {
        return getStartTag(object.getClass().getSimpleName());
    }

    public static String getStartTag(String tagName) {
        return OPEN_START_TAG + tagName + CLOSE_TAG;
    }

    public static String getEndTag(Object object) {
        return getEndTag(object.getClass().getSimpleName());
    }

    public static String getEndTag(String tagName) {
        return OPEN_END_TAG + tagName + CLOSE_TAG;
    }

    public static String suroundToken(Token token, String tagName) {
        return getStartTag(tagName) + token.getValue() + getEndTag(tagName);
    }

}
