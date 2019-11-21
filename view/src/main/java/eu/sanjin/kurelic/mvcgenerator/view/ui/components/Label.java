package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontUtil;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.JLabel;
import java.awt.Dimension;

public class Label extends JLabel {

  private static final int DEFAULT_LABEL_WIDTH = 160;
  private static final int DEFAULT_LABEL_HEIGHT = 26;

  public Label(String text) {
    super(text);
    setStyle();
  }

  private void setStyle() {
    setFont(FontUtil.getFont());
    setBackground(StyleUtil.getBackgroundColor());
    setForeground(StyleUtil.getTextColor());
    setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, DEFAULT_LABEL_HEIGHT));
  }
}
