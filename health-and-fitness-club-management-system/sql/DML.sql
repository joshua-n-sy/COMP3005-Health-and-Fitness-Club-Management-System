/* 
    Fall 2025 - Final Project
    Health and Fitness Club Management System

    Description:
        This file contains the Data Manipulation Language (DML) statements
        used to populate the database with initial sample data for testing
        the application. It includes sample members, trainers, administrative
        staff, equipment, sessions, classes, and related records.

    Author: Joshua Sy
    Course: COMP3005A - Database Management Systems
    Institution: Carleton University
    Date: December 1, 2025
*/

-- Members
INSERT INTO member (first_name, last_name, dob, gender, email, phone) VALUES
('Alice',  'Nguyen', '1995-03-12', 'Female', 'alice.nguyen@example.com', '613-555-0001'),
('Brian',  'Chen',   '1988-07-25', 'Male',   'brian.chen@example.com',   '613-555-0002'),
('Carlos', 'Diaz',   '1992-11-03', 'Male',   'carlos.diaz@example.com',  '613-555-0003'),
('Diana',  'Singh',  '2000-01-18', 'Female', 'diana.singh@example.com',  '613-555-0004'),
('Emily',  'Stone',  '1985-09-30', 'Other', 'emily.stone@example.com',  '613-555-0005');

-- Trainers
INSERT INTO trainer (first_name, last_name, email, phone) VALUES
('Tom',   'Reid', 'tom.reid@example.com',   '613-555-1001'),
('Sarah', 'Khan', 'sarah.khan@example.com', '613-555-1002'),
('Jacob', 'Lee',  'jacob.lee@example.com',  '613-555-1003');

-- Administrative Staff
INSERT INTO admin_user (first_name, last_name, email, phone) VALUES
('Laura', 'Mitchell',  'laura.mitchell@example.com',  '613-555-2001'),
('Kevin', 'Robertson', 'kevin.robertson@example.com', '613-555-2002');


-- Fitness Goals
INSERT INTO fitness_goal (member_id, goal_type, target_value, unit, start_date, target_date, status) VALUES
(1, 'Weight Loss',     60.0, 'kg',  '2025-09-01', '2026-01-01', 'Active'),
(2, 'Muscle Gain',     80.0, 'kg',  '2025-09-15', '2026-02-01', 'Completed'),
(3, 'Body Fat',        15.0, '%',   '2025-10-01', '2026-03-01', 'Active'),
(4, '5K Run Time',     25.0, 'min', '2025-10-10', '2026-01-15', 'Cancelled'),
(5, '10K Run Time',    65.0, 'min', '2025-10-10', '2026-01-15', 'Active');

-- Health Metrics
INSERT INTO health_metric (member_id, measure_time, height, weight, heart_rate, body_fat) VALUES
(1, '2025-11-01 09:00', 165.0, 70.0, 72, 28.5),
(1, '2025-12-01 09:00', 165.0, 67.5, 70, 26.0),
(2, '2025-11-05 10:30', 180.0, 82.0, 68, 20.0),
(2, '2025-12-01 10:30', 180.0, 83.5, 70, 19.5),
(3, '2025-11-10 18:00', 175.0, 78.0, 75, 24.0),
(4, '2025-11-15 08:15', 160.0, 55.0, 65, 22.0),
(5, '2025-11-20 14:45', 170.0, 68.0, 73, 25.0);

-- Rooms
INSERT INTO room (name, capacity) VALUES
('Weight Room',   25),
('Cardio Studio', 20),
('Yoga Studio',   15),
('Spin Studio',   30);

-- Equipment
INSERT INTO equipment (room_id, name, equipment_type, status) VALUES
(1, 'Squat Rack #1',  'Strength',    'Operational'),
(1, 'Bench Press #1', 'Strength',    'Operational'),
(2, 'Treadmill #1',   'Cardio',      'Operational'),
(2, 'Treadmill #2',   'Cardio',      'OutOfOrder'),
(3, 'Yoga Mat Set',   'Flexibility', 'Operational'),
(4, 'Spin Bike #1',   'Cardio',      'Operational'),
(4, 'Spin Bike #2',   'Cardio',      'Operational');

-- Equipment Issues
INSERT INTO equipment_issue (equipment_id, reported_by_admin_id, reported_at, description, status, resolved_at) VALUES
(2, 1, '2025-10-10 15:30', 'Bench Press #1 bar slightly bent.', 'Resolved',  '2025-10-15 11:00'),
(4, 1, '2025-11-12 08:00', 'Treadmill #2 display not working.', 'InProgress', NULL),
(6, 2, '2025-11-25 10:30', 'Spin Bike #1 has no resistance.',   'Open',       NULL);

-- Trainer Availability
INSERT INTO trainer_availability (trainer_id, start_time, end_time, is_recurring) VALUES
(1, '2025-12-02 09:00', '2025-12-02 12:00', FALSE),
(1, '2025-12-03 14:00', '2025-12-03 18:00', FALSE),
(2, '2025-12-02 07:00', '2025-12-02 11:00', FALSE),
(2, '2025-12-04 16:00', '2025-12-04 20:00', FALSE),
(3, '2025-12-05 10:30', '2025-12-05 15:00', FALSE);

-- Personal Training Sessions
INSERT INTO pt_session (member_id, trainer_id, room_id, session_start, session_end, status) VALUES
(1, 1, 1, '2025-12-01 09:30', '2025-12-01 10:30', 'Booked'),
(2, 1, 1, '2025-12-04 10:30', '2025-12-04 11:30', 'Booked'),
(3, 2, 2, '2025-12-01 07:30', '2025-12-01 08:30', 'Completed'),
(4, 2, 3, '2025-12-03 16:30', '2025-12-03 17:30', 'Booked'),
(5, 3, 4, '2025-12-05 10:30', '2025-12-05 11:30', 'Cancelled');

-- Group Classes
INSERT INTO group_class (trainer_id, room_id, name, description, start_time, end_time, capacity, status) VALUES
(1, 1, 'Strength Basics', 'Intro to free weights and proper form.',          '2025-12-03 17:00', '2025-12-03 18:00', 20, 'Scheduled'),
(2, 3, 'Morning Yoga',    'Gentle vinyasa flow suitable for all levels.',    '2025-12-02 07:00', '2025-12-02 08:00', 15, 'Scheduled'),
(2, 3, 'Evening Yoga',    'Intermediate level class with focus on balance.', '2025-12-04 18:00', '2025-12-04 19:00', 15, 'Scheduled'),
(3, 4, 'Spin Express',    'High-intensity 45-minute spin class.',            '2025-12-05 12:00', '2025-12-05 12:45', 30, 'Scheduled');

-- Class Registrations
INSERT INTO class_registration (class_id, member_id, registered_at) VALUES
(1, 1, '2025-11-30 16:00'),
(1, 3, '2025-11-30 16:10'),
(2, 1, '2025-11-28 09:00'),
(2, 2, '2025-11-28 09:05'),
(2, 4, '2025-11-28 10:00'),
(3, 1, '2025-11-29 08:00'),
(3, 5, '2025-11-29 08:10'),
(4, 2, '2025-11-30 15:00'),
(4, 3, '2025-11-30 15:05'),
(4, 5, '2025-11-30 15:10');
