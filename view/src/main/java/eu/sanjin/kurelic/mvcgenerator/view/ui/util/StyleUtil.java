package eu.sanjin.kurelic.mvcgenerator.view.ui.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StyleUtil {

  public static Color getBackgroundColor() {
    return Color.decode("#FAFAFA");
  }

  public static Color getTextColor() {
    return Color.decode("#424242");
  }

  public static Color getFocusedColor() {
    return Color.decode("#BEBEBE");
  }

  public static Border getBorder() {
    return BorderFactory.createLineBorder(getTextColor());
  }
}
