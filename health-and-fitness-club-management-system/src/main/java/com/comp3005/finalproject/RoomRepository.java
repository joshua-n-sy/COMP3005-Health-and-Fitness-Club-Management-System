package com.comp3005.finalproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to rooms.
 */
public class RoomRepository {
    /**
     * Retrieves all rooms.
     * 
     * @return a list of room records, or empty if none exist
     */
    public List<String> getAllRooms() {
        List<String> rooms = new ArrayList<>();
        String sql = "SELECT * FROM room ORDER BY room_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String row =
                    "Room " + rs.getInt("room_id") + " - " +
                    rs.getString("name") + " - " +
                    "Capacity: " + rs.getInt("capacity");
                rooms.add(row);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving rooms: ");
            e.printStackTrace();
        }

        return rooms;
    }

    /**
     * Retrieves a room by ID.
     * 
     * @param roomId the room ID
     * @return       a list of the room details, or empty if none exist
     */
    public List<String> getRoomById(int roomId) {
        List<String> room = new ArrayList<>();
        String sql = "SELECT * FROM room WHERE room_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String row =
                        "Room " + rs.getInt("room_id") + " - " +
                        rs.getString("name") + " - " +
                        "Capacity: " + rs.getInt("capacity");
                    room.add(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving room by ID: ");
            e.printStackTrace();
        }

        return room;
    }

    /**
     * Adds a new room.
     * 
     * @param name     the room name
     * @param capacity the room capacity
     */
    public void addRoom(String name, int capacity) {
        String sql = "INSERT INTO room (name, capacity) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, capacity);
            pstmt.executeUpdate();
            System.out.println("Room added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding room: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the capacity of a room.
     * 
     * @param roomId      the room ID
     * @param newCapacity the new room capacity
     */
    public void updateRoomCapacity(int roomId, int newCapacity) {
        String sql = "UPDATE room SET capacity = ? WHERE room_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newCapacity);
            pstmt.setInt(2, roomId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Room capacity updated successfully!");
            } else {
                System.out.println("No room found with ID: " + roomId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating room capacity: ");
            e.printStackTrace();
        }
    }

    /**
     * Updates the name of a room.
     * 
     * @param roomId  the room ID
     * @param newName the new room name
     */
    public void updateRoomName(int roomId, String newName) {
        String sql = "UPDATE room SET name = ? WHERE room_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, roomId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Room name updated successfully!");
            } else {
                System.out.println("No room found with ID: " + roomId);
            }
        } catch (SQLException e) {
            System.out.println("Error updating room name: ");
            e.printStackTrace();
        }
    }
}
