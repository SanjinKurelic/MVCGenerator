package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

public class FilePicker extends JPanel {

  private static final String BUTTON_LABEL = "...";
  private static final int BUTTON_WIDTH = 30;
  private static final int BUTTON_MARGIN = 3;
  private static final int PATH_FIELD_WIDTH = 147;
  private static final int PATH_BUTTON_GAP_WIDTH = 5;
  private static final String ACCEPTED_EXTENSION = ".sql";
  private static final String ACCEPTED_EXTENSION_DESCRIPTION = "SQL file format";
  public static final int MODE_OPEN = 1;
  public static final int MODE_SAVE = 2;

  private int mode;
  private TextField textField;
  private JFileChooser fileChooser;

  public FilePicker() {
    fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(ACCEPTED_EXTENSION);
      }

      @Override
      public String getDescription() {
        return ACCEPTED_EXTENSION_DESCRIPTION;
      }
    });

    setLayout(new GridBagLayout());
    setBackground(StyleUtil.getBackgroundColor());
    GridBagConstraints gbc = new GridBagConstraints();

    gbc.gridy = 0;
    gbc.gridx = 0;
    textField = new TextField(PATH_FIELD_WIDTH);
    add(textField, gbc);

    gbc.gridx = 1;
    gbc.insets = new Insets(BUTTON_MARGIN, PATH_BUTTON_GAP_WIDTH, BUTTON_MARGIN, BUTTON_MARGIN);
    Button button = new Button(BUTTON_LABEL, BUTTON_WIDTH);
    button.addActionListener(this::buttonActionPerformed);
    add(button, gbc);
  }

  private void buttonActionPerformed(ActionEvent e) {
    if (mode == MODE_OPEN) {
      if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
      }
    } else if (mode == MODE_SAVE) {
      if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
      }
    }
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public String getSelectedFilePath() {
    return textField.getText();
  }
}
