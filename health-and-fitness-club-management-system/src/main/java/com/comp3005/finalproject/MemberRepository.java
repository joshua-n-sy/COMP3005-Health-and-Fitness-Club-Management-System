package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to members.
 */
public class MemberRepository {
    /**
     * Retrieves all members.
     * 
     * @return a list of member records, or empty if none exist
     */
    public List<String> getAllMembers() {
        List<String> members = new ArrayList<>();
        String sql = "SELECT * FROM member ORDER BY member_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    rs.getInt("member_id") + " - " +
                    rs.getString("first_name") + " " +
                    rs.getString("last_name") + " - " +
                    rs.getString("email") + " - " +
                    rs.getDate("dob") + " - " +
                    rs.getString("gender");
                members.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving members: ");
            e.printStackTrace();
        }
        
        return members;
    }

    /**
     * Retrieves a member based on their member ID.
     *
     * @param memberId the member's ID
     * @return         a list of the member's details, or empty if none exist
     */
    public List<String> getMemberById(int memberId) {
        List<String> member = new ArrayList<>();
        String sql = "SELECT * FROM member WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String row =
                        rs.getInt("member_id") + " - " +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + " - " +
                        rs.getString("email") + " - " +
                        rs.getDate("dob") + " - " +
                        rs.getString("gender") + " - " +
                        rs.getString("phone");
                    member.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving member by ID: ");
            e.printStackTrace();
        }
        
        return member;
    }

    /**
     * Adds a new member.
     * 
     * @param firstName the member's first name
     * @param lastName  the member's last name
     * @param dob       the member's date of birth (YYYY-MM-DD)
     * @param gender    the member's gender (Male, Female, Other)
     * @param email     the member's email address
     * @param phone     the member's phone number
     */
    public void addMember(String firstName, String lastName, String dob, String gender, String email, String phone) {
        String sql = "INSERT INTO member (first_name, last_name, dob, gender, email, phone) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setDate(3, java.sql.Date.valueOf(dob));
            pstmt.setString(4, gender);
            pstmt.setString(5, email);
            pstmt.setString(6, phone);
            pstmt.executeUpdate();
            System.out.println("Member added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding member: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates a member's details based on their member ID.
     *
     * @param memberId  the member's ID
     * @param firstName the member's first name
     * @param lastName  the member's last name
     * @param dob       the member's date of birth (YYYY-MM-DD)
     * @param gender    the member's gender (Male, Female, Other)
     * @param email     the member's email address
     * @param phone     the member's phone number
     */
    public void updateMember(int memberId, String firstName, String lastName, String dob, String gender, String email, String phone) {
        String sql = "UPDATE member SET first_name = ?, last_name = ?, dob = ?, gender = ?, email = ?, phone = ? WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setDate(3, java.sql.Date.valueOf(dob));
            pstmt.setString(4, gender);
            pstmt.setString(5, email);
            pstmt.setString(6, phone);
            pstmt.setInt(7, memberId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Member details updated successfully!");
            } else {
                System.out.println("No member found with ID: " + memberId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating member details: ");
            e.printStackTrace();
        }
    }

    /**
     * Adds a fitness goal.
     * 
     * @param memberId    the member's ID
     * @param goalType    the type of fitness goal
     * @param targetValue the numeric target value for the goal
     * @param unit        the unit associated with the target value
     * @param targetDate  the target completion date for the goal, or null if not specified
     */
    public void addFitnessGoal(int memberId, String goalType, Double targetValue, String unit, String targetDate) {
        String sql = "INSERT INTO fitness_goal (member_id, goal_type, target_value, unit, target_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setString(2, goalType);
            pstmt.setDouble(3, targetValue);
            pstmt.setString(4, unit);

            if (targetDate != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(targetDate));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }

            pstmt.executeUpdate();
            System.out.println("Fitness goal added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding fitness goal: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of a fitness goal.
     * 
     * @param goalId the fitness goal ID
     * @param status the new status value (Active, Completed, Cancelled)
     */
    public void updateFitnessGoalStatus(int goalId, String status) {
        String sql = "UPDATE fitness_goal SET status = ? WHERE goal_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, goalId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Fitness goal status updated successfully!");
            } else {
                System.out.println("No fitness goal found with ID: " + goalId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating fitness goal status: ");
            e.printStackTrace();
        }
    }

    /**
     *  Retrieves all fitness goals for a member.
     *  
     * @param memberId the member's ID
     * @return         a list of fitness goal records, or empty if none exist
     */
    public List<String> getFitnessGoalsForMember(int memberId) {
        List<String> goals = new ArrayList<>();
        String sql = "SELECT * FROM fitness_goal WHERE member_id = ? ORDER BY start_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Goal " + rs.getInt("goal_id") + " - " +
                        rs.getString("goal_type") + " " +
                        rs.getDouble("target_value") + " " +
                        rs.getString("unit") + " - " +
                        "status: " + rs.getString("status") + " - " +
                        "start: " + rs.getDate("start_date") + ", target: " + rs.getDate("target_date");
                    goals.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving fitness goals: ");
            e.printStackTrace();
        }
        
        return goals;
    }

    /**
     * Adds a health metric.
     * 
     * @param memberId  the member's ID
     * @param height    the member's height
     * @param weight    the member's weight
     * @param heartRate the member's heart rate
     * @param bodyFat   the member's body fat percentage
     */
    public void addHealthMetric(int memberId, double height, double weight, int heartRate, double bodyFat) {
        String sql = "INSERT INTO health_metric (member_id, height, weight, heart_rate, body_fat) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setDouble(2, height);
            pstmt.setDouble(3, weight);
            pstmt.setInt(4, heartRate);
            pstmt.setDouble(5, bodyFat);
            pstmt.executeUpdate();
            System.out.println("Health metric added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding health metric: ");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all health metric entries for a member.
     * 
     * @param memberId the member's ID
     * @return         a list of health metric records, or empty if none exist
     */
    public List<String> getHealthMetricsForMember(int memberId) {
        List<String> metrics = new ArrayList<>();
        String sql = "SELECT * FROM health_metric WHERE member_id = ? ORDER BY measure_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        rs.getTimestamp("measure_time") + " - " +
                        "H: " + rs.getDouble("height") +
                        ", W: " + rs.getDouble("weight") +
                        ", HR: " + rs.getInt("heart_rate") +
                        ", BF: " + rs.getDouble("body_fat");
                    metrics.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving health metrics: ");
            e.printStackTrace();
        }
        
        return metrics;
    }

    /**
     * Retrieves the latest health metric entry for a member.
     * 
     * @param memberId the member's ID
     * @return         a list of the most recent health metric record, or empty if none exist
     */
    public List<String> getLatestHealthMetric(int memberId) {
        List<String> metrics = new ArrayList<>();
        String sql = "SELECT * FROM health_metric WHERE member_id = ? ORDER BY measure_time DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String row =
                        rs.getTimestamp("measure_time") + " - " +
                        "H: " + rs.getDouble("height") +
                        ", W: " + rs.getDouble("weight") +
                        ", HR: " + rs.getInt("heart_rate") +
                        ", BF: " + rs.getDouble("body_fat");
                    metrics.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving latest health metric: ");
            e.printStackTrace();
        }
        
        return metrics;
    }
}
