package eu.sanjin.kurelic.mvcgenerator.analysis.lexical;

import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.exception.LexicalException;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser.LexicalParser;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.parser.CharacterSupplier;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Token;
import eu.sanjin.kurelic.mvcgenerator.analysis.lexical.structure.Tokens;

import java.util.Objects;
import java.util.Optional;

public class LexicalAnalyzer {

  private Tokens tokens;

  // Get token reader and read all tokens
  public void parse(String code) throws LexicalException {
    tokens = new Tokens();
    if (Objects.isNull(code)) {
      return;
    }
    CharacterSupplier characterSupplier = new CharacterSupplier(code);
    LexicalParser parser = new LexicalParser(characterSupplier);
    Optional<Token> token;

    while (characterSupplier.hasCharacter()) {
      token = parser.getToken();
      token.ifPresent(value -> tokens.add(value));
    }
  }

  public Tokens getTokens() {
    return tokens;
  }
}
