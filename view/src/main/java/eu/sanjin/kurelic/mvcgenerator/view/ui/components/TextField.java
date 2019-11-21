package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Dimension;

public class TextField extends JTextField {

  private static final int DEFAULT_FIELD_WIDTH = 180;
  private static final int DEFAULT_FIELD_HEIGHT = 26;
  private static final int PADDING_LEFT_RIGHT = 5;
  private static final int PADDING_TOP_BOTTOM = 0;

  public TextField() {
    this(DEFAULT_FIELD_WIDTH);
  }

  TextField(int width) {
    super();
    setStyle(width);
  }

  private void setStyle(int width) {
    setBackground(StyleUtil.getBackgroundColor());
    setForeground(StyleUtil.getTextColor());
    setBorder(BorderFactory.createCompoundBorder(
        StyleUtil.getBorder(),
        BorderFactory.createEmptyBorder(PADDING_TOP_BOTTOM, PADDING_LEFT_RIGHT, PADDING_TOP_BOTTOM, PADDING_LEFT_RIGHT)
    ));
    setPreferredSize(new Dimension(width, DEFAULT_FIELD_HEIGHT));
  }
}
