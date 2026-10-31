-- Synthetic demo data only; NOT production credentials or real passenger information.
-- Requires database/schema.sql to have been executed first.
USE oop;

INSERT INTO Airport (airport_id, name, city) VALUES
(1, 'Demo International Airport', 'Dammam'),
(2, 'Demo Regional Airport', 'Riyadh');

INSERT INTO Employee (employee_id, name, role, email, phone, salary, department, username, password, access_level) VALUES
(201, 'Demo Manager', 'Manager', 'manager@example.com', '0000000000', 0.00, 'Operations', 'demo_manager', 'demo_password_only', 'full'),
(202, 'Demo Staff', 'Staff', 'staff@example.com', '0000000000', 0.00, 'Operations', 'demo_staff', 'demo_password_only', 'basic');

INSERT INTO Manager (employee_id, department) VALUES
(201, 'Operations');

INSERT INTO Staff (employee_id, position) VALUES
(202, 'Ground Crew');

INSERT INTO Flight (flight_id, airport_id, flight_number, origin, destination, departure_time, arrival_time, status) VALUES
(301, 1, 'DEMO101', 'Dammam', 'Riyadh', '2026-11-10 08:00:00', '2026-11-10 09:00:00', 'Scheduled');

INSERT INTO Passenger (passenger_id, passport_number, name, phone, email) VALUES
(101, 'DEMO-PASSPORT', 'Demo Passenger', '0000000000', 'passenger@example.com');

INSERT INTO Ticket (ticket_id, price, booking_date, seat_number, flight_id, airport_id, passenger_id) VALUES
(601, 300.00, '2026-11-01 08:00:00', '12A', 301, 1, 101);
