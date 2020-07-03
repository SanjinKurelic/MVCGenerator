package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.ControllerWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.ClassComponent;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.component.Component;

public class JavaSpringControllerWriter implements ControllerWriter {

  @Override
  public ClassComponent generateCode(ClassComponent view, ClassComponent service, ClassComponent entity, Component id) {

    // Generate service autowire (annotation and variable)

    // Generate getAll method

    // Generate get(id) method

    // Generate add(Entity) method

    // Generate update(Entity) method

    // Generate delete(id) method

    // Generate imports (Spring service etc)

    // Generate class wrapper + Controller annotation

    return null;
  }
}
