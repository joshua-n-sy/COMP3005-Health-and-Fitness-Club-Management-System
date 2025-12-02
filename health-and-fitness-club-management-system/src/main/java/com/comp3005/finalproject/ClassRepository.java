package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to group fitness classes.
 */
public class ClassRepository {
    /**
     * Retrieves all group fitness classes.
     * 
     * @return a list of group fitness class records, or empty if none exist
     */
    public List<String> getAllClasses() {
        List<String> classes = new ArrayList<>();
        String sql =
            "SELECT gc.class_id, gc.name, gc.description, gc.start_time, gc.end_time, " +
            "gc.capacity, gc.status, t.first_name AS trainer_first, " +
            "t.last_name AS trainer_last, r.name AS room_name " +
            "FROM \"group_class\" gc " +
            "JOIN trainer t ON gc.trainer_id = t.trainer_id " +
            "JOIN room r ON gc.room_id = r.room_id " +
            "ORDER BY gc.start_time";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    "Class " + rs.getInt("class_id") + " - " +
                    rs.getString("name") + " - " +
                    rs.getTimestamp("start_time") + " to " +
                    rs.getTimestamp("end_time") + " - " +
                    "capacity: " + rs.getInt("capacity") + " - " +
                    "status: " + rs.getString("status") + " - " +
                    "trainer: " + rs.getString("trainer_first") + " " + rs.getString("trainer_last") + " - " +
                    "room: " + rs.getString("room_name");
                classes.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving group fitness classes: ");
            e.printStackTrace();
        }

        return classes;
    }
    
    /**
     * Retrieves upcoming group fitness classes.
     * 
     * @return a list of upcoming group fitness class records, or empty if none exist
     */
    public List<String> getUpcomingClasses() {
        List<String> classes = new ArrayList<>();
        String sql =
            "SELECT gc.class_id, gc.name, gc.start_time, gc.end_time, r.name AS room_name " +
            "FROM group_class gc " +
            "JOIN room r ON gc.room_id = r.room_id " +
            "WHERE gc.start_time >= NOW() AND gc.status = 'Scheduled' " +
            "ORDER BY gc.start_time";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    "ID: " + rs.getInt("class_id") +
                    " | " + rs.getString("name") +
                    " | " + rs.getTimestamp("start_time") + " to " + rs.getTimestamp("end_time") +
                    " | Room: " + rs.getString("room_name");
                classes.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving upcoming group fitness classes: ");
            e.printStackTrace();
        }

        return classes;
    }
    
    /**
     * Retrieves group fitness classes assigned to a specific trainer.
     * 
     * @param trainerId the trainer's ID
     * @return          a list of group fitness class records for that trainer, or empty if none exist
     */
    public List<String> getClassesForTrainer(int trainerId) {
        List<String> classes = new ArrayList<>();
        String sql =
            "SELECT gc.class_id, gc.name, gc.start_time, gc.end_time, gc.status, r.name AS room_name " +
            "FROM group_class gc " +
            "JOIN room r ON gc.room_id = r.room_id " +
            "WHERE gc.trainer_id = ? " +
            "ORDER BY gc.start_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Class " + rs.getInt("class_id") +
                        " | " + rs.getString("name") +
                        " | " + rs.getTimestamp("start_time") + " to " + rs.getTimestamp("end_time") +
                        " | Room: " + rs.getString("room_name") +
                        " | Status: " + rs.getString("status");
                    classes.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving trainer classes: ");
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * Adds a new group fitness class.
     * 
     * @param className the class name
     * @param trainerId the trainer's ID
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param capacity  the class capacity
     */
    public void addGroupClass(String className, int trainerId, int roomId, String startTime, int capacity) {
        String sql = "INSERT INTO group_class (trainer_id, room_id, name, description, start_time, end_time, capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Assume a default duration of 1 hour and compute end time
            LocalDateTime start = Timestamp.valueOf(startTime).toLocalDateTime();
            LocalDateTime end   = start.plusHours(1);

            pstmt.setInt(1, trainerId);
            pstmt.setInt(2, roomId);
            pstmt.setString(3, className);
            pstmt.setString(4, null);
            pstmt.setTimestamp(5, Timestamp.valueOf(start));
            pstmt.setTimestamp(6, Timestamp.valueOf(end));
            pstmt.setInt(7, capacity);
            pstmt.executeUpdate();
            System.out.println("Group fitness class added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding group fitness class: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates a group fitness class.
     * 
     * @param classId   the group fitness class ID
     * @param className the group fitness class name
     * @param trainerId the trainer's ID
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param capacity  the class capacity
     */
    public void updateGroupClass(int classId, String className, int trainerId, int roomId, String startTime, int capacity) {
        String sql =
            "UPDATE group_class " +
            "SET name = ?, trainer_id = ?, room_id = ?, start_time = ?, end_time = ?, capacity = ? " +
            "WHERE class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Assume a default duration of 1 hour and compute end time
            LocalDateTime start = Timestamp.valueOf(startTime).toLocalDateTime();
            LocalDateTime end   = start.plusHours(1);

            pstmt.setString(1, className);
            pstmt.setInt(2, trainerId);
            pstmt.setInt(3, roomId);
            pstmt.setTimestamp(4, Timestamp.valueOf(start));
            pstmt.setTimestamp(5, Timestamp.valueOf(end));
            pstmt.setInt(6, capacity);
            pstmt.setInt(7, classId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Group fitness class updated successfully!");
            } else {
                System.out.println("No group fitness class found with ID: " + classId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating group fitness class: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Cancels a group fitness class.
     * 
     * @param classId the group fitness class ID 
     */
    public void cancelGroupClass(int classId) {
        String sql = "UPDATE group_class SET status = 'Cancelled' WHERE class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, classId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Class cancelled successfully!");
            } else {
                System.out.println("No class found with ID: " + classId);
            }
        } catch (SQLException e) {
            System.out.println("Error cancelling group class: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Registers a member for a group fitness class.
     * 
     * @param memberId the member's ID
     * @param classId  the group fitness class ID
     */
    public void registerMemberForClass(int memberId, int classId) {
        String sql = "INSERT INTO class_registration (member_id, class_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            System.out.println("Member registered for group fitness class!");
        } catch (SQLException e) {
            System.out.println("Error registering member for group fitness class: ");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all class registrations for a member.
     * 
     * @param memberId the member's ID
     * @return         a list of class registration records, or empty if none exist
     */
    public List<String> getRegistrationsForMember(int memberId) {
        List<String> registrations = new ArrayList<>();
        String sql =
            "SELECT gc.name, gc.start_time, gc.end_time, gc.room_id " +
            "FROM class_registration cr " +
            "JOIN group_class gc ON cr.class_id = gc.class_id " +
            "WHERE cr.member_id = ? " +
            "ORDER BY gc.start_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        rs.getString("name") +
                        " | " + rs.getTimestamp("start_time") + " to " + rs.getTimestamp("end_time") +
                        " | Room: " + rs.getInt("room_id");
                    registrations.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving class registrations:");
            e.printStackTrace();
        }

        return registrations;
    }
    
    /**
     * Checks if a member is already registered for a group fitness class.
     * 
     * @param memberId the member's ID
     * @param classId  the group fitness class ID
     * @return         true if already registered, or false otherwise
     */
    public boolean isMemberRegisteredForClass(int memberId, int classId) {
        String sql = "SELECT 1 FROM class_registration WHERE member_id = ? AND class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, classId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking class registration: ");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks whether a class is full.
     * 
     * @param classId the group fitness class ID
     * @return        true if full, or false otherwise
     */
    public boolean isClassFull(int classId) {
        String sql = "SELECT capacity <= (SELECT COUNT(*) FROM class_registration WHERE class_id = ?) AS full FROM group_class WHERE class_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, classId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("full");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking class capacity: ");
            e.printStackTrace();
        }

        return false;
    }
}
