package eu.sanjin.kurelic.mvcgenerator.view.ui.util;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FontUtil {

  private static final String FONT_NAME = "font.ttf";
  private static final int DEFAULT_FONT_SIZE = 14;

  private static Font font = null;

  private static void loadFont() {
    try {
      InputStream is = FontUtil.class.getClassLoader().getResourceAsStream(FONT_NAME);
      if (Objects.isNull(is)) {
        throw new IOException("File not found");
      }
      font = Font.createFont(Font.TRUETYPE_FONT, is);
    } catch (FontFormatException | IOException e) {
      // use default font
      System.err.println(e.getMessage());
      font = new JLabel().getFont();
    }
  }

  public static Font getFont(int fontSize) {
    if (font == null) {
      loadFont();
    }
    return font.deriveFont(Font.PLAIN, fontSize);
  }

  public static Font getFont() {
    return getFont(DEFAULT_FONT_SIZE);
  }
}
