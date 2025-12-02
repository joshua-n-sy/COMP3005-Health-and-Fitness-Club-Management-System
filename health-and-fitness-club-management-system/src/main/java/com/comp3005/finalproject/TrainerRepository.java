package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to trainers.
 */
public class TrainerRepository {
    /**
     * Retrieves all trainers.
     * 
     * @return a list of trainer records, or empty if none exist
     */
    public List<String> getAllTrainers() {
        List<String> trainers = new ArrayList<>();
        String sql = "SELECT * FROM trainer ORDER BY trainer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    rs.getInt("trainer_id") + " - " +
                    rs.getString("first_name") + " " +
                    rs.getString("last_name") + " - " +
                    rs.getString("email") + " - " +
                    rs.getString("phone");
                trainers.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving trainers: ");
            e.printStackTrace();
        }
        
        return trainers;
    }

    /**
     * Retrieves a trainer based on their trainer ID.
     * 
     * @param trainerId the trainer's ID
     * @return          a list of a trainer's records, or empty if none exist
     */
    public List<String> getTrainerById(int trainerId) {
        List<String> trainer = new ArrayList<>();
        String sql = "SELECT * FROM trainer WHERE trainer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String row =
                        rs.getInt("trainer_id") + " - " +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + " - " +
                        rs.getString("email") + " - " +
                        rs.getString("phone");
                    trainer.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving trainer by ID: ");
            e.printStackTrace();
        }
        
        return trainer;
    }

    /**
     * Adds an availability time slot.
     * 
     * @param trainerId   the trainer's ID
     * @param startTime   the start time of the availability (YYYY-MM-DD HH:MM:SS)
     * @param endTime     the end time of the availability (YYYY-MM-DD HH:MM:SS)
     * @param isRecurring whether the availability recurs weekly (True, False)
     */
    public void addTrainerAvailability(int trainerId, String startTime, String endTime, boolean isRecurring) {
        String sql = "INSERT INTO trainer_availability (trainer_id, start_time, end_time, is_recurring) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            pstmt.setTimestamp(2, Timestamp.valueOf(startTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(endTime));
            pstmt.setBoolean(4, isRecurring);
            pstmt.executeUpdate();
            System.out.println("Trainer availability added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding trainer availability: ");
            e.printStackTrace();
        }
    }

    /**
     * Deletes an availability time slot.
     * 
     * @param availabilityId the availability time slot's ID
     */
    public void deleteTrainerAvailability(int availabilityId) {
        String sql = "DELETE FROM trainer_availability WHERE availability_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, availabilityId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Trainer availability deleted successfully!");
            } else {
                System.out.println("No availability found with ID: " + availabilityId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting trainer availability: ");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all availability time slots.
     * 
     * @param trainerId the trainer's ID
     * @return          a list of availability records, or empty if none exist
     */
    public List<String> getTrainerAvailability(int trainerId) {
        List<String> slots = new ArrayList<>();
        String sql = "SELECT * FROM trainer_availability WHERE trainer_id = ? ORDER BY start_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        rs.getInt("availability_id") + " - " +
                        rs.getTimestamp("start_time") + " to " +
                        rs.getTimestamp("end_time") + " - " +
                        "recurring: " + rs.getBoolean("is_recurring");
                    slots.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving trainer availability: ");
            e.printStackTrace();
        }
        
        return slots;
    }

    /**
     * Retrieves all availability time slots within a time range.
     * 
     * @param trainerId the trainer's ID
     * @param startTime the start time of the availability (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the end time of the availability (YYYY-MM-DD HH:MM:SS)
     * @return          a list of availability records within a time range, or empty if none exist
     */
    public List<String> getTrainerAvailabilityInRange(int trainerId, String startTime, String endTime) {
        List<String> slots = new ArrayList<>();
        String sql =
            "SELECT availability_id, start_time, end_time, is_recurring " +
            "FROM trainer_availability " +
            "WHERE trainer_id = ? " +
            "  AND start_time < ? " +
            "  AND end_time > ? " +
            "ORDER BY start_time";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(endTime));
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(startTime));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Availability " + rs.getInt("availability_id") + " - " +
                        rs.getTimestamp("start_time") + " to " +
                        rs.getTimestamp("end_time") + " - " +
                        "recurring: " + rs.getBoolean("is_recurring");
                    slots.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving trainer availability in range: ");
            e.printStackTrace();
        }
        
        return slots;
    }
}
