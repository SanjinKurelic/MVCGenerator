package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer;

import eu.sanjin.kurelic.mvcgenerator.analysis.semantic.structure.attribute.TableAttribute;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.ClassComponent;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.Component;

public interface ServiceWriter {

  ClassComponent generateCode(ClassComponent repository, ClassComponent entity, Component id, TableAttribute tableAttribute);

}
