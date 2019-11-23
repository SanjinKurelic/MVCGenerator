package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode;

import eu.sanjin.kurelic.mvcgenerator.synthesis.intermediatecode.IntermediateCodeSynthesis;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser.TargetCodeParser;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;

public class TargetCodeSynthesis {

  private IntermediateCodeSynthesis intermediateCodeSynthesis;
  private TargetCodeParser targetCodeParser;

  public void parse(String code, TargetSettings targetSettings) throws Exception {
    intermediateCodeSynthesis.parse(code);
    // TODO use settings
  }
}
