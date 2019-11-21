package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontLoader;

import javax.swing.JButton;
import java.awt.Dimension;

public class Button extends JButton {

  private static final int DEFAULT_BUTTON_HEIGHT = 26;

  public Button(String text) {
    super(text);
    setStyle();
  }

  private void setStyle() {
    setPreferredSize(new Dimension(getMinimumSize().width, DEFAULT_BUTTON_HEIGHT));
    setFont(FontLoader.getFont());
  }
}
