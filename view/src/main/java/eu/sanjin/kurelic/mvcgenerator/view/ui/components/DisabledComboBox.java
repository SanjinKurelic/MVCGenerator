package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontLoader;

import javax.swing.JComboBox;
import java.awt.Dimension;

public class DisabledComboBox extends JComboBox<String> {

  private static final int DEFAULT_COMBO_WIDTH = 180;
  private static final int DEFAULT_COMBO_HEIGHT = 24;

  public DisabledComboBox(String selectedItem) {
    super(new String[]{selectedItem});
    setEnabled(false);
    setStyle();
  }

  private void setStyle() {
    setPreferredSize(new Dimension(DEFAULT_COMBO_WIDTH, DEFAULT_COMBO_HEIGHT));
    setFont(FontLoader.getFont());
  }
}
