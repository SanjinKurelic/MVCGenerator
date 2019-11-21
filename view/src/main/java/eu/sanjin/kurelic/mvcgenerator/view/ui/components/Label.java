package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontLoader;

import javax.swing.JLabel;
import java.awt.Dimension;

public class Label extends JLabel {

  private static final int DEFAULT_LABEL_WIDTH = 160;

  public Label(String text) {
    super(text);
    setStyle();
  }

  private void setStyle() {
    setFont(FontLoader.getFont());
    setPreferredSize(new Dimension(DEFAULT_LABEL_WIDTH, getMinimumSize().height));
  }
}
