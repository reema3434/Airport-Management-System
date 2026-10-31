public class Staff extends Employee {

    // Staff specific field (maps to Staff.position in DB)
    private String position;
    // Default constructor
    public Staff() {
        super();
        setRole("Staff");
        this.position = "";
    }



    public Staff(int id, String name, String email, String phone, String position) {

        super(id, name, "Staff", email, phone, 0.0, "Operations");
        this.position = position;
    }

    // Getter and Setter
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // Override getRole() from Employee
    @Override
    public String getRole() {
        return "Staff - " + position;
    }



    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("--- Staff Details ---");
        System.out.println("Position: " + position);
    }

    @Override
    public String toString() {
        return "Staff{id=" + getId() + ", name='" + getName() + "', position='" + position + "'}";
    }
}

