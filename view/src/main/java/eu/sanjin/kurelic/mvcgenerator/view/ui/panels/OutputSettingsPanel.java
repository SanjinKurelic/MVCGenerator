package eu.sanjin.kurelic.mvcgenerator.view.ui.panels;

import eu.sanjin.kurelic.mvcgenerator.view.ui.components.DisabledComboBox;
import eu.sanjin.kurelic.mvcgenerator.view.ui.components.FilePicker;
import eu.sanjin.kurelic.mvcgenerator.view.ui.components.TextField;

public class OutputSettingsPanel extends SettingsPanel {

  private static final String PANEL_TITLE = "Output settings";
  private static final String PROGRAMMING_LANGUAGE_LABEL = "Programming language";
  private static final String DEFAULT_PROGRAMMING_LANGUAGE = "Java";
  private static final String FRAMEWORK_LABEL = "Framework";
  private static final String DEFAULT_FRAMEWORK = "Spring + Hibernate";
  private static final String ROOT_NAMESPACE_LABEL = "Root namespace";
  private static final String OUTPUT_DIRECTORY_LABEL = "Output directory";

  private TextField rootNamespace;
  private FilePicker filePicker;

  public OutputSettingsPanel() {
    super(PANEL_TITLE);
    populateWindow();
  }

  private void populateWindow() {
    addRow(PROGRAMMING_LANGUAGE_LABEL, new DisabledComboBox(DEFAULT_PROGRAMMING_LANGUAGE));
    addRow(FRAMEWORK_LABEL, new DisabledComboBox(DEFAULT_FRAMEWORK));

    rootNamespace = new TextField();
    addRow(ROOT_NAMESPACE_LABEL, rootNamespace);

    filePicker = new FilePicker();
    filePicker.setMode(FilePicker.OPEN_DIRECTORY);
    addRow(OUTPUT_DIRECTORY_LABEL, filePicker);
  }

  public String getRootNamespace() {
    return rootNamespace.getText();
  }

  public String getOutputDirectory() {
    return filePicker.getSelectedFilePath();
  }
}
