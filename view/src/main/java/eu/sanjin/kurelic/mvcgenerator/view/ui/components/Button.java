package eu.sanjin.kurelic.mvcgenerator.view.ui.components;

import eu.sanjin.kurelic.mvcgenerator.view.ui.util.FontUtil;
import eu.sanjin.kurelic.mvcgenerator.view.ui.util.StyleUtil;

import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

  private static final int DEFAULT_BUTTON_HEIGHT = 26;
  private static final int DEFAULT_BUTTON_WIDTH = 100;

  public Button(String text) {
    this(text, DEFAULT_BUTTON_WIDTH);
  }

  Button(String text, int width) {
    super(text);
    setStyle(width);
  }

  private void setStyle(int width) {
    setPreferredSize(new Dimension(width, DEFAULT_BUTTON_HEIGHT));
    setBackground(StyleUtil.getBackgroundColor());
    setForeground(StyleUtil.getTextColor());
    setBorder(StyleUtil.getBorder());
    setFocusPainted(false);
    setFont(FontUtil.getFont());

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        setBackground(StyleUtil.getFocusedColor());
        setCursor(new Cursor(Cursor.HAND_CURSOR));
      }

      @Override
      public void mouseExited(MouseEvent e) {
        setBackground(StyleUtil.getBackgroundColor());
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      }
    });
  }
}
