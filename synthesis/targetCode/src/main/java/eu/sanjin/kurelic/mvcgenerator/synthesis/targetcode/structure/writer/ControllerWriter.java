package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.ClassComponent;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.Component;

public interface ControllerWriter {

  ClassComponent generateCode(ClassComponent view, ClassComponent service, ClassComponent entity, Component id);

}
