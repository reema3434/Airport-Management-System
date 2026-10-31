public class Manager extends Employee {

   // Additional fields specific to Manager
   private String managerLevel;       // e.g., "Senior", "Junior", "Executive"
   private int teamSize;
   private String accessLevel;        // For managing privileges (‫)ﺻﻼﺣﯿﺎت اﻟﻤﺪﯾﺮﯾﻦ‬
   private boolean canManageFlights;
   private boolean canManageEmployees;
   private boolean canManagePassengers;
   private boolean canManageTickets;

   // Default constructor
   public Manager() {
       super();
       setRole("Manager");
       this.managerLevel = "Junior";
       this.teamSize = 0;
       this.accessLevel = "Basic";
       applyAccessLevel(this.accessLevel);
   }

   // Parameterized constructor
   public Manager(int id, String name, String email, String phone, double salary,
               String department, String managerLevel, int teamSize, String accessLevel) {

       super(id, name, "Manager", email, phone, salary, department);
       this.managerLevel = managerLevel;
       this.teamSize = teamSize;
       this.accessLevel = accessLevel;

       applyAccessLevel(accessLevel);
   }



   private void applyAccessLevel(String level) {
       if (level == null) level = "basic";
       switch (level.toLowerCase()) {
           case "full":
               canManageFlights = true;
               canManageEmployees = true;
               canManagePassengers = true;
               canManageTickets = true;
               break;
           case "supervisor":
               canManageFlights = true;
               canManageEmployees = false;
               canManagePassengers = true;
               canManageTickets = true;
               break;
           case "basic":
           default:
               canManageFlights = false;
               canManageEmployees = false;
               canManagePassengers = false;
               canManageTickets = false;
               break;
       }
   }

   // Getters and Setters
   public String getManagerLevel() {
       return managerLevel;
   }

   public void setManagerLevel(String managerLevel) {
       this.managerLevel = managerLevel;
   }

   public int getTeamSize() {
       return teamSize;
   }

   public void setTeamSize(int teamSize) {
       this.teamSize = teamSize;
   }

   public String getAccessLevel() {
       return accessLevel;
   }

   public void setAccessLevel(String accessLevel) {
       this.accessLevel = accessLevel;
       applyAccessLevel(accessLevel);
   }

    public boolean isCanManageFlights() {
        return canManageFlights;
    }

    public void setCanManageFlights(boolean canManageFlights) {
        this.canManageFlights = canManageFlights;
    }

    public boolean isCanManageEmployees() {
        return canManageEmployees;
    }

    public void setCanManageEmployees(boolean canManageEmployees) {
        this.canManageEmployees = canManageEmployees;
    }

    public boolean isCanManagePassengers() {
        return canManagePassengers;
    }

    public void setCanManagePassengers(boolean canManagePassengers) {
        this.canManagePassengers = canManagePassengers;
    }

    public boolean isCanManageTickets() {
        return canManageTickets;
    }

    public void setCanManageTickets(boolean canManageTickets) {
        this.canManageTickets = canManageTickets;
    }

    // Override getRole() from Employee
    @Override
    public String getRole() {
        return "Manager (" + managerLevel + ")";
    }

    // Override displayInfo() to include Manager-specific info
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("--- Manager Details ---");
        System.out.println("Manager Level: " + managerLevel);
        System.out.println("Team Size:     " + teamSize);
        System.out.println("Access Level: " + accessLevel);
        System.out.println("Privileges:");
        System.out.println(" Manage Flights:      " + canManageFlights);
        System.out.println(" Manage Employees: " + canManageEmployees);
        System.out.println(" Manage Passengers: " + canManagePassengers);
        System.out.println(" Manage Tickets:      " + canManageTickets);
    }

    @Override
    public String toString() {
        return "Manager{id=" + getId() + ", name='" + getName() + "', level='" + managerLevel + "', access='" + accessLevel + "'}";
    }
}

