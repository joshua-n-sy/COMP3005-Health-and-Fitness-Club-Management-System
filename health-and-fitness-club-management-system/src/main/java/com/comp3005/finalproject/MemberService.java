package com.comp3005.finalproject;

import java.util.List;

/**
 * Handles member-facing operations using the member-related repositories.
 */
public class MemberService {
    private final MemberRepository memberRepository;
    private final PtSessionRepository ptSessionRepository;
    private final ClassRepository classRepository;

    public MemberService() {
        this.memberRepository = new MemberRepository();
        this.ptSessionRepository = new PtSessionRepository();
        this.classRepository = new ClassRepository();
    }

    public MemberService(MemberRepository memberRepository, PtSessionRepository ptSessionRepository, ClassRepository classRepository) {
        this.memberRepository = memberRepository;
        this.ptSessionRepository = ptSessionRepository;
        this.classRepository = classRepository;
    }

    /**
     * Registers a new member.
     * 
     * @param firstName the member's first name
     * @param lastName  the member's last name
     * @param dob       the member's date of birth (YYYY-MM-DD)
     * @param gender    the member's gender
     * @param email     the member's email address
     * @param phone     the member's phone number
     */
    public void registerMember(String firstName, String lastName, String dob, String gender, String email, String phone) {
        memberRepository.addMember(firstName, lastName, dob, gender, email, phone);
    }

    /**
     * Updates a member's profile details.
     * 
     * @param memberId  the member's ID
     * @param firstName the member's first name
     * @param lastName  the member's last name
     * @param dob       the member's date of birth (YYYY-MM-DD)
     * @param gender    the member's gender
     * @param email     the member's email address
     * @param phone     the member's phone number
     */
    public void updateProfile(int memberId, String firstName, String lastName, String dob, String gender, String email, String phone) {
        memberRepository.updateMember(memberId, firstName, lastName, dob, gender, email, phone);
    }

    /**
     * Adds a new fitness goal for a member.
     * 
     * @param memberId    the member's ID
     * @param goalType    the type of fitness goal
     * @param targetValue the numeric target value
     * @param unit        the unit of the target value
     * @param targetDate  the target completion date (YYYY-MM-DD), or null if not set
     */
    public void addFitnessGoal(int memberId, String goalType, Double targetValue, String unit, String targetDate) {
        memberRepository.addFitnessGoal(memberId, goalType, targetValue, unit, targetDate);
    }

    /**
     * Updates the status of an existing fitness goal.
     * 
     * @param goalId the goal ID
     * @param status the new status (Active, Completed, Cancelled)
     */
    public void updateFitnessGoalStatus(int goalId, String status) {
        memberRepository.updateFitnessGoalStatus(goalId, status);
    }

    /**
     * Adds a new health metric entry for a member.
     * 
     * @param memberId  the member's ID
     * @param height    the member's height
     * @param weight    the member's weight
     * @param heartRate the member's heart rate
     * @param bodyFat   the member's body fat percentage
     */
    public void addHealthMetric(int memberId, double height, double weight, int heartRate, double bodyFat) {
        memberRepository.addHealthMetric(memberId, height, weight, heartRate, bodyFat);
    }

    /**
     * Displays all health metric records for a member.
     * 
     * @param memberId the member's ID
     */
    public void showHealthHistory(int memberId) {
        List<String> metrics = memberRepository.getHealthMetricsForMember(memberId);
        if (metrics.isEmpty()) {
            System.out.println("No health metrics found for member ID: " + memberId);
            return;
        }

        System.out.println("Health history for member ID: " + memberId);
        for (String m : metrics) {
            System.out.println(m);
        }
    }

    /**
     * Schedules a personal training session for a member.
     * 
     * @param memberId     the member's ID
     * @param trainerId    the trainer's ID
     * @param roomId       the room ID
     * @param sessionStart the start time (YYYY-MM-DD HH:MM:SS)
     * @param sessionEnd   the end time (YYYY-MM-DD HH:MM:SS)
     */
    public void schedulePtSession(int memberId, int trainerId, int roomId, String sessionStart, String sessionEnd) {
        boolean memberConflict = ptSessionRepository.hasMemberSessionConflict(memberId, sessionStart, sessionEnd, null);
        boolean trainerConflict = ptSessionRepository.hasTrainerSessionConflict(trainerId, sessionStart, sessionEnd, null);
        boolean roomConflict = ptSessionRepository.hasRoomSessionConflict(roomId, sessionStart, sessionEnd, null);

        if (memberConflict) {
            System.out.println("Cannot book session: Member already has a session in this time range.");
            return;
        }

        if (trainerConflict) {
            System.out.println("Cannot book session: Trainer already has a session in this time range.");
            return;
        }

        if (roomConflict) {
            System.out.println("Cannot book session: Room is already booked in this time range.");
            return;
        }

        ptSessionRepository.addPtSession(memberId, trainerId, roomId, sessionStart, sessionEnd);
    }

    /**
     * Reschedules an existing personal training session.
     * 
     * @param sessionId    the session ID
     * @param memberId     the member's ID
     * @param trainerId    the trainer's ID
     * @param roomId       the room ID
     * @param sessionStart the start time (YYYY-MM-DD HH:MM:SS)
     * @param sessionEnd   the end time (YYYY-MM-DD HH:MM:SS)
     */
    public void reschedulePtSession(int sessionId, int memberId, int trainerId, int roomId, String sessionStart, String sessionEnd) {
        boolean memberConflict = ptSessionRepository.hasMemberSessionConflict(memberId, sessionStart, sessionEnd, sessionId);
        boolean trainerConflict = ptSessionRepository.hasTrainerSessionConflict(trainerId, sessionStart, sessionEnd, sessionId);
        boolean roomConflict = ptSessionRepository.hasRoomSessionConflict(roomId, sessionStart, sessionEnd, sessionId);

        if (memberConflict) {
            System.out.println("Cannot reschedule: Member already has a session in this time range.");
            return;
        }

        if (trainerConflict) {
            System.out.println("Cannot reschedule: Trainer already has a session in this time range.");
            return;
        }

        if (roomConflict) {
            System.out.println("Cannot reschedule: Room is already booked in this time range.");
            return;
        }

        ptSessionRepository.updatePtSessionTime(sessionId, sessionStart, sessionEnd);
    }

    /**
     * Cancels a personal training session.
     * 
     * @param sessionId the session ID
     */
    public void cancelPtSession(int sessionId) {
        ptSessionRepository.updatePtSessionStatus(sessionId, "Cancelled");
    }

    /**
     * Displays all PT sessions for a member.
     * 
     * @param memberId the member's ID
     */
    public void showPtSessions(int memberId) {
        List<String> sessions = ptSessionRepository.getPtSessionsForMember(memberId);
        if (sessions.isEmpty()) {
            System.out.println("No PT sessions found for member ID: " + memberId);
            return;
        }

        System.out.println("PT sessions for member ID: " + memberId);
        for (String s : sessions) {
            System.out.println(s);
        }
    }

    /**
     * Registers a member for a group class.
     * 
     * @param memberId the member's ID
     * @param classId  the class ID
     */
    public void registerForClass(int memberId, int classId) {
        if (classRepository.isMemberRegisteredForClass(memberId, classId)) {
            System.out.println("Member is already registered for this class.");
            return;
        }

        if (classRepository.isClassFull(classId)) {
            System.out.println("Cannot register: class is already full.");
            return;
        }

        classRepository.registerMemberForClass(memberId, classId);
    }

    /**
     * Displays all class registrations for a member.
     * 
     * @param memberId the member's ID
     */
    public void showClassRegistrations(int memberId) {
        List<String> registrations = classRepository.getRegistrationsForMember(memberId);
        if (registrations.isEmpty()) {
            System.out.println("No class registrations found for member ID: " + memberId);
            return;
        }

        System.out.println("Class registrations for member ID: " + memberId);
        for (String r : registrations) {
            System.out.println(r);
        }
    }
}
