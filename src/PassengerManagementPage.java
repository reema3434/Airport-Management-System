import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PassengerManagementPage extends JFrame {
    private JTextField txtPassengerID, txtPassport, txtName, txtPhone, txtEmail;
    private JTable passengerTable;
    private DefaultTableModel passengerModel;

      public PassengerManagementPage() {
          setTitle("Passenger Management Directory");
          setSize(1000, 600);
          setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          setLocationRelativeTo(null);
          setLayout(new BorderLayout(15, 15));
          getContentPane().setBackground(new Color(245, 247, 250));

          JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
          formPanel.setBackground(Color.WHITE);
          formPanel.setBorder(BorderFactory.createCompoundBorder(
              BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
                  " Passenger Personal Profiles ", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80
)),
                new EmptyBorder(15, 15, 15, 15)
          ));

          Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
          Color textColor = new Color(52, 73, 94);

          formPanel.add(createStyledLabel("Passenger ID (PK):", labelFont, textColor));
          txtPassengerID = createStyledTextField(); formPanel.add(txtPassengerID);
          formPanel.add(createStyledLabel("Passport Number:", labelFont, textColor));
          txtPassport = createStyledTextField(); formPanel.add(txtPassport);
          formPanel.add(createStyledLabel("Full Name:", labelFont, textColor));
          txtName = createStyledTextField(); formPanel.add(txtName);
          formPanel.add(createStyledLabel("Phone Number:", labelFont, textColor));
          txtPhone = createStyledTextField(); formPanel.add(txtPhone);
          formPanel.add(createStyledLabel("Email Address:", labelFont, textColor));
          txtEmail = createStyledTextField(); formPanel.add(txtEmail);

          JPanel leftContainer = new JPanel(new BorderLayout(10, 10));
          leftContainer.setBackground(new Color(245, 247, 250));
          leftContainer.setBorder(new EmptyBorder(10, 15, 15, 5));
          leftContainer.add(formPanel, BorderLayout.CENTER);

          JPanel btnRow = new JPanel(new GridLayout(4, 1, 5, 8));
          btnRow.setBackground(new Color(245, 247, 250));
          JButton btnSave = createStyledButton("Save Passenger", new Color(46, 204, 113));
          JButton btnUpdate = createStyledButton("Update Profile", new Color(52, 152, 219));
          JButton btnDelete = createStyledButton("Delete Passenger", new Color(231, 76, 60));
          JButton btnClear = createStyledButton("Clear Fields", new Color(241, 196, 15));
          btnRow.add(btnSave); btnRow.add(btnUpdate); btnRow.add(btnDelete); btnRow.add(btnClear);
          leftContainer.add(btnRow, BorderLayout.SOUTH);

          passengerModel = new DefaultTableModel(new Object[]{"Passenger ID", "Passport Number", "Name", "Phone", "Email"}, 0) {
              @Override public boolean isCellEditable(int r, int c) { return false; }
          };
          passengerTable = new JTable(passengerModel);
          styleTable(passengerTable);

          passengerTable.getSelectionModel().addListSelectionListener(e -> {
              int r = passengerTable.getSelectedRow();
              if (r >= 0) {
                  txtPassengerID.setText(passengerModel.getValueAt(r, 0).toString());
                  txtPassport.setText(passengerModel.getValueAt(r, 1).toString());
                  txtName.setText(passengerModel.getValueAt(r, 2).toString());
                  txtPhone.setText(passengerModel.getValueAt(r, 3).toString());
                  txtEmail.setText(passengerModel.getValueAt(r, 4).toString());
              }
          });

          add(leftContainer, BorderLayout.WEST);
          add(new JScrollPane(passengerTable), BorderLayout.CENTER);

          btnSave.addActionListener(e -> savePassenger());
          btnUpdate.addActionListener(e -> updatePassenger());
          btnDelete.addActionListener(e -> deletePassenger());
          btnClear.addActionListener(e -> clearFields());

          loadPassengers();
      }

    private JLabel createStyledLabel(String t, Font f, Color c) { JLabel l = new JLabel(t); l.setFont(f); l.setForeground(c); return l; }
    private JTextField createStyledTextField() { JTextField f = new JTextField(); f.setFont(new Font("Segoe UI", Font.PLAIN, 13)); f.setBorder(
BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true), BorderFactory.createEmptyBorder(4, 6, 4,
6))); return f; }
    private JButton createStyledButton(String t, Color bg) { JButton b = new JButton(t); b.setFont(new Font("Segoe UI", Font.BOLD, 12)); b.
setForeground(Color.BLACK); b.setBackground(bg); b.setFocusPainted(false); b.setCursor(new Cursor(Cursor.HAND_CURSOR)); b.setBorder(
BorderFactory.createEmptyBorder(10, 10, 10, 10)); return b; }
    private void styleTable(JTable t) { t.setRowHeight(25); t.setSelectionBackground(new Color(52, 152, 219, 40)); t.setSelectionForeground(
Color.BLACK); t.setGridColor(new Color(236, 240, 241)); JTableHeader h = t.getTableHeader(); h.setBackground(new Color(220, 225, 230)); h.
setForeground(Color.BLACK); h.setFont(new Font("Segoe UI", Font.BOLD, 13)); }

    private void loadPassengers() {
        passengerModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection(); ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Passenger")) {
            while (rs.next()) { passengerModel.addRow(new Object[]{rs.getInt("passenger_id"), rs.getString("passport_number"), rs.getString(
"name"), rs.getString("phone"), rs.getString("email")}); }
        } catch (Exception ex) { System.out.println(ex.getMessage()); }
    }

    private void savePassenger() {
        String sql = "INSERT INTO Passenger (passenger_id, passport_number, name, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(txtPassengerID.getText())); pstmt.setString(2, txtPassport.getText()); pstmt.setString(3, txtName.
getText()); pstmt.setString(4, txtPhone.getText()); pstmt.setString(5, txtEmail.getText());
            pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "Passenger Profiles Saved."); loadPassengers();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void updatePassenger() {
        String sql = "UPDATE Passenger SET passport_number=?, name=?, phone=?, email=? WHERE passenger_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, txtPassport.getText()); pstmt.setString(2, txtName.getText()); pstmt.setString(3, txtPhone.getText()); pstmt.
setString(4, txtEmail.getText()); pstmt.setInt(5, Integer.parseInt(txtPassengerID.getText()));
            pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "Passenger Profile Updated."); loadPassengers();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deletePassenger() {
        int ok = JOptionPane.showConfirmDialog(this, "Deleting passenger will clear their active tickets. Continue?", "Confirm Delete",
JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM Ticket WHERE passenger_id=?")) { p1.setInt(1, Integer.parseInt
(txtPassengerID.getText())); p1.executeUpdate(); }
            try (PreparedStatement p2 = conn.prepareStatement("DELETE FROM Passenger WHERE passenger_id=?")) { p2.setInt(1, Integer.parseInt
(txtPassengerID.getText())); p2.executeUpdate(); }
            conn.commit(); JOptionPane.showMessageDialog(this, "Passenger Completely Removed."); loadPassengers();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void clearFields() {
        txtPassengerID.setText(""); txtPassport.setText(""); txtName.setText(""); txtPhone.setText(""); txtEmail.setText(""); passengerTable.
clearSelection();
    }
}

