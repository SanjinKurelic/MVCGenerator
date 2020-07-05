package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.impl.java.spring.writer;

import com.x5.template.Theme;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception.OutputWriterTargetCodeException;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.parser.TargetCodeSupplier;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.models.TemplateAttributeNames;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.definition.writer.ProjectWriter;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.util.CodeOutputWriterUtil;

import java.io.File;
import java.nio.file.Paths;

public class JavaSpringProjectWriter implements ProjectWriter {

  private static final String TEMPLATE_PREFIX = TargetCodeSupplier.JAVA_SPRING_TEMPLATE + "project_";

  private TargetSettings targetSettings;

  @Override
  public void generateProjectStructure(TargetSettings targetSettings) throws OutputWriterTargetCodeException {
    this.targetSettings = targetSettings;
    String namespace = targetSettings.getRootNamespace().replace('.', File.separatorChar);

    // write pom
    writeFile("pom", "", "xml", getFileContent("pom"));

    // write start
    String startPath = Paths.get("src", "main", "java", namespace).toString();
    String startFileName = targetSettings.getProjectName() + "Application";
    writeFile(startFileName, startPath, "java", getFileContent("start"));

    // write test
    String testPath = Paths.get("src", "test", "java", namespace).toString();
    String testFileName = targetSettings.getProjectName() + "ApplicationTests";
    writeFile(testFileName, testPath, "java", getFileContent("test"));

    // write config
    String configPath = Paths.get("src", "main", "java", namespace, "config").toString();
    String configFileName = targetSettings.getProjectName() + "GlobalExceptionHandler";
    writeFile(configFileName, configPath, "java", getFileContent("config"));

    // write properties
    String propertiesPath = Paths.get("src", "main", "resources").toString();
    writeFile("application", propertiesPath, "properties", getFileContent("properties"));
  }

  private String getFileContent(String templateFile) {
    var chunk = new Theme().makeChunk(TEMPLATE_PREFIX + templateFile);
    chunk.set(TemplateAttributeNames.ROOT_NAMESPACE, targetSettings.getRootNamespace());
    chunk.set(TemplateAttributeNames.PROJECT_NAME, targetSettings.getProjectName());
    return chunk.toString();
  }

  private void writeFile(String fileName, String filePath, String fileExtension, String fileContent) throws OutputWriterTargetCodeException {
    CodeOutputWriterUtil.getInstance(targetSettings).writeFileInProject(fileName, filePath, fileExtension, fileContent);
  }
}
