import java.time.LocalDateTime;

public class Ticket {



    private int ticketID;
    private double price;
    private String seatNumber;
    private LocalDateTime bookingDate;
    private int flightID;
    private int airportID;
    private int passengerID;

    // Default Constructor sets current time as booking date
    public Ticket() {
        this.ticketID = 0;
        this.price = 0.0;
        this.seatNumber = "";
        this.bookingDate = LocalDateTime.now();
        this.flightID = 0;
        this.airportID = 0;
        this.passengerID = 0;
    }

    // Constructor to create a ticket with specific details
    public Ticket(int ticketID, double price, String seatNumber, int flightID, int airportID, int passengerID) {
        this.ticketID = ticketID;
        this.price = price;
        this.seatNumber = seatNumber;
        this.flightID = flightID;
        this.airportID = airportID;
        this.passengerID = passengerID;
        this.bookingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getTicketID() { return ticketID; }
    public void setTicketID(int ticketID) { this.ticketID = ticketID; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public int getFlightID() { return flightID; }
    public void setFlightID(int flightID) { this.flightID = flightID; }

    public int getAirportID() { return airportID; }
    public void setAirportID(int airportID) { this.airportID = airportID; }

    public int getPassengerID() { return passengerID; }
    public void setPassengerID(int passengerID) { this.passengerID = passengerID; }

    // Formats ticket info for text file export
    public String getTicketSummary() {
        return "--- TICKET DETAILS ---\n" +
            "Ticket ID: " + ticketID + "\n" +
            "Passenger ID: " + passengerID + "\n" +
        "Flight ID: " + flightID + "\n" +
            "Airport ID: " + airportID + "\n" +
            "Seat: " + seatNumber + "\n" +
            "Price: " + price + " SAR\n" +
            "Date: " + bookingDate + "\n" +
            "----------------------";
    }

    @Override
    public String toString() {
        return "Ticket{id=" + ticketID + ", passenger=" + passengerID + ", flight=" + flightID + "}";
    }
}

