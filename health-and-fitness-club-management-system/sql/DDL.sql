/* 
    Fall 2025 - Final Project
    Health and Fitness Club Management System

    Description:
        This file defines the relational database schema used by the application
        to support user management, scheduling, facility coordination, and
        operational tracking for a fitness club environment.

    Author: Joshua Sy
    Course: COMP3005A - Database Management Systems
    Institution: Carleton University
    Date: December 1, 2025
*/

DROP TABLE IF EXISTS class_registration CASCADE;
DROP TABLE IF EXISTS group_class CASCADE;
DROP TABLE IF EXISTS pt_session CASCADE;
DROP TABLE IF EXISTS trainer_availability CASCADE;
DROP TABLE IF EXISTS equipment_issue CASCADE;
DROP TABLE IF EXISTS equipment CASCADE;
DROP TABLE IF EXISTS fitness_goal CASCADE;
DROP TABLE IF EXISTS health_metric CASCADE;
DROP TABLE IF EXISTS member CASCADE;
DROP TABLE IF EXISTS trainer CASCADE;
DROP TABLE IF EXISTS room CASCADE;
DROP TABLE IF EXISTS admin_user CASCADE;

-- Members: Stores personal information for members.
CREATE TABLE member (
    member_id    SERIAL PRIMARY KEY,
    first_name   TEXT NOT NULL,
    last_name    TEXT NOT NULL,
    dob          DATE NOT NULL,
    gender       TEXT NOT NULL CHECK (gender IN ('Male', 'Female', 'Other')),
    email        TEXT NOT NULL UNIQUE,
    phone        TEXT,
    join_date    DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Trainers: Stores personal information for trainers.
CREATE TABLE trainer (
    trainer_id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    email      TEXT NOT NULL UNIQUE,
    phone      TEXT
);

-- Administrative Staff: Stores information for administrative staff.
CREATE TABLE admin_user (
    admin_id   SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    email      TEXT NOT NULL UNIQUE,
    phone      TEXT
);

-- Fitness Goals: Tracks individual member goals (e.g., weight, body fat).
CREATE TABLE fitness_goal (
    goal_id      SERIAL PRIMARY KEY,
    member_id    INTEGER NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    goal_type    TEXT NOT NULL,
    target_value NUMERIC (7,2) NOT NULL,
    unit         TEXT,
    start_date   DATE NOT NULL DEFAULT CURRENT_DATE,
    target_date  DATE,
    status       TEXT NOT NULL DEFAULT 'Active' CHECK (status IN ('Active', 'Completed', 'Cancelled'))
);

-- Health Metrics: Logs historical health data for progress tracking.
CREATE TABLE health_metric (
    metric_id    SERIAL PRIMARY KEY,
    member_id    INTEGER NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    measure_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    height       NUMERIC(5,2) CHECK (height > 0),
    weight       NUMERIC(5,2) CHECK (weight > 0),
    heart_rate   INTEGER CHECK (heart_rate > 0),
    body_fat     NUMERIC(4,2) CHECK (body_fat BETWEEN 0 AND 100)
);

-- Rooms: Represents physical rooms available for sessions and classes.
CREATE TABLE room (
    room_id   SERIAL PRIMARY KEY,
    name      TEXT NOT NULL UNIQUE,
    capacity  INTEGER NOT NULL CHECK (capacity > 0)
);

-- Equipment: Stores equipment details and operational status.
CREATE TABLE equipment (
    equipment_id   SERIAL PRIMARY KEY,
    room_id        INTEGER REFERENCES room(room_id) ON DELETE SET NULL,
    name           TEXT NOT NULL,
    equipment_type TEXT,
    status         TEXT NOT NULL DEFAULT 'Operational' CHECK (status IN ('Operational', 'OutOfOrder'))
);

-- Equipment Issues: Records maintenance reports submitted by admins.
CREATE TABLE equipment_issue (
    issue_id             SERIAL PRIMARY KEY,
    equipment_id         INTEGER NOT NULL REFERENCES equipment(equipment_id) ON DELETE CASCADE,
    reported_by_admin_id INTEGER NOT NULL REFERENCES admin_user(admin_id),
    reported_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description          TEXT NOT NULL,
    status               TEXT NOT NULL DEFAULT 'Open' CHECK (status IN ('Open', 'InProgress', 'Resolved')),
    resolved_at          TIMESTAMP
);

-- Trainer Availability: Stores when trainers are available for booking.
CREATE TABLE trainer_availability (
    availability_id SERIAL PRIMARY KEY,
    trainer_id      INTEGER NOT NULL REFERENCES trainer(trainer_id) ON DELETE CASCADE,
    start_time      TIMESTAMP NOT NULL,
    end_time        TIMESTAMP NOT NULL,
    is_recurring    BOOLEAN NOT NULL DEFAULT FALSE,
    CHECK (end_time > start_time)
);

-- Personal Training Sessions: Stores scheduled personal training sessions.
CREATE TABLE pt_session (
    pt_session_id SERIAL PRIMARY KEY,
    member_id     INTEGER NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    trainer_id    INTEGER NOT NULL REFERENCES trainer(trainer_id) ON DELETE CASCADE,
    room_id       INTEGER NOT NULL REFERENCES room(room_id) ON DELETE RESTRICT,
    session_start TIMESTAMP NOT NULL,
    session_end   TIMESTAMP NOT NULL,
    status        TEXT NOT NULL DEFAULT 'Booked' CHECK (status IN ('Booked', 'Cancelled', 'Completed')),
    CHECK (session_end > session_start)
);

CREATE INDEX idx_pt_session_trainer_time ON pt_session (trainer_id, session_start);

-- Group Classes: Defines scheduled group fitness classes.
CREATE TABLE group_class (
    class_id    SERIAL PRIMARY KEY,
    trainer_id  INTEGER NOT NULL REFERENCES trainer(trainer_id) ON DELETE RESTRICT,
    room_id     INTEGER NOT NULL REFERENCES room(room_id) ON DELETE RESTRICT,
    name        TEXT NOT NULL,
    description TEXT,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,
    capacity    INTEGER NOT NULL CHECK (capacity > 0),
    status      TEXT NOT NULL DEFAULT 'Scheduled' CHECK (status IN ('Scheduled', 'Cancelled', 'Completed')),
    CHECK (end_time > start_time)
);

-- Class Registrations: Tracks member enrollments in group classes.
CREATE TABLE class_registration (
    registration_id SERIAL PRIMARY KEY,
    class_id        INTEGER NOT NULL REFERENCES group_class(class_id) ON DELETE CASCADE,
    member_id       INTEGER NOT NULL REFERENCES member(member_id) ON DELETE CASCADE,
    registered_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (class_id, member_id)
);
