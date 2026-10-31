public class Flight {

    private int flightId;
    private int airportId;
    private String flightNumber;
    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private String status;

    // Default Constructor
    public Flight() {
        this.flightId = 0;
        this.airportId = 0;
        this.flightNumber = "";
        this.origin = "";
        this.destination = "";
        this.departureTime = "";
        this.arrivalTime = "";
        this.status = "Scheduled";
    }

    // Parameterized Constructor
    public Flight(int flightId, int airportId, String flightNumber, String origin,
                  String destination, String departureTime, String arrivalTime, String status) {
        this.flightId = flightId;
        this.airportId = airportId;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = status;
    }

    // Getters and Setters
    public int getFlightId() { return flightId; }
    public void setFlightId(int flightId) { this.flightId = flightId; }

    public int getAirportId() { return airportId; }
    public void setAirportId(int airportId) { this.airportId = airportId; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ‫ داﻟﺔ ﻣﺴﺎﻋﺪة ﻟﻄﺒﺎﻋﺔ ﻣﻠﺨﺺ اﻟﺮﺣﻠﺔ ﺑﺸﻜﻞ ﻣﻨﺴﻖ وﺟﺬاب ﻓﻲ اﻟـ‬Console ‫ﻋﻨﺪ اﻟﺤﺎﺟﺔ‬
    public void displayFlightInfo() {
        System.out.println("======= FLIGHT DETAILS =======");
        System.out.println("Flight ID      : " + flightId);
        System.out.println("Airport ID     : " + airportId);
        System.out.println("Flight Number : " + flightNumber);
        System.out.println("Route          : " + origin + " -> " + destination);
        System.out.println("Departure Time : " + departureTime);
        System.out.println("Arrival Time   : " + arrivalTime);
        System.out.println("Status         : " + status);
        System.out.println("==============================");
    }

    @Override
    public String toString() {
        return "Flight{id=" + flightId + ", number='" + flightNumber + "', route='" + origin + "-" + destination + "'}";
    }
}

