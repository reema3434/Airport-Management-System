import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {

                  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
              } catch (Exception e) {

                  System.out.println("[System Info] Custom Look and Feel could not be loaded: " + e.getMessage());
              }

              LoginPage loginPage = new LoginPage();



              loginPage.setLocationRelativeTo(null);



              loginPage.setVisible(true);

              System.out.println("[System] Airport Management System initialized successfully.");
        });
    }
}

