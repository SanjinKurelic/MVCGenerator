package eu.sanjin.kurelic.mvcgenerator.view.ui;

import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.TargetCodeSynthesis;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetFramework;
import eu.sanjin.kurelic.mvcgenerator.synthesis.targetcode.structure.TargetSettings;
import eu.sanjin.kurelic.mvcgenerator.view.ui.components.Button;
import eu.sanjin.kurelic.mvcgenerator.view.ui.panels.InputSettingsPanel;
import eu.sanjin.kurelic.mvcgenerator.view.ui.panels.OutputSettingsPanel;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.ValidationUtil;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class MainView extends JFrame {

  private static final String APPLICATION_NAME = "MVC Generator";
  private static final int PANEL_PADDING = 10;
  private static final String GENERATE_BUTTON_LABEL = "Generate";
  private static final int GENERATE_BUTTON_WIDTH = 100;
  private static final String ERROR_POPUP_TITLE = "Error";
  private static final String SUCCESS_POPUP_TITLE = "Success";
  private static final String SUCCESS_POPUP_MESSAGE = "Files are successfully created at location: %s";

  private TargetCodeSynthesis targetCodeSynthesis;
  private InputSettingsPanel inputSettingsPanel;
  private OutputSettingsPanel outputSettingsPanel;

  public MainView() throws HeadlessException {
    super(APPLICATION_NAME);
    targetCodeSynthesis = new TargetCodeSynthesis();
    inputSettingsPanel = new InputSettingsPanel();
    outputSettingsPanel = new OutputSettingsPanel();
    configureWindow();
  }

  private void configureWindow() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    getContentPane().add(populateWindow());
    pack();
    setLocationRelativeTo(null);
  }

  private JPanel populateWindow() {
    JPanel container = new JPanel(new GridBagLayout());
    container.setBackground(StyleUtil.getBackgroundColor());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridy = 0;
    gbc.gridx = 0;
    container.add(inputSettingsPanel, gbc);

    gbc.gridy = 1;
    container.add(outputSettingsPanel, gbc);

    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.LAST_LINE_END;
    gbc.fill = GridBagConstraints.NONE;
    Button generateButton = new Button(GENERATE_BUTTON_LABEL);
    generateButton.setPreferredSize(new Dimension(GENERATE_BUTTON_WIDTH, generateButton.getPreferredSize().height));
    generateButton.addActionListener(e -> generate());
    container.add(generateButton, gbc);

    return container;
  }

  private void generate() {
    String inputFilePath = inputSettingsPanel.getInputFilePath();
    String rootNamespace = outputSettingsPanel.getRootNamespace();
    String outputDirectoryPath = outputSettingsPanel.getOutputDirectory();

    ArrayList<String> errors = ValidationUtil.validateInput(inputFilePath, rootNamespace, outputDirectoryPath);
    if (errors.size() > 0) {
      showError(String.join("\n", errors));
      return;
    }

    TargetSettings settings = new TargetSettings();
    String fileContent;
    try {
      fileContent = Files.readString(Paths.get(inputFilePath));
    } catch (IOException ignored) {
      // Should not happen because validation check this case
      showError(ValidationUtil.INPUT_FILE_NOT_READABLE);
      return;
    }
    // Only Java/Spring is supported
    settings.setTargetFramework(TargetFramework.SPRING);
    settings.setRootNamespace(Objects.isNull(rootNamespace) ? "" : rootNamespace);
    settings.setOutputPath(outputDirectoryPath);

    try {
      targetCodeSynthesis.parse(fileContent, settings);
      JOptionPane.showMessageDialog(
        this,
        String.format(SUCCESS_POPUP_MESSAGE, outputDirectoryPath),
        SUCCESS_POPUP_TITLE,
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (Exception e) {
      showError(e.getMessage());
    }
  }

  private void showError(String error) {
    JOptionPane.showMessageDialog(this, error, ERROR_POPUP_TITLE, JOptionPane.ERROR_MESSAGE);
  }
}
