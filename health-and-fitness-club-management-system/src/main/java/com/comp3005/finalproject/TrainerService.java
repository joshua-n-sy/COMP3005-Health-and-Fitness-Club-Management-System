package com.comp3005.finalproject;

import java.util.List;

/**
 * Handles trainer-facing operations using the trainer-related repositories.
 */
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final PtSessionRepository ptSessionRepository;
    private final ClassRepository classRepository;
    private final MemberRepository memberRepository;

    public TrainerService() {
        this.trainerRepository = new TrainerRepository();
        this.ptSessionRepository = new PtSessionRepository();
        this.classRepository = new ClassRepository();
        this.memberRepository = new MemberRepository();
    }

    public TrainerService(TrainerRepository trainerRepository, PtSessionRepository ptSessionRepository, ClassRepository classRepository, MemberRepository memberRepository) {
        this.trainerRepository = trainerRepository;
        this.ptSessionRepository = ptSessionRepository;
        this.classRepository = classRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Adds an availability time slot for a trainer.
     * 
     * @param trainerId   the trainer's ID
     * @param startTime   the start time (YYYY-MM-DD HH:MM:SS)
     * @param endTime     the end time (YYYY-MM-DD HH:MM:SS)
     * @param isRecurring true if this availability repeats, or false otherwise
     */
    public void addAvailability(int trainerId, String startTime, String endTime, boolean isRecurring) {
        List<String> overlapping =
            trainerRepository.getTrainerAvailabilityInRange(trainerId, startTime, endTime);

        if (!overlapping.isEmpty()) {
            System.out.println("Cannot add availability: Overlapping availability already exists.");
            for (String slot : overlapping) {
                System.out.println("  " + slot);
            }
            return;
        }

        trainerRepository.addTrainerAvailability(trainerId, startTime, endTime, isRecurring);
    }

    /**
     * Removes an availability time slot for a trainer.
     * 
     * @param availabilityId
     */
    public void removeAvailability(int availabilityId) {
        trainerRepository.deleteTrainerAvailability(availabilityId);
    }

    /**
     * Displays all availability windows for a trainer.
     * 
     * @param trainerId the trainer's ID
     */
    public void showAvailability(int trainerId) {
        List<String> slots = trainerRepository.getTrainerAvailability(trainerId);
        if (slots.isEmpty()) {
            System.out.println("No availability found for trainer ID: " + trainerId);
            return;
        }

        System.out.println("Availability for trainer ID: " + trainerId);
        for (String slot : slots) {
            System.out.println(slot);
        }
    }

    /**
     * Displays the upcoming schedule for a trainer.
     * 
     * @param trainerId the trainer's ID
     */
    public void showSchedule(int trainerId) {
        List<String> ptSessions = ptSessionRepository.getPtSessionsForTrainer(trainerId);
        List<String> classes    = classRepository.getClassesForTrainer(trainerId);

        System.out.println("Schedule for trainer ID: " + trainerId);

        System.out.println("Personal Training Sessions:");
        if (ptSessions.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (String s : ptSessions) {
                System.out.println("  " + s);
            }
        }

        System.out.println("Group Classes:");
        if (classes.isEmpty()) {
            System.out.println("  (none)");
        } else {
            for (String c : classes) {
                System.out.println("  " + c);
            }
        }
    }

    /**
     * Looks up a member by their full name and displays their profile,
     * fitness goals, and latest health metric. This operation is read-only.
     * 
     * @param name the member's full name
     */
    public void lookupMemberByName(String name) {
        List<String> allMembers = memberRepository.getAllMembers();
        if (allMembers.isEmpty()) {
            System.out.println("No members found in the system.");
            return;
        }

        // Normalize the input full name
        String normalizedInput = name.trim().replaceAll("\\s+", " ").toLowerCase();
        boolean anyMatch = false;

        for (String memberRow : allMembers) {
            // Format: "member_id - first last - email - dob - gender"
            String[] parts = memberRow.split(" - ");
            if (parts.length < 2) {
                continue;
            }

            String memberName = parts[1];  // "first last"
            String normalizedMemberName = memberName.trim().replaceAll("\\s+", " ").toLowerCase();

            if (normalizedMemberName.equals(normalizedInput)) {
                anyMatch = true;

                int memberId = -1;
                try {
                    memberId = Integer.parseInt(parts[0].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Warning: could not parse member ID from row: " + memberRow);
                    continue;
                }

                System.out.println("--------------------------------------------------");
                System.out.println("Member: " + memberRow);

                // Fitness goals (read-only)
                List<String> goals = memberRepository.getFitnessGoalsForMember(memberId);
                if (goals.isEmpty()) {
                    System.out.println("  Fitness goals: none");
                } else {
                    System.out.println("  Fitness goals:");
                    for (String g : goals) {
                        System.out.println("    " + g);
                    }
                }

                // Latest health metric (read-only)
                List<String> latestMetric = memberRepository.getLatestHealthMetric(memberId);
                if (latestMetric.isEmpty()) {
                    System.out.println("  Latest health metric: none");
                } else {
                    System.out.println("  Latest health metric:");
                    for (String m : latestMetric) {
                        System.out.println("    " + m);
                    }
                }
            }
        }

        if (!anyMatch) {
            System.out.println("No members found with full name: " + name);
        }
    }
}
