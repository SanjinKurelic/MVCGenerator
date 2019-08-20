package eu.sanjin.kurelic.mvcgenerator.analysis.syntax;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.exception.SyntaxException;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.SyntaxParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.parser.TokenSupplier;
import eu.sanjin.kurelic.mvcgenerator.analysis.syntax.structure.SyntaxTree;

public class SyntaxAnalyzer {

    private TokenSupplier tokenSupplier;
    private SyntaxParser syntaxParser;

    public SyntaxAnalyzer(Tokens tokens) {
        tokenSupplier = new TokenSupplier(tokens);
        syntaxParser = new SyntaxParser(tokenSupplier);
    }

    public void parse() throws SyntaxException {
        syntaxParser.parse();
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxParser.getSyntaxTree();
    }

}
