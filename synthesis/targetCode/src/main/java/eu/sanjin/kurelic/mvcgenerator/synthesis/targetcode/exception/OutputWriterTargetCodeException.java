package eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.exception;

import java.io.File;

public class OutputWriterTargetCodeException extends TargetCodeException {

  private static final String DIRECTORY_CREATION_ERROR = "Error while creating directory %s";
  private static final String FILE_CREATION_ERROR = "Error while creating file %s in directory %s";

  public OutputWriterTargetCodeException(File directory) {
    super(String.format(DIRECTORY_CREATION_ERROR, directory.getAbsolutePath()));
  }

  public OutputWriterTargetCodeException(File directory, File file) {
    super(String.format(FILE_CREATION_ERROR, file.getName(), directory.getAbsolutePath()));
  }
}
