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
 * Handles database operations related to equipment.
 */
public class EquipmentRepository {
    /**
     * Retrieves all equipment items.
     * 
     * @return a list of equipment records, or empty if none exist
     */
    public List<String> getAllEquipment() {
        List<String> equipmentList = new ArrayList<>();
        String sql =
            "SELECT e.equipment_id, e.name, e.equipment_type, e.status, e.room_id, r.name AS room_name " +
            "FROM equipment e " +
            "LEFT JOIN room r ON e.room_id = r.room_id " +
            "ORDER BY e.equipment_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    "Equipment " + rs.getInt("equipment_id") + " - " +
                    rs.getString("name") + " (" + rs.getString("equipment_type") + ") - " +
                    "Status: " + rs.getString("status") + " - " +
                    "Room: " + (rs.getObject("room_id") == null
                        ? "Unassigned"
                        : rs.getInt("room_id") + " (" + rs.getString("room_name") + ")");
                equipmentList.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving equipment: ");
            e.printStackTrace();
        }

        return equipmentList;
    }

    /**
     * Retrieves a single equipment item by ID.
     * 
     * @param equipmentId the equipment ID
     * @return            a list of the equipment record, or empty if none exist
     */
    public List<String> getEquipmentById(int equipmentId) {
        List<String> equipment = new ArrayList<>();
        String sql =
            "SELECT e.equipment_id, e.name, e.equipment_type, e.status, e.room_id, r.name AS room_name " +
            "FROM equipment e " +
            "LEFT JOIN room r ON e.room_id = r.room_id " +
            "WHERE e.equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String row =
                        "Equipment " + rs.getInt("equipment_id") + " - " +
                        rs.getString("name") + " (" + rs.getString("equipment_type") + ") - " +
                        "Status: " + rs.getString("status") + " - " +
                        "Room: " + (rs.getObject("room_id") == null
                            ? "Unassigned"
                            : rs.getInt("room_id") + " (" + rs.getString("room_name") + ")");
                    equipment.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving equipment by ID: ");
            e.printStackTrace();
        }

        return equipment;
    }

    /**
     * Adds an equipment item.
     * 
     * @param roomId        the room ID
     * @param name          the equipment name
     * @param equipmentType the equipment type
     */
    public void addEquipment(Integer roomId, String name, String equipmentType) {
        String sql = "INSERT INTO equipment (room_id, name, equipment_type) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (roomId != null) {
                pstmt.setInt(1, roomId);
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }

            pstmt.setString(2, name);
            pstmt.setString(3, equipmentType);
            pstmt.executeUpdate();
            System.out.println("Equipment added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding equipment: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of an equipment item.
     * 
     * @param equipmentId the equipment ID
     * @param status      the new status (Operational, OutOfOrder)
     */
    public void updateEquipmentStatus(int equipmentId, String status) {
        String sql = "UPDATE equipment SET status = ? WHERE equipment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, equipmentId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Equipment status updated successfully!");
            } else {
                System.out.println("No equipment found with ID: " + equipmentId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating equipment status: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Adds a new equipment issue.
     * 
     * @param equipmentId the equipment  ID
     * @param adminId     the ID of the admin reporting the issue
     * @param description a description of the issue
     */
    public void addEquipmentIssue(int equipmentId, int adminId, String description) {
        String sql = "INSERT INTO equipment_issue (equipment_id, reported_by_admin_id, description) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
            pstmt.setInt(2, adminId);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            System.out.println("Equipment issue logged successfully!");
        } catch (SQLException e) {
            System.out.println("Error logging equipment issue: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of an equipment issue.
     * 
     * @param issueId    the issue ID
     * @param status     the new status (Open, InProgress, Resolved)
     * @param resolvedAt the resolution timestamp (YYYY-MM-DD HH:MM:SS), or null if not resolved
     */
    public void updateEquipmentIssueStatus(int issueId, String status, String resolvedAt) {
        String sql = "UPDATE equipment_issue SET status = ?, resolved_at = ? WHERE issue_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);

            if (resolvedAt != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(resolvedAt));
            } else {
                pstmt.setNull(2, java.sql.Types.TIMESTAMP);
            }

            pstmt.setInt(3, issueId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Equipment issue status updated successfully!");
            } else {
                System.out.println("No equipment issue found with ID: " + issueId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating equipment issue status: ");
            e.printStackTrace();
        }
    }
    
    /**
     * Retrieves all open equipment issues.
     * 
     * @return a list of open equipment issue records, or empty if none exist
     */
    public List<String> getOpenEquipmentIssues() {
        List<String> issues = new ArrayList<>();
        String sql =
            "SELECT ei.issue_id, ei.equipment_id, e.name AS equipment_name, ei.reported_at, ei.status, ei.description, ei.resolved_at " +
            "FROM equipment_issue ei " +
            "JOIN equipment e ON ei.equipment_id = e.equipment_id " +
            "WHERE ei.status <> 'Resolved' " +
            "ORDER BY ei.reported_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    "Issue " + rs.getInt("issue_id") + " - " +
                    "Equipment " + rs.getInt("equipment_id") + " (" + rs.getString("equipment_name") + ") - " +
                    "Reported: " + rs.getTimestamp("reported_at") + " - " +
                    "Status: " + rs.getString("status") + " - " +
                    "Description: " + rs.getString("description") + " - " +
                    "Resolved at: " + rs.getTimestamp("resolved_at");
                issues.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving open equipment issues: ");
            e.printStackTrace();
        }

        return issues;
    }

    /**
     * Retrieves all issues for a specific equipment item.
     * 
     * @param equipmentId the equipment ID
     * @return            a list of issue records for the equipment, or empty if none exist
     */
    public List<String> getIssuesForEquipment(int equipmentId) {
        List<String> issues = new ArrayList<>();
        String sql =
            "SELECT issue_id, reported_at, status, description, resolved_at " +
            "FROM equipment_issue " +
            "WHERE equipment_id = ? " +
            "ORDER BY reported_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Issue " + rs.getInt("issue_id") + " - " +
                        "Reported: " + rs.getTimestamp("reported_at") + " - " +
                        "Status: " + rs.getString("status") + " - " +
                        "Description: " + rs.getString("description") + " - " +
                        "Resolved at: " + rs.getTimestamp("resolved_at");
                    issues.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving issues for equipment: ");
            e.printStackTrace();
        }

        return issues;
    }
}
