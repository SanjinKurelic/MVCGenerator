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
    new MainView().setVisible(true);
  }
}
