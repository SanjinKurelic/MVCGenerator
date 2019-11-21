package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

public class FilePicker extends JPanel {

  private static final String BUTTON_LABEL = "...";
  private static final int PATH_FIELD_WIDTH = 140;
  private static final int PADDING = 0;
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
    setLayout(new FlowLayout(FlowLayout.CENTER, PADDING, PADDING));

    textField = new TextField(PATH_FIELD_WIDTH);
    add(textField);

    Button button = new Button(BUTTON_LABEL);
    button.addActionListener(this::buttonActionPerformed);
    add(button);
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

  public JFileChooser getFileChooser() {
    return this.fileChooser;
  }
}
