-- Schema for the CSC236 educational Airport Management System.
-- Back up existing data before executing this script.

-- Existing databases are never dropped by this setup script.

CREATE DATABASE IF NOT EXISTS oop;

USE oop;




CREATE TABLE Passenger (

     passenger_id INT PRIMARY KEY,

     passport_number VARCHAR(20),

     name VARCHAR(100),

     phone VARCHAR(15),

     email VARCHAR(100)

);




CREATE TABLE Employee (

     employee_id INT PRIMARY KEY,

     name VARCHAR(100),

     role VARCHAR(50),

     email VARCHAR(100),

     phone VARCHAR(20),

     salary DECIMAL(10,2) DEFAULT 0.00,

     department VARCHAR(100) DEFAULT '',

     username VARCHAR(50) UNIQUE,

     password VARCHAR(50),

     access_level VARCHAR(20)

);




CREATE TABLE Airport (

     airport_id INT PRIMARY KEY,

     name VARCHAR(100),

     city VARCHAR(50)

);




CREATE TABLE Manager (

     employee_id INT PRIMARY KEY,

     department VARCHAR(100),

     FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE

);




CREATE TABLE Staff (

     employee_id INT PRIMARY KEY,

     position VARCHAR(100),

     FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE

);

CREATE TABLE Flight (

      flight_id INT,

      airport_id INT,

      flight_number VARCHAR(10),

      origin VARCHAR(100),

      destination VARCHAR(100),

      departure_time DATETIME,

      arrival_time DATETIME,

      status VARCHAR(20),

      PRIMARY KEY (flight_id, airport_id),

      FOREIGN KEY (airport_id) REFERENCES Airport(airport_id)

);




CREATE TABLE Ticket (

      ticket_id INT PRIMARY KEY,

      price DECIMAL(10,2),

      booking_date DATETIME,

      seat_number VARCHAR(10),

      flight_id INT,

      airport_id INT,

      passenger_id INT,

      FOREIGN KEY (passenger_id) REFERENCES Passenger(passenger_id),

      FOREIGN KEY (flight_id, airport_id) REFERENCES Flight(flight_id, airport_id)

);




CREATE TABLE FlightEmployee (

      flight_id INT,

      employee_id INT,

      airport_id INT,

      PRIMARY KEY (flight_id, employee_id, airport_id),

      FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),

      FOREIGN KEY (flight_id, airport_id) REFERENCES Flight(flight_id, airport_id)

);
