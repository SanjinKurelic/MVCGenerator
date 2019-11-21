package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontUtil;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;

public class DisabledComboBox extends JComboBox<String> {

  private static final int DEFAULT_COMBO_WIDTH = 180;
  private static final int DEFAULT_COMBO_HEIGHT = 24;
  private static final int TEXT_FIELD_PADDING_LEFT_RIGHT = 5;
  private static final int TEXT_FIELD_PADDING_TOP_BOTTOM = 0;
  private static final int TEXT_FIELD_FONT_SIZE = 12;

  public DisabledComboBox(String selectedItem) {
    super(new String[]{selectedItem});
    setEnabled(false);
    setStyle();
  }

  private void setStyle() {
    setPreferredSize(new Dimension(DEFAULT_COMBO_WIDTH, DEFAULT_COMBO_HEIGHT));
    setEditable(true);
    setBorder(StyleUtil.getBorder());
    setFont(FontUtil.getFont());

    // Style text edit of combobox
    JTextField textField = (JTextField) getEditor().getEditorComponent();
    textField.setDisabledTextColor(StyleUtil.getTextColor());
    textField.setFont(FontUtil.getFont(TEXT_FIELD_FONT_SIZE));
    textField.setBackground(StyleUtil.getBackgroundColor());
    textField.setBorder(BorderFactory.createEmptyBorder(
        TEXT_FIELD_PADDING_TOP_BOTTOM, TEXT_FIELD_PADDING_LEFT_RIGHT, TEXT_FIELD_PADDING_TOP_BOTTOM, TEXT_FIELD_PADDING_LEFT_RIGHT
    ));

    // Style arrow button of combobox
    for (Component component : this.getComponents()) {
      if (component instanceof JButton) {
        component.setBackground(StyleUtil.getBackgroundColor());
        component.setForeground(StyleUtil.getTextColor());
      }
    }
  }
}
