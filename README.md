# Health and Fitness Club Management System
COMP 3005 - Fall 2025 Final Project

## Overview
This project implements a Health and Fitness Club Management System that provides a centralized, database-driven platform for managing the core operations of a modern fitness center. The system supports both personal training sessions and group fitness classes, ensuring organized scheduling, room allocation, and trainer coordination.

The application accommodates three types of users: members, trainers, and administrative staff, each with specific responsibilities and access levels within the system.

All data is stored in a PostgreSQL relational database, and the backend is implemented in Java using JDBC to interact with the database.

## Features
### Member
Members can manage their personal information, track fitness progress, and interact with the club’s services. They can update their profile, set fitness goals, record health metrics, and view their progress over time. Members can also book, reschedule, or cancel personal training sessions, as well as register for group fitness classes and view their upcoming schedule.

### Trainer
Trainers can manage their availability by adding, viewing, or removing time slots. They can also view their upcoming sessions and classes. Additionally, trainers have read-only access to member information, allowing them to look up members by name to review goals and health metrics.

### Administrative Staff
Administrative staff oversee the operational aspects of the fitness club. They can manage rooms, equipment, and group fitness classes. This includes creating and updating rooms, assigning and tracking equipment, logging equipment issues, and scheduling or editing group classes while ensuring trainer and room availability.

## Setup Instructions
1. Install Requirements
Make sure you have the following installed:
- Apache Maven
- Java 17+
- PostgreSQL 14+
- A terminal or IDE

2. Clone the Repository
git clone
cd health-and-fitness-club-management-system

3. Set Up the PostgreSQL Database
Start your PostgreSQL server.
Open a SQL client.
Run the provided DDL script.

4. Configure database connection.

5. Build the Project
mvn clean compile

6. Start the Application
mvn exec:java

## Author
Name: Joshua Sy
Course: COMP3005A – Database Management Systems
Institution: Carleton University
Date: December 1, 2025

## Video Demonstration
A short video demonstration of the project can be viewed here: [Watch on Youtube](URL)
