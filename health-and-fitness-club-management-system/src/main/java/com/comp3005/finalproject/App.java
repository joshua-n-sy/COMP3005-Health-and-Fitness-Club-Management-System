package com.comp3005.finalproject;

import java.util.List;
import java.util.Scanner;

/**
 * Fall 2025 - Final Project
 * Application: Health and Fitness Club Management System
 *
 * Description:
 *  Main application entry point for the Health and Fitness 
 *  Club Management System. Handles the console menus, user 
 *  navigation, and delegates operations to service and repository
 *  classes.
 *
 * Author: Joshua Sy
 * Course: COMP3005A - Database Management Systems
 * Institution: Carleton University
 * Date: December 1, 2025
 *
 * Notes:
 *  - Requires PostgreSQL running locally on port 5432.
 *  - Update DatabaseConnection.java for database credentials.
 *  - To run:
 *      mvn clean compile
 *      mvn exec:java
 */
public class App {
    private static final Scanner scanner = new Scanner(System.in);

    private static final MemberService memberService = new MemberService();
    private static final TrainerService trainerService = new TrainerService();
    private static final AdminService adminService = new AdminService();

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("            WELCOME TO SY FITNESS CLUB");
        System.out.println("             Member Experience Portal");
        System.out.println("==================================================\n");

        while (true) {
            System.out.println("Please select your access role:");
            System.out.println("  [1] Member");
            System.out.println("  [2] Trainer");
            System.out.println("  [3] Administrative Staff");
            System.out.println("  [0] Exit the System\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> memberMenu();
                case 2 -> trainerMenu();
                case 3 -> adminMenu();
                case 0 -> {
                    System.out.println("Thank you for training with Sy Fitness Club.");
                    System.out.println("Consistency builds champions. Until next time!\n");
                    System.exit(0);
                }
                default -> System.out.println("Invalid selection. Please choose a valid option.\n");
            }
        }
    }

    // Member Menu
    private static void memberMenu() {
        System.out.print("\nPlease enter your Member ID: ");
        int memberId = readInt();

        while (true) {
            System.out.println("\n============ MEMBER EXPERIENCE MENU ============\n");

            System.out.println("Please select an action:\n");
            System.out.println("  [1]  View Profile");
            System.out.println("  [2]  Update Profile Details");
            System.out.println("  [3]  Add New Fitness Goal");
            System.out.println("  [4]  Update Fitness Goal Status");
            System.out.println("  [5]  Record New Health Metric");
            System.out.println("  [6]  View Health & Progress History");

            System.out.println("\n  Personal Training Services:");
            System.out.println("  [7]  Book Session");
            System.out.println("  [8]  Reschedule Session");
            System.out.println("  [9]  Cancel Session");
            System.out.println("  [10] View Upcoming Sessions");

            System.out.println("\n  Group Fitness Class Services:");
            System.out.println("  [11] Register for Class");
            System.out.println("  [12] View Class Registrations");
            
            System.out.println("  [0]  Return to Main Menu\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> showMemberProfile(memberId);
                case 2 -> updateMemberProfile(memberId);
                case 3 -> addFitnessGoal(memberId);
                case 4 -> updateGoalStatus();
                case 5 -> addHealthMetric(memberId);
                case 6 -> memberService.showHealthHistory(memberId);
                case 7 -> bookPt(memberId);
                case 8 -> reschedulePt(memberId);
                case 9 -> cancelPt();
                case 10 -> memberService.showPtSessions(memberId);
                case 11 -> registerForClass(memberId);
                case 12 -> memberService.showClassRegistrations(memberId);
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Trainer Menu
    private static void trainerMenu() {
        System.out.print("\nPlease enter your Trainer ID: ");
        int trainerId = readInt();

        while (true) {
            System.out.println("\n============ TRAINER EXPERIENCE MENU ===========\n");

            System.out.println("Please select an action:\n");
            System.out.println("  [1] View Availability");
            System.out.println("  [2] Add New Availability");
            System.out.println("  [3] Remove Existing Availability");
            System.out.println("  [4] View Upcoming Schedule");

            System.out.println("\n  Member Insights:");
            System.out.println("  [5] Look Up Member by Name");

            System.out.println("  [0] Return to Main Menu\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> trainerService.showAvailability(trainerId);
                case 2 -> addTrainerAvailability(trainerId);
                case 3 -> removeTrainerAvailability();
                case 4 -> trainerService.showSchedule(trainerId);
                case 5 -> {
                    System.out.print("Enter member full name: ");
                    String name = scanner.nextLine();
                    trainerService.lookupMemberByName(name);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Admin Menu
    private static void adminMenu() {
        System.out.print("\nPlease enter your Admin ID: ");
        int adminId = readInt();

        while (true) {
            System.out.println("\n============ ADMIN EXPERIENCE MENU =============\n");
            
            System.out.println("Please select an action:\n");
            System.out.println("  [1] Manage Rooms");
            System.out.println("  [2] Manage Equipment");
            System.out.println("  [3] Manage Group Fitness Classes");
            System.out.println("  [0] Return to Main Menu\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> roomMenu();
                case 2 -> equipmentMenu(adminId);
                case 3 -> classMenu();
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Room Submenu
    private static void roomMenu() {
        while (true) {
            System.out.println("\n============= ROOM MANAGEMENT MENU =============\n");

            System.out.println("Please select an action:\n");
            System.out.println("  [1] View All Rooms");
            System.out.println("  [2] Add New Room");
            System.out.println("  [3] Update Room Name");
            System.out.println("  [4] Update Room Capacity");
            System.out.println("  [0] Back\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> adminService.showAllRooms();
                case 2 -> {
                    System.out.print("Enter room name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter capacity: ");
                    int cap = readInt();
                    adminService.addRoom(name, cap);
                }
                case 3 -> {
                    System.out.print("Enter room ID: ");
                    int id = readInt();
                    System.out.print("Enter new room name: ");
                    String name = scanner.nextLine();
                    adminService.updateRoomName(id, name);
                }
                case 4 -> {
                    System.out.print("Enter room ID: ");
                    int id = readInt();
                    System.out.print("Enter new room capacity: ");
                    int cap = readInt();
                    adminService.updateRoomCapacity(id, cap);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Equipment Submenu
    private static void equipmentMenu(int adminId) {
        while (true) {
            System.out.println("\n=========== EQUIPMENT MANAGEMENT MENU ==========\n");

            System.out.println("Please select an action:\n");
            System.out.println("  [1] View Equipment Inventory");
            System.out.println("  [2] Add New Equipment");
            System.out.println("  [3] Update Equipment Status");
            System.out.println("  [4] Report New Equipment Issue");
            System.out.println("  [5] Update Equipment Issue Status");
            System.out.println("  [6] View All Open Equipment Issues");
            System.out.println("  [7] View Issues for Specific Equipment");
            System.out.println("  [0] Back\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> adminService.showEquipment();
                case 2 -> {
                    System.out.print("Enter room ID (or 0 for none): ");
                    int room = readInt();
                    Integer roomId = (room == 0 ? null : room);

                    System.out.print("Enter equipment name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter equipment type: ");
                    String type = scanner.nextLine();

                    adminService.addEquipment(roomId, name, type);
                }
                case 3 -> {
                    System.out.print("Enter equipment ID: ");
                    int eq = readInt();
                    System.out.print("Enter new status (Operational / OutOfService): ");
                    String status = scanner.nextLine();
                    adminService.updateEquipmentStatus(eq, status);
                }
                case 4 -> {
                    System.out.print("Enter equipment ID: ");
                    int eq = readInt();
                    System.out.print("Describe the issue: ");
                    String desc = scanner.nextLine();
                    adminService.logEquipmentIssue(eq, adminId, desc);
                }
                case 5 -> {
                    System.out.print("Enter issue ID: ");
                    int issue = readInt();
                    System.out.print("Enter new status (Open / InProgress / Resolved): ");
                    String status = scanner.nextLine();

                    String resolvedTime = null;
                    if (status.equalsIgnoreCase("Resolved")) {
                        System.out.print("Enter resolution time (YYYY-MM-DD HH:MM:SS): ");
                        resolvedTime = scanner.nextLine();
                    }
                    adminService.updateEquipmentIssueStatus(issue, status, resolvedTime);
                }
                case 6 -> adminService.showOpenEquipmentIssues();
                case 7 -> {
                    System.out.print("Enter equipment ID: ");
                    int eq = readInt();
                    adminService.showIssuesForEquipment(eq);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Class Submenu
    private static void classMenu() {
        while (true) {
            System.out.println("\n============ CLASS MANAGEMENT MENU =============\n");

            System.out.println("Please select an action:\n");
            System.out.println("  [1] View All Scheduled Classes");
            System.out.println("  [2] Create New Class");
            System.out.println("  [3] Update Class Details");
            System.out.println("  [4] Cancel Class");
            System.out.println("  [0] Back\n");

            System.out.print("Selection: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> adminService.showAllClasses();
                case 2 -> addGroupClass();
                case 3 -> updateGroupClass();
                case 4 -> {
                    System.out.print("Enter class ID: ");
                    int id = readInt();
                    adminService.cancelGroupClass(id);
                }
                case 0 -> { return; }
                default -> System.out.println("Invalid selection. Please select a valid option.\n");
            }
        }
    }

    // Member Action Methods
    private static void showMemberProfile(int memberId) {
        List<String> member = new MemberRepository().getMemberById(memberId);
        if (member.isEmpty())
            System.out.println("No member found.");
        else
            System.out.println(member.get(0));
    }

    private static void updateMemberProfile(int memberId) {
        System.out.print("First name: ");
        String fn = scanner.nextLine();
        System.out.print("Last name: ");
        String ln = scanner.nextLine();
        System.out.print("Date of birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Gender: ");
        String g = scanner.nextLine();
        System.out.print("Email: ");
        String em = scanner.nextLine();
        System.out.print("Phone: ");
        String ph = scanner.nextLine();
        memberService.updateProfile(memberId, fn, ln, dob, g, em, ph);

    }

    private static void addFitnessGoal(int memberId) {
        System.out.print("Goal type: ");
        String type = scanner.nextLine();
        System.out.print("Target value: ");
        double val = readDouble();
        System.out.print("Unit: ");
        String unit = scanner.nextLine();
        System.out.print("Target date (YYYY-MM-DD or blank): ");
        String date = scanner.nextLine();
        if (date.isBlank()) date = null;
        memberService.addFitnessGoal(memberId, type, val, unit, date);
    }

    private static void updateGoalStatus() {
        System.out.print("Goal ID: ");
        int id = readInt();
        System.out.print("New Status (Active / Completed / Cancelled): ");
        String status = scanner.nextLine();
        memberService.updateFitnessGoalStatus(id, status);
    }

    private static void addHealthMetric(int memberId) {
        System.out.print("Height: ");
        double h = readDouble();
        System.out.print("Weight: ");
        double w = readDouble();
        System.out.print("Heart rate: ");
        int hr = readInt();
        System.out.print("Body fat %: ");
        double bf = readDouble();
        memberService.addHealthMetric(memberId, h, w, hr, bf);
    }

    private static void bookPt(int memberId) {
        System.out.print("Trainer ID: ");
        int trainerId = readInt();
        System.out.print("Room ID: ");
        int roomId = readInt();
        System.out.print("Start (YYYY-MM-DD HH:MM:SS): ");
        String start = scanner.nextLine();
        System.out.print("End (YYYY-MM-DD HH:MM:SS): ");
        String end = scanner.nextLine();
        memberService.schedulePtSession(memberId, trainerId, roomId, start, end);
    }

    private static void reschedulePt(int memberId) {
        System.out.print("Session ID: ");
        int sessionId = readInt();
        System.out.print("Trainer ID: ");
        int trainer = readInt();
        System.out.print("Room ID: ");
        int room = readInt();
        System.out.print("Start (YYYY-MM-DD HH:MM:SS): ");
        String start = scanner.nextLine();
        System.out.print("End (YYYY-MM-DD HH:MM:SS): ");
        String end = scanner.nextLine();
        memberService.reschedulePtSession(sessionId, memberId, trainer, room, start, end);
    }

    private static void cancelPt() {
        System.out.print("Session ID: ");
        int id = readInt();
        memberService.cancelPtSession(id);
    }

    private static void registerForClass(int memberId) {
        System.out.print("Class ID: ");
        int id = readInt();
        memberService.registerForClass(memberId, id);
    }

    // Trainer Action Methods
    private static void addTrainerAvailability(int trainerId) {
        System.out.print("Start (YYYY-MM-DD HH:MM:SS): ");
        String start = scanner.nextLine();
        System.out.print("End (YYYY-MM-DD HH:MM:SS): ");
        String end = scanner.nextLine();
        System.out.print("Recurring weekly? (true / false): ");
        boolean recurring = Boolean.parseBoolean(scanner.nextLine());
        trainerService.addAvailability(trainerId, start, end, recurring);
    }

    private static void removeTrainerAvailability() {
        System.out.print("Availability ID: ");
        int id = readInt();
        trainerService.removeAvailability(id);
    }

    // Admin Action Methods
    private static void addGroupClass() {
        System.out.print("Class name: ");
        String name = scanner.nextLine();
        System.out.print("Trainer ID: ");
        int trainer = readInt();
        System.out.print("Room ID: ");
        int room = readInt();
        System.out.print("Start (YYYY-MM-DD HH:MM:SS): ");
        String start = scanner.nextLine();
        System.out.print("Capacity: ");
        int cap = readInt();
        adminService.addGroupClass(name, trainer, room, start, cap);
    }

    private static void updateGroupClass() {
        System.out.print("Class ID: ");
        int id = readInt();
        System.out.print("Class name: ");
        String name = scanner.nextLine();
        System.out.print("Trainer ID: ");
        int trainer = readInt();
        System.out.print("Room ID: ");
        int room = readInt();
        System.out.print("Start (YYYY-MM-DD HH:MM:SS): ");
        String start = scanner.nextLine();
        System.out.print("Capacity: ");
        int cap = readInt();
        adminService.updateGroupClass(id, name, trainer, room, start, cap);
    }

    // Utilities
    private static int readInt() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid integer number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return Double.parseDouble(line.trim());
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter a valid decimal number: ");
            }
        }
    }
}
