package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.UnsupportedTargetFrameworkTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringControllerWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringEntityWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringProjectWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringRepositoryWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringServiceWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.writer.JavaSpringViewWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.ControllerWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.EntityWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.ProjectWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.RepositoryWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.ServiceWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.writer.ViewWriter;

public class TargetCodeSupplier {

  private final ProjectWriter projectWriter;
  private final ViewWriter viewWriter;
  private final ControllerWriter controllerWriter;
  private final ServiceWriter serviceWriter;
  private final EntityWriter entityWriter;
  private final RepositoryWriter repositoryWriter;

  public TargetCodeSupplier(TargetFramework targetFramework) throws TargetCodeException {
    if (TargetFramework.SPRING.equals(targetFramework)) {
      projectWriter = new JavaSpringProjectWriter();
      controllerWriter = new JavaSpringControllerWriter();
      entityWriter = new JavaSpringEntityWriter();
      repositoryWriter = new JavaSpringRepositoryWriter();
      serviceWriter = new JavaSpringServiceWriter();
      viewWriter = new JavaSpringViewWriter();
    }
    throw new UnsupportedTargetFrameworkTargetCodeException(targetFramework);
  }

  public ProjectWriter getProjectWriter() {
    return projectWriter;
  }

  public ViewWriter getViewWriter() {
    return viewWriter;
  }

  public ControllerWriter getControllerWriter() {
    return controllerWriter;
  }

  public ServiceWriter getServiceWriter() {
    return serviceWriter;
  }

  public EntityWriter getEntityWriter() {
    return entityWriter;
  }

  public RepositoryWriter getRepositoryWriter() {
    return repositoryWriter;
  }
}
