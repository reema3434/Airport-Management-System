import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class TicketManagementPage extends JFrame {
    private JTextField txtTicketID, txtPrice, txtSeat, txtFlightID, txtAirportID, txtPassengerID;
    private JTable ticketTable;
    private DefaultTableModel ticketModel;

    public TicketManagementPage() {
        setTitle("Flight Tickets Management Operational Hub");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 15)); // ‫رﺟﻊ‬6‫ﺻﻔﻮف ﻧﻈﯿﻔﺔ وﻣﺜﺎﻟﯿﺔ‬
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 220, 228), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color textColor = new Color(52, 73, 94);

        formPanel.add(createStyledLabel("Ticket ID (PK):", labelFont, textColor));
        txtTicketID = createStyledTextField(); formPanel.add(txtTicketID);

        formPanel.add(createStyledLabel("Ticket Price (SAR):", labelFont, textColor));
        txtPrice = createStyledTextField(); formPanel.add(txtPrice);

        formPanel.add(createStyledLabel("Seat Number:", labelFont, textColor));
        txtSeat = createStyledTextField(); formPanel.add(txtSeat);

        formPanel.add(createStyledLabel("Flight ID (FK):", labelFont, textColor));
        txtFlightID = createStyledTextField(); formPanel.add(txtFlightID);

        formPanel.add(createStyledLabel("Airport ID (FK):", labelFont, textColor));
        txtAirportID = createStyledTextField(); formPanel.add(txtAirportID);

        formPanel.add(createStyledLabel("Passenger ID (FK):", labelFont, textColor));
        txtPassengerID = createStyledTextField(); formPanel.add(txtPassengerID);

        // ‫ ﺗﻔﻌﯿﻞ ﺗﺤﺪﯾﺚ ﺣﻘﻞ اﻟﻨﺺ ﻣﺒﺎﺷﺮة ﻋﻨﺪ ﻛﺘﺎﺑﺔ اﻟـ‬ID ‫ ﯾﺪوﯾﺎﹰ واﻟﻀﻐﻂ ﻋﻠﻰ‬Enter
        txtPassengerID.addActionListener(e -> fetchPassengerName());

        // ‫أو ﺑﻤﺠﺮد ﻣﺎ ﯾﺸﯿﻞ اﻟﻤﻮﻇﻒ ﯾﺪه ﻋﻦ اﻟﻜﯿﺒﻮرد أو ﯾﻨﻘﻞ اﻟﺘﺮﻛﯿﺰ ﻟﺤﻘﻞ آﺧﺮ‬
        txtPassengerID.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) { fetchPassengerName(); }
        });

        JPanel leftContainer = new JPanel(new BorderLayout(10, 10));
        leftContainer.setBackground(new Color(245, 247, 250));
        leftContainer.setBorder(new EmptyBorder(10, 15, 15, 5));
        leftContainer.add(formPanel, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new GridLayout(5, 1, 5, 6));
        btnRow.setBackground(new Color(245, 247, 250));
        JButton btnSave = createStyledButton("Issue Flight Ticket", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update Details", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Cancel Ticket", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear Fields", new Color(241, 196, 15));
        JButton btnExport = createStyledButton("Print Ticket (TXT)", new Color(149, 165, 166));
        btnRow.add(btnSave); btnRow.add(btnUpdate); btnRow.add(btnDelete); btnRow.add(btnClear); btnRow.add(btnExport);
        leftContainer.add(btnRow, BorderLayout.SOUTH);

        ticketModel = new DefaultTableModel(new Object[]{"Ticket ID", "Price", "Booking Date", "Seat", "Flight ID", "Airport ID", "Passenger ID"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        ticketTable = new JTable(ticketModel);
        styleTable(ticketTable);

        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            int r = ticketTable.getSelectedRow();
            if (r >= 0 && !e.getValueIsAdjusting()) {
                txtTicketID.setText(ticketModel.getValueAt(r, 0).toString());

                 txtPrice.setText(ticketModel.getValueAt(r, 1).toString());
                 txtSeat.setText(ticketModel.getValueAt(r, 3).toString());
                 txtFlightID.setText(ticketModel.getValueAt(r, 4).toString());
                 txtAirportID.setText(ticketModel.getValueAt(r, 5).toString());

                 // ‫ ﺟﻠﺐ اﻟـ‬ID ‫اﻟﺼﺎﻓﻲ ﻟﻠﺮاﻛﺐ وﺗﺤﺪﯾﺜﮫ ﻓﻮراﹰ ﻟﯿﻈﮭﺮ ﻣﺪﻣﺠﺎﹰ ﻣﻊ اﻻﺳﻢ‬
                 String rawPassengerId = ticketModel.getValueAt(r, 6).toString();
                 txtPassengerID.setText(rawPassengerId);
                 fetchPassengerName();
             }
       });

       add(leftContainer, BorderLayout.WEST);
       add(new JScrollPane(ticketTable), BorderLayout.CENTER);

       btnSave.addActionListener(e -> saveTicket());
       btnUpdate.addActionListener(e -> updateTicket());
       btnDelete.addActionListener(e -> deleteTicket());
       btnClear.addActionListener(e -> clearFields());
       btnExport.addActionListener(e -> exportTicketToText());

       loadTickets();
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

   // ✅ ‫ داﻟﺔ اﻟﺘﺤﺪﯾﺚ اﻟﺪﯾﻨﺎﻣﯿﻜﻲ اﻟﺬﻛﻲ ﻟﺘﺪﻣﺞ اﻻﺳﻢ ﻣﻊ اﻟـ‬ID ‫ﻓﻲ ﻧﻔﺲ اﻟﺘﻜﺴﺖ ﻓﯿﻠﺪ‬
   private void fetchPassengerName() {
       String input = txtPassengerID.getText().trim();
       if (input.isEmpty()) return;

       // ‫ ﻧﺴﺘﺨﺮج اﻟـ‬،(‫ إذا ﻛﺎن اﻟﻤﺪﺧﻞ ﯾﺤﺘﻮي ﻋﻠﻰ ﺷﺮﻃﺔ اﺻﻼﹰ )ﯾﻌﻨﻲ ﺗﻢ اﺧﺘﯿﺎره ﻣﻦ اﻟﺠﺪول‬ID ‫اﻟﺼﺎﻓﻲ أوﻻﹰ ﻟﺒﺤﺚ اﻟﺴﯿﻜﻮل‬
       String cleanID = input.contains(" - ") ? input.split(" - ")[0] : input;

       try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM Passenger WHERE passenger_id=?")) {
           ps.setInt(1, Integer.parseInt(cleanID));
           try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    txtPassengerID.setText(cleanID + " - " + rs.getString("name"));
                } else {
                    txtPassengerID.setText(cleanID + " - [Not Found]");
                }
           }
       } catch (Exception ex) {
           // ‫ﺗﺒﻘﻰ ﻛﻤﺎ ھﻲ ﻟﻮ ﻛﺎﻧﺖ اﻟﻘﯿﻤﺔ ﻟﯿﺴﺖ رﻗﻤﺎﹰ‬
       }
   }

   // ‫ داﻟﺔ ﻣﺴﺎﻋﺪة ﻟﻘﺺ اﻟﻨﺺ واﺳﺘﺨﺮاج اﻟـ‬ID ‫اﻟﺼﺎﻓﻲ ﻓﻘﻂ ﻗﺒﻞ إرﺳﺎﻟﮫ ﻟﻘﺎﻋﺪة اﻟﺒﯿﺎﻧﺎت ﻟﻤﻨﻊ اﻟﺴﯿﻜﻮل إﯾﺮور‬
   private int getCleanPassengerID() {
       String input = txtPassengerID.getText().trim();
       if (input.contains(" - ")) {
           return Integer.parseInt(input.split(" - ")[0]);
       }
       return Integer.parseInt(input);
   }

    private void loadTickets() {
        ticketModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection(); ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Ticket")) {
            while (rs.next()) { ticketModel.addRow(new Object[]{rs.getInt("ticket_id"), rs.getDouble("price"), rs.getTimestamp("booking_date"
), rs.getString("seat_number"), rs.getInt("flight_id"), rs.getInt("airport_id"), rs.getInt("passenger_id")}); }
        } catch (Exception ex) { System.out.println(ex.getMessage()); }
    }

    private void saveTicket() {
        String sql = "INSERT INTO Ticket (ticket_id, price, booking_date, seat_number, flight_id, airport_id, passenger_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(txtTicketID.getText()));
            pstmt.setDouble(2, Double.parseDouble(txtPrice.getText()));
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(4, txtSeat.getText());
            pstmt.setInt(5, Integer.parseInt(txtFlightID.getText()));
            pstmt.setInt(6, Integer.parseInt(txtAirportID.getText()));
            pstmt.setInt(7, getCleanPassengerID()); // ✅ ‫ ﺳﺤﺐ اﻟـ‬ID ‫اﻟﺼﺎﻓﻲ اﻟﻤﻘﺼﻮص ﺑﺄﻣﺎن‬

           pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "Ticket Issued."); loadTickets();
       } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
   }

   private void updateTicket() {
       String sql = "UPDATE Ticket SET price=?, seat_number=?, flight_id=?, airport_id=?, passenger_id=? WHERE ticket_id=?";
       try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
           pstmt.setDouble(1, Double.parseDouble(txtPrice.getText()));
           pstmt.setString(2, txtSeat.getText());
           pstmt.setInt(3, Integer.parseInt(txtFlightID.getText()));
           pstmt.setInt(4, Integer.parseInt(txtAirportID.getText()));
           pstmt.setInt(5, getCleanPassengerID()); // ✅ ‫ ﺳﺤﺐ اﻟـ‬ID ‫اﻟﺼﺎﻓﻲ اﻟﻤﻘﺼﻮص ﺑﺄﻣﺎن‬
           pstmt.setInt(6, Integer.parseInt(txtTicketID.getText()));

           pstmt.executeUpdate(); JOptionPane.showMessageDialog(this, "Ticket Record Updated."); loadTickets();
       } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
   }

    private void deleteTicket() {
        int ok = JOptionPane.showConfirmDialog(this, "Delete this ticket?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Ticket WHERE ticket_id=?")) {
            pstmt.setInt(1, Integer.parseInt(txtTicketID.getText())); pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Ticket Canceled."); loadTickets();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

   private void exportTicketToText() {
       String fileName = "Ticket_No_" + txtTicketID.getText() + ".txt";
       try (PrintWriter w = new PrintWriter(fileName)) {
           w.println("Ticket ID: " + txtTicketID.getText() + " | Price: " + txtPrice.getText() + " | Seat: " + txtSeat.getText());
           w.println("Flight ID: " + txtFlightID.getText() + " | Airport ID: " + txtAirportID.getText());
           w.println("Passenger Reference: " + txtPassengerID.getText()); // ‫ﺑﯿﻄﺒﻊ اﻟﺮﻗﻢ واﻻﺳﻢ ﻣﻨﺴﻘﯿﻦ‬
           JOptionPane.showMessageDialog(this, "Exported to " + fileName);
       } catch (Exception ex) { System.out.println(ex.getMessage()); }
   }

    private void clearFields() {
        txtTicketID.setText(""); txtPrice.setText(""); txtSeat.setText(""); txtFlightID.setText(""); txtAirportID.setText(""); txtPassengerID.
setText(""); ticketTable.clearSelection();
    }
}

