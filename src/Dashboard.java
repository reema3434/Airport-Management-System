import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Dashboard GUI Class
 * Technical Architecture for the Airport Management System Control Panel.
 * Handles responsive layout scaling, dynamic role abstraction, and session management.
 */
public class Dashboard extends javax.swing.JFrame {

   private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dashboard.class.getName());

   private Manager currentManager;

   private JTable scheduleTable;
   private DefaultTableModel tableModel;
   private JScrollPane scrollPane;
   private JPanel staffTablePanel;

   public Dashboard() {
       initComponents();
       setupStaffTable();
       styleDashboardComponents();
   }

   public Dashboard(Manager manager) {
       this.currentManager = manager;
       initComponents();
       setupStaffTable();
       styleDashboardComponents();
       applyManagerPrivileges();
   }

   /**
    * Initializes and builds the dynamic schedule view container for standard personnel.
    */
   private void setupStaffTable() {
       staffTablePanel = new JPanel(new BorderLayout());
       staffTablePanel.setBackground(Color.WHITE);
       staffTablePanel.setBorder(BorderFactory.createTitledBorder(
           BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
           " My Personal Flight Duty Schedule ", TitledBorder.LEFT, TitledBorder.TOP,
           new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80)
       ));

       tableModel = new DefaultTableModel(new Object[]{
           "Flight ID", "Airport ID", "Flight Num", "Origin", "Destination", "Departure", "Arrival", "Status"
       }, 0) {
           @Override
           public boolean isCellEditable(int row, int column) { return false; }
       };

       scheduleTable = new JTable(tableModel);
       styleTable(scheduleTable);

       scrollPane = new JScrollPane(scheduleTable);
       scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       staffTablePanel.add(scrollPane, BorderLayout.CENTER);

       centerPanel.add(staffTablePanel, "cardTable");
       staffTablePanel.setVisible(false);
   }

   /**
    * Role abstraction layer. Parses active security contexts, cleans up redundant corporate terminology,
    * and maps layout states dynamically to the active session parameters.
    */
   private void applyManagerPrivileges() {
       if (currentManager == null) return;

       String access = currentManager.getAccessLevel().toLowerCase();

       String cleanRole = currentManager.getRole();
       if (cleanRole != null) {
           cleanRole = cleanRole.trim();
           if (cleanRole.contains("Manager (") && cleanRole.endsWith(")")) {
               cleanRole = cleanRole.substring(cleanRole.indexOf("(") + 1, cleanRole.length() - 1);
           } else if (cleanRole.toLowerCase().startsWith("manager")) {
               cleanRole = cleanRole.replaceFirst("(?i)manager\\s*", "");
           }

        if (cleanRole.trim().isEmpty()) {
            cleanRole = currentManager.getRole();
        }
    } else {
        cleanRole = "Staff";
    }

    lblWelcome.setText("Welcome back, " + currentManager.getName());
    lblRole.setText("Role: " + cleanRole + " | Access Level: " + access.toUpperCase());
    setTitle("Airport Management System - " + cleanRole + " Dashboard");

    if (access.equals("basic")) {
        jButton1.setVisible(false);
        jButton2.setVisible(false);
        jButton3.setVisible(false);
        jButton4.setVisible(false);

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(staffTablePanel, BorderLayout.CENTER);
        staffTablePanel.setVisible(true);

        loadMyFlights();
    } else {
        staffTablePanel.setVisible(false);

        jButton1.setVisible(true);
        jButton2.setVisible(true);
        jButton3.setVisible(true);
        jButton4.setVisible(true);

        jButton1.setEnabled(currentManager.isCanManageFlights());
        jButton2.setEnabled(currentManager.isCanManageEmployees());
        jButton3.setEnabled(currentManager.isCanManagePassengers());
        jButton4.setEnabled(currentManager.isCanManageTickets());
    }

    topPanel.revalidate();
    topPanel.repaint();
    centerPanel.revalidate();
    centerPanel.repaint();
}

/**
 * Pulls active assignments mapped via standard relational table links.
 */
private void loadMyFlights() {
    String sql = "SELECT f.* FROM Flight f " +
                 "JOIN FlightEmployee fe ON f.flight_id = fe.flight_id AND f.airport_id = fe.airport_id " +
                 "WHERE fe.employee_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, currentManager.getId());
        try (ResultSet rs = pstmt.executeQuery()) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("flight_id"),
                    rs.getInt("airport_id"),
                    rs.getString("flight_number"),
                    rs.getString("origin"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getString("status")
                });
            }
        }
    } catch (SQLException ex) {
        System.out.println("Error loading staff flights: " + ex.getMessage());
    }
}

/**
 * Renders UI styling specifications, overrides legacy window matrices, and optimizes viewports.
 */
private void styleDashboardComponents() {
    setSize(850, 620);
    setLocationRelativeTo(null);
    setResizable(false);
    getContentPane().setBackground(new Color(245, 247, 250));

    topPanel.setBackground(new Color(44, 62, 80));
    topPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

    lblSystemTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblSystemTitle.setForeground(new Color(52, 152, 219));

    lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblWelcome.setForeground(Color.WHITE);

    lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    lblRole.setForeground(new Color(189, 195, 199));

    centerPanel.removeAll();
    centerPanel.setLayout(new GridLayout(2, 2, 25, 25));
    centerPanel.setBackground(new Color(245, 247, 250));
    centerPanel.setBorder(new EmptyBorder(25, 45, 25, 45));

    centerPanel.add(jButton1);
    centerPanel.add(jButton2);
    centerPanel.add(jButton3);
    centerPanel.add(jButton4);

    styleMenuButton(jButton1, new Color(52, 152, 219));
    styleMenuButton(jButton2, new Color(46, 204, 113));
    styleMenuButton(jButton3, new Color(155, 89, 182));
    styleMenuButton(jButton4, new Color(230, 126, 34));

    bottomPanel.removeAll();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
    bottomPanel.setBackground(new Color(245, 247, 250));
    bottomPanel.setBorder(new EmptyBorder(5, 45, 25, 45));

    bottomPanel.add(jButton5);

    jButton5.setFont(new Font("Segoe UI", Font.BOLD, 14));
    jButton5.setForeground(Color.BLACK);
    jButton5.setBackground(new Color(231, 76, 60));
    jButton5.setFocusPainted(false);
    jButton5.setCursor(new Cursor(Cursor.HAND_CURSOR));

    jButton5.setPreferredSize(new Dimension(200, 45));
    jButton5.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43), 1, true));
}

private void styleMenuButton(JButton button, Color accentColor) {
    button.setFont(new Font("Segoe UI", Font.BOLD, 16));
    button.setForeground(Color.BLACK);
    button.setBackground(accentColor);
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(accentColor.darker(), 1, true),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
}

private void styleTable(JTable table) {
    table.setRowHeight(25);
    table.setSelectionBackground(new Color(52, 152, 219, 40));
    table.setSelectionForeground(Color.BLACK);
    table.setGridColor(new Color(236, 240, 241));
    JTableHeader header = table.getTableHeader();
    header.setBackground(new Color(220, 225, 230));
    header.setForeground(Color.BLACK);
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
}

@SuppressWarnings("unchecked")
// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
private void initComponents() {

    topPanel = new javax.swing.JPanel();
    lblWelcome = new javax.swing.JLabel();
    lblRole = new javax.swing.JLabel();
    lblSystemTitle = new javax.swing.JLabel();
    centerPanel = new javax.swing.JPanel();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();
    jButton4 = new javax.swing.JButton();
    bottomPanel = new javax.swing.JPanel();
    jButton5 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    topPanel.setLayout(new java.awt.GridLayout(3, 1, 0, 4));

    lblWelcome.setText("Welcome back...");
    topPanel.add(lblWelcome);

    lblRole.setText("Role: Loading...");

        topPanel.add(lblRole);

        lblSystemTitle.setText("AIRPORT CONTROL CENTER");
        topPanel.add(lblSystemTitle);

        getContentPane().add(topPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setLayout(new java.awt.GridLayout(2, 2, 20, 20));

        jButton1.setText("Manage Flights");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        centerPanel.add(jButton1);

        jButton2.setText("Manage Employees");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        centerPanel.add(jButton2);

        jButton3.setText("Manage Passengers");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        centerPanel.add(jButton3);

        jButton4.setText("Manage Tickets");
        jButton4.addActionListener(this::jButton4ActionPerformed);
        centerPanel.add(jButton4);

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        bottomPanel.setLayout(new java.awt.BorderLayout());

        jButton5.setText("Exit System");
        jButton5.addActionListener(this::jButton5ActionPerformed);
        bottomPanel.add(jButton5, java.awt.BorderLayout.LINE_END);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        FlightManagementPage flightPage = new FlightManagementPage();
        flightPage.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        StaffManagementPage staffPage = new StaffManagementPage();
        staffPage.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        PassengerManagementPage passengerPage = new PassengerManagementPage();
        passengerPage.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        TicketManagementPage ticketPage = new TicketManagementPage();
        ticketPage.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit the system?", "Exit Confirmation", JOptionPane.
YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSystemTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}

