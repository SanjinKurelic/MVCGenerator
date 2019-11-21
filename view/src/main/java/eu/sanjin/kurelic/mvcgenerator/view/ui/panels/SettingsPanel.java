package eu.sanjin.kurelic.mvcgenerator.view.ui.panels;

import eu.sanjin.kurelic.mvcgenerator.view.ui.components.Label;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontUtil;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

class SettingsPanel extends JPanel {

  private static final String LABEL_APPENDER = ":";
  private static final int PANEL_PADDING_LEFT_RIGHT = 10;
  private static final int PANEL_PADDING_TOP_BOTTOM = 5;
  private static final int TITLE_FONT_SIZE = 12;

  private GridBagConstraints gbc;
  private int currentRow = 0;

  SettingsPanel(String panelTitle) {
    setLayout(new GridBagLayout());
    setBackground(StyleUtil.getBackgroundColor());
    setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(StyleUtil.getTextColor()),
        panelTitle,
        TitledBorder.LEADING,
        TitledBorder.DEFAULT_POSITION,
        FontUtil.getFont(TITLE_FONT_SIZE)
    ));
    gbc = new GridBagConstraints();
    gbc.insets = new Insets(PANEL_PADDING_TOP_BOTTOM, PANEL_PADDING_LEFT_RIGHT, PANEL_PADDING_TOP_BOTTOM, PANEL_PADDING_LEFT_RIGHT);
    gbc.anchor = GridBagConstraints.LINE_START;
    gbc.weightx = 0.5;
    gbc.weighty = 0.5;
  }

  void addRow(String labelName, JComponent component) {
    gbc.gridy = currentRow;
    gbc.gridx = 0;
    add(new Label(labelName + LABEL_APPENDER), gbc);

    gbc.gridx = 1;
    add(component, gbc);

    currentRow++;
  }
}
