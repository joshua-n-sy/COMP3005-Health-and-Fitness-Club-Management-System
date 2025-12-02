package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to personal training sessions.
 */
public class PtSessionRepository {
    /**
     * Adds a personal training session.
     * 
     * @param memberId  the member's ID
     * @param trainerId the trainer's ID
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the end time (YYYY-MM-DD HH:MM:SS)
     */
    public void addPtSession(int memberId, int trainerId, int roomId, String startTime, String endTime) {
        String sql = "INSERT INTO pt_session (member_id, trainer_id, room_id, session_start, session_end) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, trainerId);
            pstmt.setInt(3, roomId);
            pstmt.setTimestamp(4, Timestamp.valueOf(startTime));
            pstmt.setTimestamp(5, Timestamp.valueOf(endTime));
            pstmt.executeUpdate();
            System.out.println("PT session booked successfully!");
        } catch (SQLException e) {
            System.out.println("Error booking PT session: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the time range of a personal training session.
     * 
     * @param sessionId the session ID
     * @param startTime the new start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the new end time (YYYY-MM-DD HH:MM:SS)
     */
    public void updatePtSessionTime(int sessionId, String startTime, String endTime) {
        String sql = "UPDATE pt_session SET session_start = ?, session_end = ? WHERE pt_session_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(startTime));
            pstmt.setTimestamp(2, Timestamp.valueOf(endTime));
            pstmt.setInt(3, sessionId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("PT session rescheduled successfully!");
            } else {
                System.out.println("No PT session found with ID: " + sessionId);
            }
        } catch (SQLException e) {
            System.out.println("Error rescheduling PT session: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the status of a personal training session.
     * 
     * @param sessionId the session ID
     * @param status    the new status (Booked, Cancelled, Completed)
     */
    public void updatePtSessionStatus(int sessionId, String status) {
        String sql = "UPDATE pt_session SET status = ? WHERE pt_session_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, sessionId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("PT session status updated successfully!");
            } else {
                System.out.println("No PT session found with ID: " + sessionId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating PT session status: ");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all PT sessions for a member.
     * 
     * @param memberId the member's ID
     * @return         a list of PT session records, or empty if none exist
     */
    public List<String> getPtSessionsForMember(int memberId) {
        List<String> sessions = new ArrayList<>();
        String sql =
            "SELECT ps.pt_session_id, ps.session_start, ps.session_end, ps.status, t.first_name, t.last_name, r.name AS room_name " +
            "FROM pt_session ps " +
            "JOIN trainer t ON ps.trainer_id = t.trainer_id " +
            "JOIN room r ON ps.room_id = r.room_id " +
            "WHERE ps.member_id = ? " +
            "ORDER BY ps.session_start";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Session " + rs.getInt("pt_session_id") + " - " +
                        rs.getTimestamp("session_start") + " to " +
                        rs.getTimestamp("session_end") + " - " +
                        rs.getString("status") + " - " +
                        "Trainer: " + rs.getString("first_name") + " " + rs.getString("last_name") + " - " +
                        "Room: " + rs.getString("room_name");
                    sessions.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving PT sessions: ");
            e.printStackTrace();
        }
        
        return sessions;
    }

    /**
     * Retrieves upcoming personal training sessions for a trainer.
     * 
     * @param trainerId the trainer's ID
     * @return          a list of upcoming PT session records, or empty if none exist
     */
    public List<String> getPtSessionsForTrainer(int trainerId) {
        List<String> sessions = new ArrayList<>();
        String sql =
            "SELECT ps.pt_session_id, ps.session_start, ps.session_end, ps.status, m.first_name, m.last_name " +
            "FROM pt_session ps " +
            "JOIN member m ON ps.member_id = m.member_id " +
            "WHERE ps.trainer_id = ? AND ps.session_start >= NOW() " +
            "ORDER BY ps.session_start";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String row =
                        "Session " + rs.getInt("pt_session_id") + " - " +
                        rs.getTimestamp("session_start") + " to " +
                        rs.getTimestamp("session_end") + " - " +
                        rs.getString("status") + " - " +
                        "Member: " + rs.getString("first_name") + " " +
                        rs.getString("last_name");

                    sessions.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving upcoming PT sessions: ");
            e.printStackTrace();
        }
        
        return sessions;
    }

    /**
     * Checks for overlapping PT sessions for a member.
     * 
     * @param memberId  the member's ID
     * @param startTime the proposed start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the proposed end time (YYYY-MM-DD HH:MM:SS)
     * @param excludeId the session ID to exclude from the check, or null if none
     * @return          true if a conflict exists, or false otherwise
     */
    public boolean hasMemberSessionConflict(int memberId, String startTime, String endTime, Integer excludeId) {
        String sql =
            "SELECT COUNT(*) FROM pt_session " +
            "WHERE member_id = ? " +
            "  AND session_start < ? " +
            "  AND session_end > ? " +
            "  AND (? IS NULL OR pt_session_id <> ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setTimestamp(2, Timestamp.valueOf(endTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(startTime));
            if (excludeId != null) {
                pstmt.setInt(4, excludeId);
                pstmt.setInt(5, excludeId);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking member session conflict: ");
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Checks for overlapping PT sessions for a trainer.
     * 
     * @param trainerId the trainer's ID
     * @param startTime the proposed start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the proposed end time (YYYY-MM-DD HH:MM:SS)
     * @param excludeId the session ID to exclude from the check, or null if none
     * @return          true if a conflict exists, or false otherwise
     */
    public boolean hasTrainerSessionConflict(int trainerId, String startTime, String endTime, Integer excludeId) {
        String sql =
            "SELECT COUNT(*) FROM pt_session " +
            "WHERE trainer_id = ? " +
            "  AND session_start < ? " +
            "  AND session_end > ? " +
            "  AND (? IS NULL OR pt_session_id <> ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, trainerId);
            pstmt.setTimestamp(2, Timestamp.valueOf(endTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(startTime));
            if (excludeId != null) {
                pstmt.setInt(4, excludeId);
                pstmt.setInt(5, excludeId);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking trainer session conflict: ");
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Checks for overlapping PT sessions for a room.
     * 
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime   the end time (YYYY-MM-DD HH:MM:SS)
     * @param excludeId the session ID to exclude from the check, or null if none
     * @return          true if a conflict exists, or false otherwise
     */
    public boolean hasRoomSessionConflict(int roomId, String startTime, String endTime, Integer excludeId) {
        String sql =
            "SELECT COUNT(*) FROM pt_session " +
            "WHERE room_id = ? " +
            "  AND session_start < ? " +
            "  AND session_end > ? " +
            "  AND (? IS NULL OR pt_session_id <> ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setTimestamp(2, Timestamp.valueOf(endTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(startTime));
            if (excludeId != null) {
                pstmt.setInt(4, excludeId);
                pstmt.setInt(5, excludeId);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking room session conflict: ");
            e.printStackTrace();
        }
        
        return false;
    }
}
