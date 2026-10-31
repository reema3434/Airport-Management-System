public class FlightEmployee {

    private int employeeID;     // FK -> Employee.employee_id
    private int flightID;       // FK -> Flight.flight_id
    private int airportID;      // FK -> Flight.airport_id

    // Default Constructor
    public FlightEmployee() {
        this.employeeID = 0;
        this.flightID = 0;
        this.airportID = 0;
    }

    // Parameterized Constructor
    public FlightEmployee(int employeeID, int flightID, int airportID) {
        this.employeeID = employeeID;
        this.flightID   = flightID;
        this.airportID = airportID;
    }

    // Getters and Setters
    public int getEmployeeID() { return employeeID; }
    public void setEmployeeID(int employeeID) { this.employeeID = employeeID; }

    public int getFlightID() { return flightID; }
    public void setFlightID(int flightID) { this.flightID = flightID; }

    public int getAirportID() { return airportID; }
    public void setAirportID(int airportID) { this.airportID = airportID; }

    // Utility Methods
    public String getAssignmentSummary() {
        return "======= FLIGHT ASSIGNMENT =======\n"
             + "Employee ID : " + employeeID + "\n"
             + "Flight ID   : " + flightID   + "\n"
             + "Airport ID : " + airportID + "\n"
             + "=================================";
    }

    @Override
    public String toString() {
        return "FlightEmployee{employeeID=" + employeeID
                + ", flightID=" + flightID
                + ", airportID=" + airportID + "}";
    }
}

