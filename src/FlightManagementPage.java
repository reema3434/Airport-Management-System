import java.awt.*;
import java.io.PrintWriter;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * FlightManagementPage GUI Class
 * Technical implementation of the flight tracking and control administration node.
 * Integrates directly with relational schema models via atomic transaction wrappers.
 */
public class FlightManagementPage extends JFrame {

   private JTextField txtFlightID, txtAirportID, txtFlightNum, txtOrigin, txtDestination, txtDeparture, txtArrival, txtStatus;
   private JTable dataTable;
   private DefaultTableModel model;

   public FlightManagementPage() {
       setTitle("Airport Management System - Flight Control Panel");
       setSize(1000, 600);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       setLocationRelativeTo(null);
       setLayout(new BorderLayout(15, 15));
       getContentPane().setBackground(new Color(245, 247, 250));

       JPanel headerPanel = new JPanel();
       headerPanel.setBackground(new Color(44, 62, 80));
       headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
       JLabel lblTitle = new JLabel("✈ FLIGHT MANAGEMENT PANEL");
       lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
       lblTitle.setForeground(Color.WHITE);
       headerPanel.add(lblTitle);
       add(headerPanel, BorderLayout.NORTH);

       JPanel leftPanel = new JPanel(new BorderLayout(10, 15));
       leftPanel.setBackground(new Color(245, 247, 250));
       leftPanel.setBorder(new EmptyBorder(10, 15, 15, 5));

       JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 12));
       formPanel.setBackground(Color.WHITE);
       formPanel.setBorder(BorderFactory.createCompoundBorder(
           BorderFactory.createLineBorder(new Color(200, 215, 228), 1, true),
           new EmptyBorder(15, 15, 15, 15)
       ));

       Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
       Color textColor = new Color(52, 73, 94);

       formPanel.add(createStyledLabel("Flight ID (Primary Key):", labelFont, textColor));
       txtFlightID = createStyledTextField(); formPanel.add(txtFlightID);

       formPanel.add(createStyledLabel("Airport ID (Composite PK):", labelFont, textColor));
       txtAirportID = createStyledTextField(); formPanel.add(txtAirportID);

       formPanel.add(createStyledLabel("Flight Number:", labelFont, textColor));
       txtFlightNum = createStyledTextField(); formPanel.add(txtFlightNum);

       formPanel.add(createStyledLabel("Origin:", labelFont, textColor));
       txtOrigin = createStyledTextField(); formPanel.add(txtOrigin);

       formPanel.add(createStyledLabel("Destination:", labelFont, textColor));
       txtDestination = createStyledTextField(); formPanel.add(txtDestination);

       formPanel.add(createStyledLabel("Departure Time:", labelFont, textColor));
       txtDeparture = createStyledTextField(); txtDeparture.setToolTipText("YYYY-MM-DD HH:MM:SS"); formPanel.add(txtDeparture);

       formPanel.add(createStyledLabel("Arrival Time:", labelFont, textColor));
       txtArrival = createStyledTextField(); txtArrival.setToolTipText("YYYY-MM-DD HH:MM:SS"); formPanel.add(txtArrival);

       formPanel.add(createStyledLabel("Status:", labelFont, textColor));
       txtStatus = createStyledTextField(); formPanel.add(txtStatus);

       leftPanel.add(formPanel, BorderLayout.CENTER);

       JPanel actionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
       actionsPanel.setBackground(new Color(245, 247, 250));

       JButton btnSave = createStyledButton("Save Flight", new Color(46, 204, 113));
       JButton btnUpdate = createStyledButton("Update Flight", new Color(52, 152, 219));
       JButton btnDelete = createStyledButton("Delete Flight", new Color(231, 76, 60));
       JButton btnExport = createStyledButton("Export (TXT)", new Color(149, 165, 166));

       actionsPanel.add(btnSave);

    actionsPanel.add(btnUpdate);
    actionsPanel.add(btnDelete);
    actionsPanel.add(btnExport);

    leftPanel.add(actionsPanel, BorderLayout.SOUTH);
    add(leftPanel, BorderLayout.WEST);

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setBackground(Color.WHITE);
    tablePanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
        " Flights Database Records ", TitledBorder.LEFT, TitledBorder.TOP,
        new Font("Segoe UI", Font.BOLD, 14), new Color(44, 62, 80)
    ));

    model = new DefaultTableModel(new Object[]{
        "Flight ID", "Airport ID", "Flight No", "Origin", "Destination", "Departure", "Arrival", "Status"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    dataTable = new JTable(model);
    dataTable.setRowHeight(25);
    dataTable.setSelectionBackground(new Color(52, 152, 219, 40));
    dataTable.setSelectionForeground(Color.BLACK);
    dataTable.setGridColor(new Color(236, 240, 241));

    JTableHeader header = dataTable.getTableHeader();
    header.setBackground(new Color(220, 225, 230));
    header.setForeground(Color.BLACK);
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));

    JScrollPane scrollPane = new JScrollPane(dataTable);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    tablePanel.add(scrollPane, BorderLayout.CENTER);

    dataTable.getSelectionModel().addListSelectionListener(e -> {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow != -1) {
            txtFlightID.setText(model.getValueAt(selectedRow, 0).toString());
            txtAirportID.setText(model.getValueAt(selectedRow, 1).toString());
            txtFlightNum.setText(model.getValueAt(selectedRow, 2).toString());
            txtOrigin.setText(model.getValueAt(selectedRow, 3).toString());
            txtDestination.setText(model.getValueAt(selectedRow, 4).toString());
            txtDeparture.setText(model.getValueAt(selectedRow, 5).toString());
            txtArrival.setText(model.getValueAt(selectedRow, 6).toString());
            txtStatus.setText(model.getValueAt(selectedRow, 7).toString());
        }
    });

    add(tablePanel, BorderLayout.CENTER);

    btnSave.addActionListener(e -> saveFlight());
    btnUpdate.addActionListener(e -> updateFlight());
    btnDelete.addActionListener(e -> deleteFlight());
    btnExport.addActionListener(e -> exportFlightToText());

    loadFlights();
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
    button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return button;

     }

     private boolean isNumber(String value) {
         try { Integer.parseInt(value); return true; }
         catch (Exception e) { return false; }
     }

     private boolean flightExists(int flightId, int airportId) {
         String sql = "SELECT flight_id FROM Flight WHERE flight_id=? AND airport_id=?";
         try (Connection conn = DBConnection.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setInt(1, flightId);
             pstmt.setInt(2, airportId);
             ResultSet rs = pstmt.executeQuery();
             return rs.next();
         } catch (Exception ex) {
             return false;
         }
     }

     private void saveFlight() {
         if (txtFlightID.getText().isEmpty() || txtAirportID.getText().isEmpty() ||
             txtFlightNum.getText().isEmpty() || txtOrigin.getText().isEmpty() || txtDestination.getText().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Warning", JOptionPane.WARNING_MESSAGE);
             return;
         }

         if (!isNumber(txtFlightID.getText()) || !isNumber(txtAirportID.getText())) {
             JOptionPane.showMessageDialog(this, "Flight ID and Airport ID must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

         int flightID = Integer.parseInt(txtFlightID.getText());
         int airportID = Integer.parseInt(txtAirportID.getText());

        if (flightExists(flightID, airportID)) {
            JOptionPane.showMessageDialog(this, "Flight with this ID and Airport combination already exists.", "Conflict", JOptionPane.
ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO Flight (flight_id, airport_id, flight_number, origin, destination, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

         try (Connection conn = DBConnection.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setInt(1, flightID);
             pstmt.setInt(2, airportID);
             pstmt.setString(3, txtFlightNum.getText());
             pstmt.setString(4, txtOrigin.getText());
             pstmt.setString(5, txtDestination.getText());
             pstmt.setString(6, txtDeparture.getText());
             pstmt.setString(7, txtArrival.getText());
             pstmt.setString(8, txtStatus.getText());

             pstmt.executeUpdate();
             JOptionPane.showMessageDialog(this, "Saved Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
             refreshTable();
             clearFields();
         } catch (Exception ex) {
             JOptionPane.showMessageDialog(this, "Insert Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
         }
     }

     private void updateFlight() {
         if (txtFlightID.getText().isEmpty() || txtAirportID.getText().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please select or enter both Flight ID and Airport ID.", "Warning", JOptionPane.WARNING_MESSAGE
);
             return;
         }

         int flightID = Integer.parseInt(txtFlightID.getText());
         int airportID = Integer.parseInt(txtAirportID.getText());

         if (!flightExists(flightID, airportID)) {
             JOptionPane.showMessageDialog(this, "Record not found. Cannot update.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
         }

        String sql = "UPDATE Flight SET flight_number=?, origin=?, destination=?, departure_time=?, arrival_time=?, status=? WHERE flight_id=? AND airport_id=?";

         try (Connection conn = DBConnection.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {

             pstmt.setString(1, txtFlightNum.getText());

            pstmt.setString(2, txtOrigin.getText());
            pstmt.setString(3, txtDestination.getText());
            pstmt.setString(4, txtDeparture.getText());
            pstmt.setString(5, txtArrival.getText());
            pstmt.setString(6, txtStatus.getText());
            pstmt.setInt(7, flightID);
            pstmt.setInt(8, airportID);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFlight() {
        if (txtFlightID.getText().isEmpty() || txtAirportID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a flight record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int flightID = Integer.parseInt(txtFlightID.getText());
        int airportID = Integer.parseInt(txtAirportID.getText());

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this flight record?", "Confirm Deletion",
JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Flight WHERE flight_id=? AND airport_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, flightID);
            pstmt.setInt(2, airportID);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Deleted Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportFlightToText() {
        if (txtFlightID.getText().isEmpty() || txtAirportID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing selected to export.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String fileName = "Flight_" + txtFlightID.getText() + "_" + txtAirportID.getText() + "_Details.txt";

        try (PrintWriter writer = new PrintWriter(fileName)) {
            writer.println("======= FLIGHT INFORMATION =======");
            writer.println("Flight ID:      " + txtFlightID.getText());
            writer.println("Airport ID:     " + txtAirportID.getText());
            writer.println("Flight Number: " + txtFlightNum.getText());
            writer.println("Origin:         " + txtOrigin.getText());
            writer.println("Destination:    " + txtDestination.getText());
            writer.println("Departure Time: " + txtDeparture.getText());
            writer.println("Arrival Time:   " + txtArrival.getText());
            writer.println("Status:         " + txtStatus.getText());
            writer.println("==================================");

            JOptionPane.showMessageDialog(this, "Flight info successfully exported to: " + fileName, "Export Done", JOptionPane.
INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFlights() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Flight")) {

            while (rs.next()) {
                model.addRow(new Object[]{
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
        } catch (SQLException ex) {
            System.out.println("Load Error: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        loadFlights();
    }

    private void clearFields() {
        txtFlightID.setText("");
        txtAirportID.setText("");
        txtFlightNum.setText("");
        txtOrigin.setText("");
        txtDestination.setText("");
        txtDeparture.setText("");
        txtArrival.setText("");
        txtStatus.setText("");
        dataTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightManagementPage().setVisible(true));
    }
}

