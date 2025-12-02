package com.comp3005.finalproject;

import java.util.List;

/**
 * Handles admin-facing operations using the admin-related repositories.
 */
public class AdminService {
    private final EquipmentRepository equipmentRepository;
    private final RoomRepository roomRepository;
    private final ClassRepository classRepository;

    public AdminService() {
        this.equipmentRepository = new EquipmentRepository();
        this.roomRepository = new RoomRepository();
        this.classRepository = new ClassRepository();
    }

    public AdminService(EquipmentRepository equipmentRepository, RoomRepository roomRepository, ClassRepository classRepository) {
        this.equipmentRepository = equipmentRepository;
        this.roomRepository = roomRepository;
        this.classRepository = classRepository;
    }
    
    /**
     * Displays all rooms in the facility.
     */
    public void showAllRooms() {
        List<String> rooms = roomRepository.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }

        System.out.println("\nRooms:");
        for (String r : rooms) {
            System.out.println(r);
        }
    }

    /**
     * Add a room.
     * 
     * @param name     the room name
     * @param capacity the room capacity
     */
    public void addRoom(String name, int capacity) {
        roomRepository.addRoom(name, capacity);
    }

    /**
     * Update the name of a room.
     * 
     * @param roomId  the room ID
     * @param newName the new name of the room
     */
    public void updateRoomName(int roomId, String newName) {
        roomRepository.updateRoomName(roomId, newName);
    }

    /**
     * Update the room capacity.
     * 
     * @param roomId      the room ID
     * @param newCapacity the new capacity of the room
     */
    public void updateRoomCapacity(int roomId, int newCapacity) {
        roomRepository.updateRoomCapacity(roomId, newCapacity);
    }

    /**
     * Displays all equipment items.
     */
    public void showEquipment() {
        List<String> equipment = equipmentRepository.getAllEquipment();
        if (equipment.isEmpty()) {
            System.out.println("No equipment found.");
            return;
        }

        System.out.println("\nEquipment:");
        for (String e : equipment) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new equipment item.
     * 
     * @param roomId the room ID
     * @param name   the equipment name
     * @param type   the equipment type
     */
    public void addEquipment(Integer roomId, String name, String type) {
        equipmentRepository.addEquipment(roomId, name, type);
    }

    /**
     * Updates an equipment's operational status.
     * 
     * @param equipmentId the equipement ID
     * @param status      the equipement's operational status
     */
    public void updateEquipmentStatus(int equipmentId, String status) {
        equipmentRepository.updateEquipmentStatus(equipmentId, status);
    }

    /**
     * Records a new equipment issue.
     * 
     * @param equipmentId the equipment ID
     * @param adminId     the admin ID reporting the issue
     * @param description a description of the issue
     */
    public void logEquipmentIssue(int equipmentId, int adminId, String description) {
        equipmentRepository.addEquipmentIssue(equipmentId, adminId, description);
    }

    /**
     * Updates the status of an equipment issue.
     * 
     * @param issueId    the issue ID
     * @param status     the new status (Open, InProgress, Resolved)
     * @param resolvedAt the timestamp of resolution (YYYY-MM-DD HH:MM:SS), or null if unresolved
     */
    public void updateEquipmentIssueStatus(int issueId, String status, String resolvedAt) {
        equipmentRepository.updateEquipmentIssueStatus(issueId, status, resolvedAt);
    }

    /**
     * Displays a list of all open equipment issues.
     */
    public void showOpenEquipmentIssues() {
        List<String> issues = equipmentRepository.getOpenEquipmentIssues();
        if (issues.isEmpty()) {
            System.out.println("No open equipment issues.");
            return;
        }

        System.out.println("\nOpen Equipment Issues:");
        for (String issue : issues) {
            System.out.println(issue);
        }
    }

    /**
     * Displays all issues related to a specific equipment.
     * 
     * @param equipmentId the equipment ID
     */
    public void showIssuesForEquipment(int equipmentId) {
        List<String> issues = equipmentRepository.getIssuesForEquipment(equipmentId);
        if (issues.isEmpty()) {
            System.out.println("No issues found for equipment " + equipmentId);
            return;
        }

        System.out.println("\nIssues for equipment " + equipmentId + ":");
        for (String issue : issues) {
            System.out.println(issue);
        }
    }

    /**
     * Displays all group fitness classes.
     */
    public void showAllClasses() {
        List<String> classes = classRepository.getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("No classes found.");
            return;
        }

        System.out.println("\nGroup Fitness Classes:");
        for (String c : classes) {
            System.out.println(c);
        }
    }

    /**
     * Adds a new group fitness class.
     * 
     * @param className the class name
     * @param trainerId the class name
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param capacity  the class capacity
     */
    public void addGroupClass(String className, int trainerId, int roomId, String startTime, int capacity) {
        classRepository.addGroupClass(className, trainerId, roomId, startTime, capacity);
    }

    /**
     * Updates an existing group fitness class.
     * 
     * @param classId   the group fitness class ID
     * @param className the group fitness class name
     * @param trainerId the trainer's ID
     * @param roomId    the room ID
     * @param startTime the start time (YYYY-MM-DD HH:MM:SS)
     * @param capacity  the class capacity
     */
    public void updateGroupClass(int classId, String className, int trainerId, int roomId, String startTime, int capacity) {
        classRepository.updateGroupClass(classId, className, trainerId, roomId, startTime, capacity);
    }

    /**
     * Cancels a group fitness class.
     * 
     * @param classId the group fitness class ID
     */
    public void cancelGroupClass(int classId) {
        classRepository.cancelGroupClass(classId);
    }
}
