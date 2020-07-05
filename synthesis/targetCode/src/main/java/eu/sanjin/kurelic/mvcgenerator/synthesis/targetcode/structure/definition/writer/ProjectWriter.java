package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.OutputWriterTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;

public interface ProjectWriter {

  void generateProjectStructure(TargetSettings targetSettings) throws OutputWriterTargetCodeException;

}
