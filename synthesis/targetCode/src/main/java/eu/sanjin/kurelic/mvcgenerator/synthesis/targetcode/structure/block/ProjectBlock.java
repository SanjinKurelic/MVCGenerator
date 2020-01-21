package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;

public interface ProjectBlock {

  TargetSettings adjustTargetSettings(TargetSettings targetSettings);

  void generateProjectStructure(TargetSettings targetSettings);
}
