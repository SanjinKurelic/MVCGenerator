package eu.sanjin.kurelic.mvcgenerator.view.ui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ValidationUtil {

  private static final String INPUT_FILE_EMPTY = "Please select sql input file.";
  private static final String INPUT_FILE_NOT_EXISTS = "Selected SQL file does not exists. Have you delete it?";
  public static final String INPUT_FILE_NOT_READABLE = "Selected SQL file is not readable. Please check permission.";
  private static final String PROJECT_NAME_EMPTY = "Project name is required.";
  private static final String PROJECT_NAME_UNEXPECTED_CHARACTER = "Project name can contain only alphabet characters.";
  private static final String ROOT_NAMESPACE_UNEXPECTED_CHARACTER = "Root namespace can contain only alphabet and dot characters.";
  private static final String OUTPUT_DIRECTORY_EMPTY = "Please select output directory.";
  private static final String OUTPUT_DIRECTORY_NOT_READABLE = "Output directory is not accessible. Please check permissions.";

  public static ArrayList<String> validateInput(String inputFilePath, String projectName, String rootNamespace, String outputDirectoryPath) {
    ArrayList<String> errors = new ArrayList<>();

    // Input file
    if (Objects.isNull(inputFilePath) || inputFilePath.isBlank()) {
      errors.add(INPUT_FILE_EMPTY);
    } else {
      File inputFile = new File(inputFilePath);
      if (!inputFile.exists()) {
        errors.add(INPUT_FILE_NOT_EXISTS);
      } else if (!inputFile.canRead()) {
        errors.add(INPUT_FILE_NOT_READABLE);
      }
    }

    // Project name
    if (Objects.isNull(projectName) || projectName.isBlank()) {
      errors.add(PROJECT_NAME_EMPTY);
    } else {
      for (char character : projectName.toCharArray()) {
        if (!Character.isAlphabetic(character)) {
          errors.add(PROJECT_NAME_UNEXPECTED_CHARACTER);
          break;
        }
      }
    }

    // Root namespace
    if (!Objects.isNull(rootNamespace) && !rootNamespace.isBlank()) {
      for (char character : rootNamespace.toCharArray()) {
        if (!Character.isAlphabetic(character) && character != '.') {
          errors.add(ROOT_NAMESPACE_UNEXPECTED_CHARACTER);
          break;
        }
      }
    }

    // Output directory
    if (Objects.isNull(outputDirectoryPath) || outputDirectoryPath.isBlank()) {
      errors.add(OUTPUT_DIRECTORY_EMPTY);
    } else {
      File outputDirectory = new File(outputDirectoryPath);
      if (!outputDirectory.canRead()) {
        errors.add(OUTPUT_DIRECTORY_NOT_READABLE);
      }
    }

    return errors;
  }
}
