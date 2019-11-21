package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import javax.swing.JTextField;
import java.awt.Dimension;

public class TextField extends JTextField {

  private static final int DEFAULT_FIELD_WIDTH = 180;
  private static final int DEFAULT_FIELD_HEIGHT = 24;

  public TextField() {
    this(DEFAULT_FIELD_WIDTH);
  }

  TextField(int width) {
    super();
    setStyle(width);
  }

  private void setStyle(int width) {
    setPreferredSize(new Dimension(width, DEFAULT_FIELD_HEIGHT));
  }
}
