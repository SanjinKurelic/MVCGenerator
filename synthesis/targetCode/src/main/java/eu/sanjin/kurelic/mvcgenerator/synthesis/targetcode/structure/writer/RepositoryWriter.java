package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.ClassComponent;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.Component;

public interface RepositoryWriter {

  ClassComponent generateCode(ClassComponent entity, Component id);

}
