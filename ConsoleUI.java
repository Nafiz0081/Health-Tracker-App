import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_WHITE = "\u001B[37m";
    
    private Scanner scanner;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    // Display the main menu
    public void displayMainMenu() {
        System.out.println(ANSI_BLUE + "\n===============================");
        System.out.println("    PERSONAL HEALTH TRACKER");
        System.out.println("===============================");
        System.out.println(ANSI_WHITE);
        System.out.println("[1] Add New Health Record");
        System.out.println("[2] View All Records");
        System.out.println("[3] View Weekly Summary");
        System.out.println("[4] View Monthly Summary");
        System.out.println("[5] Set/Update Health Goals");
        System.out.println("[6] Track Progress Against Goals");
        System.out.println("[7] Delete a Record");
        System.out.println("[8] Mood Tracking");

        System.out.println("[10] Medication Reminder");
        System.out.println("[11] Health Metrics Visualization");
        System.out.println("[12] Social Sharing");
        System.out.println("[13] Exit");
        System.out.println(ANSI_RESET);
    }
    
    // Get user menu choice
    public int getUserChoice() {
        System.out.print(ANSI_CYAN + "Please enter your choice: " + ANSI_RESET);
        while (!scanner.hasNextInt()) {
            System.out.print(ANSI_RED + "Invalid input. Please enter a number: " + ANSI_RESET);
            scanner.next(); // Consume invalid input
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return choice;
    }
    
    // Display success message
    public void displaySuccess(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }
    
    // Display error message
    public void displayError(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }
    
    // Display information message
    public void displayInfo(String message) {
        System.out.println(ANSI_CYAN + message + ANSI_RESET);
    }
    
    // Display regular message
    public void displayMessage(String message) {
        System.out.println(message);
    }
    
    // Get user input for a new health record
    public HealthRecord getNewHealthRecord() {
        LocalDate date = LocalDate.now();
        displayInfo("Enter health data for " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + ":");
        
        // Get weight
        double weight = getDoubleInput("Enter your weight (kg): ");
        
        // Get sleep hours
        int sleepHours = getIntInput("Enter your sleep hours: ");
        
        // Get water intake
        double waterIntake = getDoubleInput("Enter your water intake (liters): ");
        
        // Get exercises
        displayInfo("Enter exercises (comma-separated, leave empty if none): ");
        String exercisesInput = scanner.nextLine().trim();
        List<String> exercises = new ArrayList<>();
        if (!exercisesInput.isEmpty()) {
            exercises = Arrays.asList(exercisesInput.split(","));
            // Trim each exercise
            for (int i = 0; i < exercises.size(); i++) {
                exercises.set(i, exercises.get(i).trim());
            }
        }
        
        return new HealthRecord(date, weight, sleepHours, waterIntake, exercises);
    }
    
    // Get user input for setting goals
    public Goal getGoalInput() {
        displayInfo("Setting health goals (enter 0 to skip a goal):");
        
        // Get target weight
        Double targetWeight = null;
        double weightInput = getDoubleInput("Enter target weight (kg): ");
        if (weightInput > 0) {
            targetWeight = weightInput;
        }
        
        // Get target sleep hours
        Integer targetSleepHours = null;
        int sleepInput = getIntInput("Enter target sleep hours: ");
        if (sleepInput > 0) {
            targetSleepHours = sleepInput;
        }
        
        // Get target water intake
        Double targetWaterIntake = null;
        double waterInput = getDoubleInput("Enter target water intake (liters): ");
        if (waterInput > 0) {
            targetWaterIntake = waterInput;
        }
        
        return new Goal(targetWeight, targetSleepHours, targetWaterIntake);
    }
    
    // Display all health records
    public void displayRecords(List<HealthRecord> records) {
        if (records.isEmpty()) {
            displayInfo("No health records found.");
            return;
        }
        
        displayInfo("\nAll Health Records:\n");
        for (int i = 0; i < records.size(); i++) {
            System.out.println("Record #" + (i + 1));
            System.out.println(records.get(i).toString());
            System.out.println("-----------------------------");
        }
    }
    
    // Get record index for deletion
    public int getRecordIndexForDeletion(int maxIndex) {
        displayInfo("Enter the record number to delete (1-" + maxIndex + "): ");
        int index = getIntInput("") - 1; // Convert to 0-based index
        
        while (index < 0 || index >= maxIndex) {
            displayError("Invalid record number. Please try again.");
            index = getIntInput("Enter the record number (1-" + maxIndex + "): ") - 1;
        }
        
        return index;
    }
    
    // Helper method to get double input
    private double getDoubleInput(String prompt) {
        double value = 0;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                System.out.print(ANSI_CYAN + prompt + ANSI_RESET);
                value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    displayError("Value cannot be negative. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                displayError("Invalid input. Please enter a number.");
            }
        }
        
        return value;
    }
    
    // Helper method to get integer input
    private int getIntInput(String prompt) {
        int value = 0;
        boolean validInput = false;
        
        while (!validInput) {
            try {
                System.out.print(ANSI_CYAN + prompt + ANSI_RESET);
                value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    displayError("Value cannot be negative. Please try again.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                displayError("Invalid input. Please enter a whole number.");
            }
        }
        
        return value;
    }
    
    // Get user input for a new mood record
    public MoodRecord getNewMoodRecord() {
        LocalDate date = LocalDate.now();
        displayInfo("Enter mood data for " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + ":");
        
        // Get mood rating
        System.out.println(ANSI_CYAN + "Select your mood:" + ANSI_RESET);
        System.out.println("1. Happy");
        System.out.println("2. Sad");
        System.out.println("3. Stressed");
        System.out.println("4. Relaxed");
        System.out.println("5. Energetic");
        System.out.println("6. Tired");
        System.out.println("7. Anxious");
        System.out.println("8. Content");
        System.out.println("9. Other (specify)");
        
        int moodChoice = getIntInput("Enter your choice (1-9): ");
        String moodRating;
        
        switch (moodChoice) {
            case 1: moodRating = "Happy"; break;
            case 2: moodRating = "Sad"; break;
            case 3: moodRating = "Stressed"; break;
            case 4: moodRating = "Relaxed"; break;
            case 5: moodRating = "Energetic"; break;
            case 6: moodRating = "Tired"; break;
            case 7: moodRating = "Anxious"; break;
            case 8: moodRating = "Content"; break;
            case 9:
                System.out.print(ANSI_CYAN + "Specify your mood: " + ANSI_RESET);
                moodRating = scanner.nextLine().trim();
                break;
            default: moodRating = "Neutral";
        }
        
        // Get energy level
        int energyLevel = getIntInput("Enter your energy level (1-10): ");
        while (energyLevel < 1 || energyLevel > 10) {
            displayError("Energy level must be between 1 and 10.");
            energyLevel = getIntInput("Enter your energy level (1-10): ");
        }
        
        // Get notes
        System.out.print(ANSI_CYAN + "Enter any notes about your mood/feelings: " + ANSI_RESET);
        String notes = scanner.nextLine().trim();
        
        return new MoodRecord(date, moodRating, energyLevel, notes);
    }
    
    // Display all mood records
    public void displayMoodRecords(List<MoodRecord> records) {
        if (records.isEmpty()) {
            displayInfo("No mood records found.");
            return;
        }
        
        displayInfo("\nAll Mood Records:\n");
        for (int i = 0; i < records.size(); i++) {
            System.out.println("Record #" + (i + 1));
            System.out.println(records.get(i).toString());
            System.out.println("-----------------------------");
        }
    }
    
    // Display mood tracking submenu
    public void displayMoodTrackingMenu() {
        System.out.println(ANSI_BLUE + "\n===============================");
        System.out.println("         MOOD TRACKING");
        System.out.println("===============================");
        System.out.println(ANSI_WHITE);
        System.out.println("[1] Add New Mood Record");
        System.out.println("[2] View All Mood Records");
        System.out.println("[3] Generate Mood Analysis");
        System.out.println("[4] Correlate Mood with Health");
        System.out.println("[5] Delete a Mood Record");
        System.out.println("[6] Return to Main Menu");
        System.out.println(ANSI_RESET);
    }
    
    // Get mood record index for deletion
    public int getMoodRecordIndexForDeletion(int maxIndex) {
        displayInfo("Enter the mood record number to delete (1-" + maxIndex + "): ");
        int index = getIntInput("") - 1; // Convert to 0-based index
        
        while (index < 0 || index >= maxIndex) {
            displayError("Invalid record number. Please try again.");
            index = getIntInput("Enter the record number (1-" + maxIndex + "): ") - 1;
        }
        
        return index;
    }
    
    // Display nutrition tracking submenu

    
    // Display medication reminder submenu
    public void displayMedicationReminderMenu() {
        System.out.println(ANSI_BLUE + "\n===============================");
        System.out.println("     MEDICATION REMINDER");
        System.out.println("===============================");
        System.out.println(ANSI_WHITE);
        System.out.println("[1] Add New Medication");
        System.out.println("[2] View Medications");
        System.out.println("[3] Track Medication Adherence");
        System.out.println("[4] Set Medication Reminder");
        System.out.println("[5] Return to Main Menu");
        System.out.println(ANSI_RESET);
    }
    
    // Display health metrics visualization submenu
    public void displayVisualizationMenu() {
        System.out.println(ANSI_BLUE + "\n===============================");
        System.out.println("  HEALTH METRICS VISUALIZATION");
        System.out.println("===============================");
        System.out.println(ANSI_WHITE);
        System.out.println("[1] Weight Trend Graph");
        System.out.println("[2] Sleep Pattern Analysis");
        System.out.println("[3] Water Intake Visualization");
        System.out.println("[4] Mood and Energy Correlation");
        System.out.println("[5] Return to Main Menu");
        System.out.println(ANSI_RESET);
    }
    
    // Display social sharing submenu
    public void displaySocialSharingMenu() {
        System.out.println(ANSI_BLUE + "\n===============================");
        System.out.println("        SOCIAL SHARING");
        System.out.println("===============================");
        System.out.println(ANSI_WHITE);
        System.out.println("[1] Export Health Data");
        System.out.println("[2] Share Progress Report");
        System.out.println("[3] Connect with Friends");
        System.out.println("[4] View Community Challenges");
        System.out.println("[5] Return to Main Menu");
        System.out.println(ANSI_RESET);
    }
    
    // Close the scanner
    public void close() {
        scanner.close();
    }
}