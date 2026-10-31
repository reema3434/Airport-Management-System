import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * LoginPage GUI Class
 * Technical implementation of the system authentication node.
 * Establishes real-time connection wrappers to validate active relational database records.
 */
public class LoginPage extends javax.swing.JFrame {

   private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginPage.class.getName());

   public LoginPage() {
       initComponents();
       styleLoginComponents();
   }

   /**
    * Renders UI styling specifications, structural spacing parameters, and baseline themes.
    */
   private void styleLoginComponents() {
       setSize(450, 350);
       setLocationRelativeTo(null);
       setResizable(false);
       getContentPane().setBackground(new Color(245, 247, 250));

       topPanel.setBackground(new Color(44, 62, 80));
       topPanel.setBorder(new EmptyBorder(15, 10, 15, 10));
       jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 22));
       jLabel1.setForeground(Color.WHITE);
       jLabel1.setHorizontalAlignment(SwingConstants.CENTER);

       centerPanel.setBackground(new Color(245, 247, 250));
       centerPanel.setBorder(new EmptyBorder(25, 40, 20, 40));

       jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 13));
       jLabel2.setForeground(new Color(44, 62, 80));

       jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 13));
       jLabel3.setForeground(new Color(44, 62, 80));

       jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
       jPasswordField1.setFont(new Font("Segoe UI", Font.PLAIN, 14));

       jButton1.setFont(new Font("Segoe UI", Font.BOLD, 14));
       jButton1.setForeground(Color.BLACK);
       jButton1.setBackground(new Color(52, 152, 219));
       jButton1.setFocusPainted(false);
       jButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
       jButton1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
   }

   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

       topPanel = new javax.swing.JPanel();
       jLabel1 = new javax.swing.JLabel();
       centerPanel = new javax.swing.JPanel();
       jLabel2 = new javax.swing.JLabel();
       jTextField1 = new javax.swing.JTextField();
       jLabel3 = new javax.swing.JLabel();
       jPasswordField1 = new javax.swing.JPasswordField();
       jButton1 = new javax.swing.JButton();

       setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       setTitle("Airport Management System - Login Page");

       topPanel.setLayout(new java.awt.BorderLayout());

       jLabel1.setText("AIRPORT CONTROL SYSTEM");
       topPanel.add(jLabel1, java.awt.BorderLayout.CENTER);

       getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

       centerPanel.setLayout(new java.awt.GridLayout(5, 1, 0, 8));

       jLabel2.setText("Username");
       centerPanel.add(jLabel2);
       centerPanel.add(jTextField1);

       jLabel3.setText("Password");
       centerPanel.add(jLabel3);
       centerPanel.add(jPasswordField1);

    jButton1.setText("Login");
    jButton1.setForeground(Color.BLACK);
    jButton1.addActionListener(this::jButton1ActionPerformed);
    centerPanel.add(jButton1);

    getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

    pack();
}// </editor-fold>//GEN-END:initComponents

/**
 * Executes asynchronous database verification utilizing case-sensitive lookup mechanics.
 */
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    String inputUser = jTextField1.getText().trim();
    String inputPass = new String(jPasswordField1.getPassword()).trim();

    if (inputUser.isEmpty() || inputPass.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter both Username and Password!", "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    String sql = "SELECT * FROM Employee WHERE username = BINARY ? AND password = BINARY ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, inputUser);
        pstmt.setString(2, inputPass);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int id = rs.getInt("employee_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                double salary = rs.getDouble("salary");
                String department = rs.getString("department");

                String realRole = rs.getString("role");
                String accessLevel = rs.getString("access_level");

                JOptionPane.showMessageDialog(this, "Welcome back, " + name + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);

                Manager loggedInManager = new Manager(id, name, email, phone, salary, department, realRole, 5, accessLevel);

                Dashboard dashboard = new Dashboard(loggedInManager);
                dashboard.setVisible(true);

                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database Connection Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        logger.log(java.util.logging.Level.SEVERE, "Login processing database error", ex);
    }
}//GEN-LAST:event_jButton1ActionPerformed

public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(() -> new LoginPage().setVisible(true));
}

// Variables declaration - do not modify//GEN-BEGIN:variables
private javax.swing.JPanel centerPanel;
private javax.swing.JButton jButton1;
private javax.swing.JLabel jLabel1;
private javax.swing.JLabel jLabel2;
private javax.swing.JLabel jLabel3;
private javax.swing.JPasswordField jPasswordField1;
private javax.swing.JTextField jTextField1;
private javax.swing.JPanel topPanel;

    // End of variables declaration//GEN-END:variables
}

