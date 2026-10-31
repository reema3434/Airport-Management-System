import java.awt.*;
import java.io.PrintWriter;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * StaffManagementPage Class
 * Technical implementation of the personnel registry and duty allocation node.
 * Integrates directly with Employee, Staff, and FlightEmployee relational schemas.
 */
public class StaffManagementPage extends JFrame {

   private JTextField txtEmpID, txtName, txtEmail, txtPhone, txtUsername, txtPassword;
   private JComboBox<String> cmbPosition, cmbAccessLevel;
   private JTable staffTable;
   private DefaultTableModel staffModel;

   private JTextField txtAssignEmpID, txtAssignFlightID, txtAssignAirportID;
   private JTable assignTable;
   private DefaultTableModel assignModel;

   public StaffManagementPage() {
       setTitle("Airport Management System - Operations & Staff Control");
       setSize(1000, 600);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       setLocationRelativeTo(null);
       setLayout(new BorderLayout());

       JTabbedPane tabs = new JTabbedPane();
       tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

       tabs.addTab("# Staff Management", buildStaffPanel());
       tabs.addTab("✈ Flight Assignments", buildAssignmentPanel());

       add(tabs, BorderLayout.CENTER);

       loadStaff();
       loadAssignments();
   }

   private JPanel buildStaffPanel() {
       JPanel panel = new JPanel(new BorderLayout(15, 15));
       panel.setBackground(new Color(245, 247, 250));
       panel.setBorder(new EmptyBorder(15, 15, 15, 15));

       JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));
       form.setBackground(Color.WHITE);
       form.setBorder(BorderFactory.createCompoundBorder(
           BorderFactory.createTitledBorder(
               BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
               " Staff Personal & Secure Account Information ", TitledBorder.LEFT, TitledBorder.TOP,
               new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80)
           ),
           new EmptyBorder(10, 15, 15, 15)
       ));

       Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
       Color textColor = new Color(52, 73, 94);

       form.add(createStyledLabel("Employee ID (Primary Key):", labelFont, textColor));
       txtEmpID = createStyledTextField(); form.add(txtEmpID);

       form.add(createStyledLabel("Full Name:", labelFont, textColor));
       txtName = createStyledTextField(); form.add(txtName);

       form.add(createStyledLabel("Email Address:", labelFont, textColor));
       txtEmail = createStyledTextField(); form.add(txtEmail);

       form.add(createStyledLabel("Phone Number:", labelFont, textColor));
       txtPhone = createStyledTextField(); form.add(txtPhone);

       form.add(createStyledLabel("Assigned Position:", labelFont, textColor));
       cmbPosition = new JComboBox<>(new String[]{
           "Manager", "Pilot", "Flight Attendant", "Ground Crew", "Assistant", "Booking Crew", "Gate Agent", "Security"
       });
       cmbPosition.setFont(new Font("Segoe UI", Font.PLAIN, 13));
       form.add(cmbPosition);

       form.add(createStyledLabel("Account Username (Unique):", labelFont, textColor));
       txtUsername = createStyledTextField(); form.add(txtUsername);

       form.add(createStyledLabel("Account Password:", labelFont, textColor));

    txtPassword = createStyledTextField(); form.add(txtPassword);

    form.add(createStyledLabel("System Access Privilege:", labelFont, textColor));
    cmbAccessLevel = new JComboBox<>(new String[]{"full", "supervisor", "basic"});
    cmbAccessLevel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    form.add(cmbAccessLevel);

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    btnRow.setBackground(new Color(245, 247, 250));

    JButton btnSave = createStyledButton("Save Staff Account", new Color(46, 204, 113));
    JButton btnUpdate = createStyledButton("Update Info", new Color(52, 152, 219));
    JButton btnDelete = createStyledButton("Delete Staff", new Color(231, 76, 60));
    JButton btnClear = createStyledButton("Clear Fields", new Color(241, 196, 15));
    JButton btnExport = createStyledButton("Export Record", new Color(149, 165, 166));

    btnRow.add(btnSave); btnRow.add(btnUpdate); btnRow.add(btnDelete); btnRow.add(btnClear); btnRow.add(btnExport);

    JPanel topContainer = new JPanel(new BorderLayout(10, 10));
    topContainer.setBackground(new Color(245, 247, 250));
    topContainer.add(form, BorderLayout.CENTER);
    topContainer.add(btnRow, BorderLayout.SOUTH);

    staffModel = new DefaultTableModel(new Object[]{"Employee ID", "Name", "Email", "Phone", "Position"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    staffTable = new JTable(staffModel);
    styleTable(staffTable);

    staffTable.getSelectionModel().addListSelectionListener(e -> {
        int r = staffTable.getSelectedRow();
        if (r >= 0 && !e.getValueIsAdjusting()) {
            String empIdStr = staffModel.getValueAt(r, 0).toString();

              txtEmpID.setText(empIdStr);
              txtName.setText(staffModel.getValueAt(r, 1).toString());
              txtEmail.setText(staffModel.getValueAt(r, 2).toString());
              txtPhone.setText(staffModel.getValueAt(r, 3).toString());
              cmbPosition.setSelectedItem(staffModel.getValueAt(r, 4).toString());

              String sqlFetchDetails = "SELECT username, password, access_level FROM Employee WHERE employee_id = ?";
              try (Connection conn = DBConnection.getConnection();
                   PreparedStatement pstmt = conn.prepareStatement(sqlFetchDetails)) {

                 pstmt.setInt(1, Integer.parseInt(empIdStr));
                 try (ResultSet rs = pstmt.executeQuery()) {
                     if (rs.next()) {
                         txtUsername.setText(rs.getString("username"));
                         txtPassword.setText(rs.getString("password"));

                         String accessLevel = rs.getString("access_level");
                         if (accessLevel != null) {
                             cmbAccessLevel.setSelectedItem(accessLevel.toLowerCase().trim());
                         }
                      }
                  }
              } catch (SQLException ex) {
                  System.out.println("Error fetching secure details: " + ex.getMessage());
              }
          }
    });

    panel.add(topContainer, BorderLayout.NORTH);
    panel.add(new JScrollPane(staffTable), BorderLayout.CENTER);

    btnSave.addActionListener(e -> saveStaff());
    btnUpdate.addActionListener(e -> updateStaff());
    btnDelete.addActionListener(e -> deleteStaff());
    btnClear.addActionListener(e -> clearStaffFields());
    btnExport.addActionListener(e -> exportStaffToText());

    return panel;
}

private JPanel buildAssignmentPanel() {
    JPanel panel = new JPanel(new BorderLayout(15, 15));
    panel.setBackground(new Color(245, 247, 250));
    panel.setBorder(new EmptyBorder(15, 15, 15, 15));

    JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
    form.setBackground(Color.WHITE);
    form.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
            " Assign Operational Duty (FlightEmployee Table Linkage) ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13), new Color(44, 62, 80)

          ),
          new EmptyBorder(15, 15, 15, 15)
    ));

    Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
    Color textColor = new Color(52, 73, 94);

    form.add(createStyledLabel("Employee ID (FK → Employee):", labelFont, textColor));
    txtAssignEmpID = createStyledTextField(); form.add(txtAssignEmpID);

    form.add(createStyledLabel("Flight ID (Composite FK 1 → Flight):", labelFont, textColor));
    txtAssignFlightID = createStyledTextField(); form.add(txtAssignFlightID);

    form.add(createStyledLabel("Airport ID (Composite FK 2 → Flight):", labelFont, textColor));
    txtAssignAirportID = createStyledTextField(); form.add(txtAssignAirportID);

    JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    btnRow.setBackground(new Color(245, 247, 250));

    JButton btnAssign = createStyledButton("Assign to Flight", new Color(46, 204, 113));
    JButton btnRemove = createStyledButton("Remove Duty", new Color(231, 76, 60));
    JButton btnClear = createStyledButton("Clear Fields", new Color(241, 196, 15));
    JButton btnExport = createStyledButton("Export Summary", new Color(149, 165, 166));

    btnRow.add(btnAssign); btnRow.add(btnRemove); btnRow.add(btnClear); btnRow.add(btnExport);

    JPanel topContainer = new JPanel(new BorderLayout(10, 10));
    topContainer.setBackground(new Color(245, 247, 250));
    topContainer.add(form, BorderLayout.CENTER);
    topContainer.add(btnRow, BorderLayout.SOUTH);

    assignModel = new DefaultTableModel(new Object[]{"Employee ID", "Flight ID", "Airport ID"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    assignTable = new JTable(assignModel);
    styleTable(assignTable);

    assignTable.getSelectionModel().addListSelectionListener(e -> {
        int r = assignTable.getSelectedRow();
        if (r >= 0) {
            txtAssignEmpID.setText(assignModel.getValueAt(r, 0).toString());
            txtAssignFlightID.setText(assignModel.getValueAt(r, 1).toString());
            txtAssignAirportID.setText(assignModel.getValueAt(r, 2).toString());
        }
    });

    panel.add(topContainer, BorderLayout.NORTH);
    panel.add(new JScrollPane(assignTable), BorderLayout.CENTER);

    btnAssign.addActionListener(e -> saveAssignment());
    btnRemove.addActionListener(e -> deleteAssignment());
    btnClear.addActionListener(e -> clearAssignFields());
    btnExport.addActionListener(e -> exportAssignmentToText());

    return panel;
}

private JLabel createStyledLabel(String text, Font font, Color color) {
    JLabel label = new JLabel(text);
    label.setFont(font);
    label.setForeground(color);
    return label;
}

private JTextField createStyledTextField() {
    JTextField field = new JTextField();
    field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
        BorderFactory.createEmptyBorder(4, 6, 4, 6)
    ));
    return field;
}

private JButton createStyledButton(String text, Color baseColor) {
    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 13));
    button.setForeground(Color.BLACK);
    button.setBackground(baseColor);
    button.setFocusPainted(false);
    button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return button;
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

    private void saveStaff() {
        if (txtEmpID.getText().isEmpty() || txtName.getText().isEmpty() ||
            txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields including Username and Password are required.", "Validation Error", JOptionPane.
WARNING_MESSAGE);
            return;
        }
        if (!isInt(txtEmpID.getText())) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a numeric number.", "Data Type Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtEmpID.getText());
        if (employeeExists(id)) {
            JOptionPane.showMessageDialog(this, "Employee ID already exists in system.", "Conflict Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

         try (Connection conn = DBConnection.getConnection()) {
             conn.setAutoCommit(false);

            String sqlEmp = "INSERT INTO Employee (employee_id, name, role, email, phone, salary, department, username, password, access_level) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)";

             try (PreparedStatement ps1 = conn.prepareStatement(sqlEmp)) {
                 ps1.setInt(1, id);
                 ps1.setString(2, txtName.getText());
                 ps1.setString(3, cmbPosition.getSelectedItem().toString().equals("Pilot") ? "Manager" : "Staff");
                 ps1.setString(4, txtEmail.getText());
                 ps1.setString(5, txtPhone.getText());
                 ps1.setDouble(6, 6500.00);
                 ps1.setString(7, "Operations");
                 ps1.setString(8, txtUsername.getText());
                 ps1.setString(9, txtPassword.getText());
                 ps1.setString(10, cmbAccessLevel.getSelectedItem().toString());
                 ps1.executeUpdate();
             }

             String sqlStaff = "INSERT INTO Staff (employee_id, position) VALUES (?,?)";
             try (PreparedStatement ps2 = conn.prepareStatement(sqlStaff)) {
                 ps2.setInt(1, id);
                 ps2.setString(2, cmbPosition.getSelectedItem().toString());
                 ps2.executeUpdate();
             }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Real Account Created Successfully!\nYou can now login with this user immediately.", "Success"
, JOptionPane.INFORMATION_MESSAGE);
            refreshStaffTable();
            clearStaffFields();

         } catch (SQLException ex) {
             JOptionPane.showMessageDialog(this, "Database Transaction Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
         }
     }

     private void updateStaff() {
         if (txtEmpID.getText().isEmpty() || !isInt(txtEmpID.getText())) {
             JOptionPane.showMessageDialog(this, "Please enter or select a valid Employee ID to update.", "Warning", JOptionPane.WARNING_MESSAGE
);
             return;
         }
         int id = Integer.parseInt(txtEmpID.getText());
         if (!employeeExists(id)) {
             JOptionPane.showMessageDialog(this, "Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         try (Connection conn = DBConnection.getConnection()) {
             conn.setAutoCommit(false);

             String sqlEmp = "UPDATE Employee SET name=?, email=?, phone=?, username=?, password=?, access_level=? WHERE employee_id=?";
             try (PreparedStatement ps1 = conn.prepareStatement(sqlEmp)) {
                 ps1.setString(1, txtName.getText());
                 ps1.setString(2, txtEmail.getText());
                 ps1.setString(3, txtPhone.getText());

               ps1.setString(4, txtUsername.getText());
               ps1.setString(5, txtPassword.getText());
               ps1.setString(6, cmbAccessLevel.getSelectedItem().toString());
               ps1.setInt(7, id);
               ps1.executeUpdate();
           }

           String sqlStaff = "UPDATE Staff SET position=? WHERE employee_id=?";
           try (PreparedStatement ps2 = conn.prepareStatement(sqlStaff)) {
               ps2.setString(1, cmbPosition.getSelectedItem().toString());
               ps2.setInt(2, id);
               ps2.executeUpdate();
           }

           conn.commit();
           JOptionPane.showMessageDialog(this, "Staff updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
           refreshStaffTable();
           clearStaffFields();

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
       }
   }

    private void deleteStaff() {
        if (txtEmpID.getText().isEmpty() || !isInt(txtEmpID.getText())) {
            JOptionPane.showMessageDialog(this, "Select a staff record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtEmpID.getText());
        if (!employeeExists(id)) {
            JOptionPane.showMessageDialog(this, "Record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Delete Staff ID " + id + " and all their active flight duties?", "Confirm Cascaded Delete", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

       try (Connection conn = DBConnection.getConnection()) {
           conn.setAutoCommit(false);

           try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM FlightEmployee WHERE employee_id=?")) {
               ps1.setInt(1, id); ps1.executeUpdate();
           }
           try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM Staff WHERE employee_id=?")) {
               ps2.setInt(1, id); ps2.executeUpdate();
           }
           try (PreparedStatement ps3 = conn.prepareStatement("DELETE FROM Employee WHERE employee_id=?")) {
               ps3.setInt(1, id); ps3.executeUpdate();
           }

           conn.commit();
           JOptionPane.showMessageDialog(this, "Staff record completely deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
           refreshStaffTable();
           refreshAssignTable();
           clearStaffFields();

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Deletion Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
       }
   }

   private void loadStaff() {
       String sql = "SELECT e.employee_id, e.name, e.email, e.phone, IFNULL(s.position, e.role) AS position "
                  + "FROM Employee e LEFT JOIN Staff s ON e.employee_id = s.employee_id";

       try (Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(sql)) {

           while (rs.next()) {
               staffModel.addRow(new Object[]{
                   rs.getInt("employee_id"),
                   rs.getString("name"),
                   rs.getString("email"),
                   rs.getString("phone"),
                   rs.getString("position")
               });
           }
       } catch (SQLException ex) {
           System.out.println("Load Staff Log Error: " + ex.getMessage());
       }
   }

   private void exportStaffToText() {
       if (txtEmpID.getText().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Select a Staff record from table to export.", "Warning", JOptionPane.WARNING_MESSAGE);
           return;

        }
        Staff s = new Staff(
            Integer.parseInt(txtEmpID.getText()), txtName.getText(), txtEmail.getText(), txtPhone.getText(), cmbPosition.getSelectedItem().
toString()
        );

        String fileName = "Staff_Profile_ID_" + s.getId() + ".txt";
        try (PrintWriter w = new PrintWriter(fileName)) {
            w.println("=========================================");
            w.println("        ✈ AIRPORT OFFICIAL STAFF LOG     ");
            w.println("=========================================");
            w.println(" Employee ID : " + s.getId());
            w.println(" Full Name   : " + s.getName());
            w.println(" Email Link : " + s.getEmail());
            w.println(" Phone No    : " + s.getPhone());
            w.println(" Position    : " + s.getPosition());
            w.println(" Role Type   : " + s.getRole());
            w.println("=========================================");
            JOptionPane.showMessageDialog(this, "Staff profile exported to text file: " + fileName, "Export Done", JOptionPane.
INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export File Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAssignment() {
        if (txtAssignEmpID.getText().isEmpty() || txtAssignFlightID.getText().isEmpty() || txtAssignAirportID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All three IDs (Employee, Flight, Airport) are strongly required.", "Validation Error",
JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isInt(txtAssignEmpID.getText()) || !isInt(txtAssignFlightID.getText()) || !isInt(txtAssignAirportID.getText())) {
            JOptionPane.showMessageDialog(this, "IDs fields must contain numeric values.", "Type Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO FlightEmployee (employee_id, flight_id, airport_id) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtAssignEmpID.getText()));
            ps.setInt(2, Integer.parseInt(txtAssignFlightID.getText()));
            ps.setInt(3, Integer.parseInt(txtAssignAirportID.getText()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Duty Assigned Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAssignTable();
            clearAssignFields();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Assignment Database Error: " + ex.getMessage(), "DB Constraint Failure", JOptionPane.
ERROR_MESSAGE);
        }
    }

    private void deleteAssignment() {
        if (txtAssignEmpID.getText().isEmpty() || txtAssignFlightID.getText().isEmpty() || txtAssignAirportID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill or select full fields row to identify the exact duty assignment.", "Warning", JOptionPane
.WARNING_MESSAGE);
            return;
        }

        String sql = "DELETE FROM FlightEmployee WHERE employee_id=? AND flight_id=? AND airport_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtAssignEmpID.getText()));
            ps.setInt(2, Integer.parseInt(txtAssignFlightID.getText()));
            ps.setInt(3, Integer.parseInt(txtAssignAirportID.getText()));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Flight assignment duty removed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshAssignTable();
            clearAssignFields();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Removal Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAssignments() {
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM FlightEmployee")) {

            while (rs.next()) {
                assignModel.addRow(new Object[]{
                    rs.getInt("employee_id"), rs.getInt("flight_id"), rs.getInt("airport_id")

                });
            }
        } catch (SQLException ex) {
            System.out.println("Load Assignments Error: " + ex.getMessage());
        }
    }

    private void exportAssignmentToText() {
        if (txtAssignEmpID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an active assignment duty row from the table first.", "Warning", JOptionPane.
WARNING_MESSAGE);
            return;
        }
        FlightEmployee fa = new FlightEmployee(
            Integer.parseInt(txtAssignEmpID.getText()),
            Integer.parseInt(txtAssignFlightID.getText().isEmpty() ? "0" : txtAssignFlightID.getText()),
            Integer.parseInt(txtAssignAirportID.getText().isEmpty() ? "0" : txtAssignAirportID.getText())
        );

        String fileName = "Assignment_Summary_EmpNo_" + fa.getEmployeeID() + ".txt";
        try (PrintWriter w = new PrintWriter(fileName)) {
            w.println(fa.getAssignmentSummary());
            JOptionPane.showMessageDialog(this, "Duty summary report successfully exported to: " + fileName, "Export Complete", JOptionPane.
INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "File Exporting Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean employeeExists(int id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT employee_id FROM Employee WHERE employee_id=?")) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (Exception e) { return false; }
    }

    private boolean isInt(String s) {
        try { Integer.parseInt(s); return true; } catch (Exception e) { return false; }
    }

    private void refreshStaffTable() { staffModel.setRowCount(0); loadStaff(); }
    private void refreshAssignTable() { assignModel.setRowCount(0); loadAssignments(); }

    private void clearStaffFields() {
        txtEmpID.setText(""); txtName.setText(""); txtEmail.setText(""); txtPhone.setText("");
        txtUsername.setText(""); txtPassword.setText(""); cmbPosition.setSelectedIndex(0); cmbAccessLevel.setSelectedIndex(0);
        staffTable.clearSelection();
    }

    private void clearAssignFields() {
        txtAssignEmpID.setText(""); txtAssignFlightID.setText(""); txtAssignAirportID.setText("");
        assignTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StaffManagementPage::new);
    }
}

