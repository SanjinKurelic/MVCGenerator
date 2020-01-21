package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.UnsupportedTargetFrameworkTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.ControllerBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.DataBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.PresentationBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.ProjectBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.block.ServiceBlock;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.java.spring.block.JavaSpringControllerBlock;

public class TargetCodeSupplier {

  private ProjectBlock projectBlock;
  private PresentationBlock presentationBlock;
  private ControllerBlock controllerBlock;
  private ServiceBlock serviceBlock;
  private DataBlock dataBlock;

  public TargetCodeSupplier(TargetFramework targetFramework) throws TargetCodeException {
    if (TargetFramework.SPRING.equals(targetFramework)) {
      projectBlock;
      presentationBlock;
      controllerBlock = new JavaSpringControllerBlock();
      serviceBlock;
      dataBlock;
    }
    throw new UnsupportedTargetFrameworkTargetCodeException(targetFramework);
  }

  public ProjectBlock getProjectBlock() {
    return projectBlock;
  }

  public PresentationBlock getPresentationBlock() {
    return presentationBlock;
  }

  public ControllerBlock getControllerBlock() {
    return controllerBlock;
  }

  public ServiceBlock getServiceBlock() {
    return serviceBlock;
  }

  public DataBlock getDataBlock() {
    return dataBlock;
  }
}
