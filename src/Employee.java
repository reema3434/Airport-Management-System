public class Employee {



    private int id;
    private String name;
    private String role;
    private String email;
    private String phone;
    private double salary;
    private String department;

    // Default constructor
    public Employee() {
        this.id = 0;
        this.name = "";
        this.role = "Employee";
        this.email = "";
        this.phone = "";
        this.salary = 0.0;
        this.department = "";
    }

    // Parameterized constructor
    public Employee(int id, String name, String role, String email, String phone, double salary, String department) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.department = department;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Methods (‫)اﻟﺪوال اﻟﻤﻨﺴﻘﺔ ﻟﻠﻌﺮض‬
    public void displayInfo() {
        System.out.println("=== Employee Info ===");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Salary: " + salary + " SAR");
        System.out.println("Department: " + department);
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', role='" + role + "', department='" + department + "'}";
    }
}

