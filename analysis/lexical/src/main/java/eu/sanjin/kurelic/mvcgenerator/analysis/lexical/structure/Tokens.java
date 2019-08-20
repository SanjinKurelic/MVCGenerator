package eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure;

import java.util.ArrayList;

public class Tokens extends ArrayList<Token> {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Token t : this) {
            sb.append(t).append("\n");
        }
        return sb.toString();
    }

}
