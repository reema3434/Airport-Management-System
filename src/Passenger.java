public class Passenger {



    private int passengerID;
    private String name;
    private String passportNumber;
    private String email;
    private String phone;

    // Default Constructor
    public Passenger() {
        this.passengerID = 0;
        this.name = "";
        this.passportNumber = "";
        this.email = "";
        this.phone = "";
    }

    // Parameterized Constructor to initialize passenger object
    public Passenger(int passengerID, String name, String passportNumber, String email, String phone) {
        this.passengerID = passengerID;
        this.name = name;
        this.passportNumber = passportNumber;
        this.email = email;
        this.phone = phone;
    }

    // Standard Getters and Setters
    public int getPassengerID() { return passengerID; }
    public void setPassengerID(int passengerID) { this.passengerID = passengerID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }



    public void displayPassengerInfo() {
        System.out.println("=== PASSENGER INFO ===");
        System.out.println("Passenger ID : " + passengerID);
        System.out.println("Name         : " + name);
        System.out.println("Passport No : " + passportNumber);
        System.out.println("Email        : " + email);
        System.out.println("Phone        : " + phone);
        System.out.println("======================");
    }

    @Override
    public String toString() {
        return "Passenger{id=" + passengerID + ", name='" + name + "', passport='" + passportNumber + "'}";
    }
}

