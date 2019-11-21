package eu.sanjin.kurelic.mvcgenerator.view;

import eu.sanjin.kurelic.mvcgenerator.view.ui.MainView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Application {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Application::showWindow);
  }

  private static void showWindow() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
      // use swing default style
    }
    new MainView().setVisible(true);
  }
}
