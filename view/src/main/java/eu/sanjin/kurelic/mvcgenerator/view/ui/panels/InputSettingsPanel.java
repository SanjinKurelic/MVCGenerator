package eu.sanjin.kurelic.mvcgenerator.view.ui.panels;

import eu.sanjin.kurelic.mvcgenerator.view.ui.components.DisabledComboBox;
import eu.sanjin.kurelic.mvcgenerator.view.ui.components.FilePicker;

public class InputSettingsPanel extends SettingsPanel {

  private static final String PANEL_TITLE = "Input settings";
  private static final String SQL_FILE_LABEL = "SQL file";
  private static final String SQL_DIALECT_LABEL = "SQL dialect";
  private static final String DEFAULT_SQL_DIALECT = "Standard SQL-92";

  private FilePicker filePicker;

  public InputSettingsPanel() {
    super(PANEL_TITLE);
    populatePanel();
  }

  private void populatePanel() {
    filePicker = new FilePicker();
    filePicker.setMode(FilePicker.MODE_OPEN);
    addRow(SQL_FILE_LABEL, filePicker);

    addRow(SQL_DIALECT_LABEL, new DisabledComboBox(DEFAULT_SQL_DIALECT));
  }

  public String getInputFilePath() {
    return filePicker.getSelectedFilePath();
  }
}
