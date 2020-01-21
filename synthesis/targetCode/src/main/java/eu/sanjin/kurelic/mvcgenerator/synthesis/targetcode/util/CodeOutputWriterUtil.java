package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.OutputWriterTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.TargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;

import java.io.File;

public class CodeOutputWriterUtil {

  private String projectPath;
  private String namespacePath;

  public CodeOutputWriterUtil(TargetSettings targetSettings) {
    projectPath = targetSettings.getOutputPath() + File.pathSeparator + targetSettings.getProjectName();
    namespacePath = targetSettings.getRootNamespace();
  }

  public void createAbsolutePackage(String packagePath) throws TargetCodeException {
    File folder;
    for (String path : packagePath.split("\\.")) {
      folder = new File(projectPath + File.pathSeparator + path);
      if (!folder.exists()) {
        if (!folder.mkdir()) {
          throw new OutputWriterTargetCodeException(folder);
        }
      }
    }
  }

  public void createRelativePackage(String packagePath) throws TargetCodeException {
    createAbsolutePackage(namespacePath + File.pathSeparator + packagePath);
  }

}
