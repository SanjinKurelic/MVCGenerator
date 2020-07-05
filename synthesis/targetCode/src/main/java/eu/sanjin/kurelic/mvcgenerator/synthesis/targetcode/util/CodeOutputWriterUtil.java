package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.OutputWriterTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser.TargetCodeType;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetProgrammingLanguage;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CodeOutputWriterUtil {

  private static CodeOutputWriterUtil instance;

  private final String projectPath;
  private final String namespacePath;
  private final String fileExtension;

  private CodeOutputWriterUtil(TargetSettings targetSettings) {
    projectPath = Paths.get(targetSettings.getOutputPath(), targetSettings.getProjectName()).toAbsolutePath().toString();

    // Change dots to path separator
    String namespace = targetSettings.getRootNamespace().replace('.', File.separatorChar);

    if (targetSettings.getTargetFramework().getTargetProgrammingLanguage() == TargetProgrammingLanguage.JAVA) {
      fileExtension = "java";
      namespacePath = Paths.get(projectPath, "src", "main", "java", namespace).toAbsolutePath().toString();
    } else {
      fileExtension = ""; // add for PHP/C# and others
      namespacePath = projectPath;
    }
  }

  public static CodeOutputWriterUtil getInstance(TargetSettings targetSettings) {
    if (Objects.isNull(instance)) {
      instance = new CodeOutputWriterUtil(targetSettings);
    }
    return instance;
  }

  private void writeFile(String fileName, String fileContent) throws OutputWriterTargetCodeException {
    File file = new File(fileName);

    // Create all parent dirs
    File parent = file.getParentFile();
    if (!parent.exists() && !parent.mkdirs()) {
      throw new OutputWriterTargetCodeException(parent);
    }

    // Write content
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(fileContent);
    } catch (IOException e) {
      throw new OutputWriterTargetCodeException(parent, file);
    }
  }

  public void writeFileInProject(String fileName, String filePath, String fileExtension, String fileContent) throws OutputWriterTargetCodeException {
    String path = Path.of(projectPath, filePath, fileName + '.' + fileExtension).toAbsolutePath().toString();
    writeFile(path, fileContent);
  }

  public void writeFileInNamespace(TargetCodeType targetCodeType, String fileName, String fileContent) throws OutputWriterTargetCodeException {
    String path = Paths.get(namespacePath, targetCodeType.name().toLowerCase(), fileName + '.' + fileExtension).toAbsolutePath().toString();
    writeFile(path, fileContent);
  }
}
