import java.util.List;
import java.util.ArrayList;


public class HealthTrackerApp {
    private static UserProfile userProfile;
    private static ConsoleUI ui;
    private static SummaryService summaryService;
    private static ProgressTracker progressTracker;
    private static MoodTracker moodTracker;
    
    public static void main(String[] args) {
        try {
            // Initialize components
            ui = new ConsoleUI();
            userProfile = new UserProfile("User"); // Default name
            summaryService = new SummaryService();
            progressTracker = new ProgressTracker();
            moodTracker = new MoodTracker();
            
            boolean running = true;
            
            // Main application loop
            while (running) {
                ui.displayMainMenu();
                int choice = ui.getUserChoice();
                
                try {
                    switch (choice) {
                case 1: // Add new health record
                    addNewRecord();
                    break;
                case 2: // View all records
                    viewAllRecords();
                    break;
                case 3: // View weekly summary
                    viewWeeklySummary();
                    break;
                case 4: // View monthly summary
                    viewMonthlySummary();
                    break;
                case 5: // Set/update health goals
                    setHealthGoals();
                    break;
                case 6: // Track progress against goals
                    trackProgress();
                    break;
                case 7: // Delete a record
                    deleteRecord();
                    break;
                case 8: // Mood Tracking
                    handleMoodTracking();
                    break;

                case 10: // Medication Reminder
                    handleMedicationReminder();
                    break;
                case 11: // Health Metrics Visualization
                    handleVisualization();
                    break;
                case 12: // Social Sharing
                    handleSocialSharing();
                    break;
                case 13: // Exit
                    running = false;
                    ui.displaySuccess("Thank you for using Personal Health Tracker. Goodbye!");
                    break;
                default:
                    ui.displayError("Invalid choice. Please try again.");
                    }
                } catch (Exception e) {
                    ui.displayError("An error occurred: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
        } finally {
            // Close resources
            if (ui != null) {
                ui.close();
            }
        }
    }
    
    // Add a new health record
    private static void addNewRecord() {
        HealthRecord record = ui.getNewHealthRecord();
        userProfile.addRecord(record);
        ui.displaySuccess("Health record added successfully!");
    }
    
    // View all health records
    private static void viewAllRecords() {
        ui.displayRecords(userProfile.getRecords());
    }
    
    // View weekly summary
    private static void viewWeeklySummary() {
        String summary = summaryService.generateWeeklySummary(userProfile.getRecords());
        ui.displayMessage(summary);
    }
    
    // View monthly summary
    private static void viewMonthlySummary() {
        String summary = summaryService.generateMonthlySummary(userProfile.getRecords());
        ui.displayMessage(summary);
    }
    
    // Set or update health goals
    private static void setHealthGoals() {
        Goal currentGoal = userProfile.getGoal();
        if (currentGoal != null) {
            ui.displayInfo("Current goals:\n" + currentGoal.toString());
            ui.displayInfo("\nEnter new goals (or 0 to keep current):\n");
        }
        
        Goal newGoal = ui.getGoalInput();
        userProfile.setGoal(newGoal);
        ui.displaySuccess("Health goals updated successfully!");
    }
    
    // Track progress against goals
    private static void trackProgress() {
        Goal goal = userProfile.getGoal();
        if (goal == null) {
            ui.displayError("No health goals set. Please set goals first.");
            return;
        }
        
        List<HealthRecord> records = userProfile.getRecords();
        if (records.isEmpty()) {
            ui.displayError("No health records found. Please add records first.");
            return;
        }
        
        // Get the most recent record
        HealthRecord latestRecord = records.get(records.size() - 1);
        
        String progressReport = progressTracker.compareWithGoals(latestRecord, goal);
        ui.displayMessage(progressReport);
    }
    
    // Delete a health record
    private static void deleteRecord() {
        List<HealthRecord> records = userProfile.getRecords();
        if (records.isEmpty()) {
            ui.displayError("No health records found to delete.");
            return;
        }
        
        ui.displayRecords(records);
        int index = ui.getRecordIndexForDeletion(records.size());
        
        if (userProfile.deleteRecord(index)) {
            ui.displaySuccess("Record deleted successfully!");
        } else {
            ui.displayError("Failed to delete record.");
        }
    }
    
    // Handle mood tracking functionality
    private static void handleMoodTracking() {
        boolean inMoodMenu = true;
        
        while (inMoodMenu) {
            ui.displayMoodTrackingMenu();
            int choice = ui.getUserChoice();
            
            switch (choice) {
                case 1: // Add new mood record
                    MoodRecord moodRecord = ui.getNewMoodRecord();
                    userProfile.addMoodRecord(moodRecord);
                    ui.displaySuccess("Mood record added successfully!");
                    break;
                case 2: // View all mood records
                    ui.displayMoodRecords(userProfile.getMoodRecords());
                    break;
                case 3: // Generate mood analysis
                    List<MoodRecord> moodRecords = userProfile.getMoodRecords();
                    if (moodRecords.isEmpty()) {
                        ui.displayError("No mood records found for analysis.");
                    } else {
                        String analysis = moodTracker.generateMoodAnalysis(moodRecords);
                        ui.displayMessage(analysis);
                    }
                    break;
                case 4: // Correlate mood with health
                    List<MoodRecord> mRecords = userProfile.getMoodRecords();
                    List<HealthRecord> hRecords = userProfile.getRecords();
                    if (mRecords.isEmpty() || hRecords.isEmpty()) {
                        ui.displayError("Need both mood and health records for correlation.");
                    } else {
                        String correlation = moodTracker.correlateMoodWithHealth(mRecords, hRecords);
                        ui.displayMessage(correlation);
                    }
                    break;
                case 5: // Delete a mood record
                    List<MoodRecord> records = userProfile.getMoodRecords();
                    if (records.isEmpty()) {
                        ui.displayError("No mood records found to delete.");
                    } else {
                        ui.displayMoodRecords(records);
                        int index = ui.getMoodRecordIndexForDeletion(records.size());
                        if (userProfile.deleteMoodRecord(index)) {
                            ui.displaySuccess("Mood record deleted successfully!");
                        } else {
                            ui.displayError("Failed to delete mood record.");
                        }
                    }
                    break;
                case 6: // Return to main menu
                    inMoodMenu = false;
                    break;
                default:
                    ui.displayError("Invalid choice. Please try again.");
            }
        }
    }
    

    
    // Handle medication reminder functionality
    private static void handleMedicationReminder() {
        boolean inMedicationMenu = true;
        
        while (inMedicationMenu) {
            ui.displayMedicationReminderMenu();
            int choice = ui.getUserChoice();
            
            switch (choice) {
                case 1: // Add new medication reminder
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 2: // View all reminders
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 3: // Return to main menu
                    inMedicationMenu = false;
                    break;
                default:
                    ui.displayError("Invalid choice. Please try again.");
            }
        }
    }
    
    // Handle health metrics visualization functionality
    private static void handleVisualization() {
        boolean inVisualizationMenu = true;
        
        while (inVisualizationMenu) {
            ui.displayVisualizationMenu();
            int choice = ui.getUserChoice();
            
            switch (choice) {
                case 1: // View weight trends
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 2: // View blood pressure trends
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 3: // Return to main menu
                    inVisualizationMenu = false;
                    break;
                default:
                    ui.displayError("Invalid choice. Please try again.");
            }
        }
    }
    
    // Handle social sharing functionality
    private static void handleSocialSharing() {
        boolean inSocialMenu = true;
        
        while (inSocialMenu) {
            ui.displaySocialSharingMenu();
            int choice = ui.getUserChoice();
            
            switch (choice) {
                case 1: // Share health summary
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 2: // Share progress
                    ui.displayInfo("Feature coming soon!");
                    break;
                case 3: // Return to main menu
                    inSocialMenu = false;
                    break;
                default:
                    ui.displayError("Invalid choice. Please try again.");
            }
        }
    }
}