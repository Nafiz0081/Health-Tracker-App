import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Goal {
    private Double targetWeight;
    private Integer targetSleepHours;
    private Double targetWaterIntake;
    private LocalDate creationDate;

    // Constructor
    public Goal(Double targetWeight, Integer targetSleepHours, Double targetWaterIntake) {
        this.targetWeight = targetWeight;
        this.targetSleepHours = targetSleepHours;
        this.targetWaterIntake = targetWaterIntake;
        this.creationDate = LocalDate.now();
    }

    // Getters and Setters
    public Double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(Double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public Integer getTargetSleepHours() {
        return targetSleepHours;
    }

    public void setTargetSleepHours(Integer targetSleepHours) {
        this.targetSleepHours = targetSleepHours;
    }

    public Double getTargetWaterIntake() {
        return targetWaterIntake;
    }

    public void setTargetWaterIntake(Double targetWaterIntake) {
        this.targetWaterIntake = targetWaterIntake;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    // Check if a health record meets the goals
    public boolean isGoalMet(HealthRecord record) {
        boolean weightGoalMet = targetWeight == null || record.getWeight() <= targetWeight;
        boolean sleepGoalMet = targetSleepHours == null || record.getSleepHours() >= targetSleepHours;
        boolean waterGoalMet = targetWaterIntake == null || record.getWaterIntake() >= targetWaterIntake;
        
        return weightGoalMet && sleepGoalMet && waterGoalMet;
    }

    // Convert goal to string for file storage
    public String toFileString() {
        return (targetWeight != null ? targetWeight : "null") + "," +
               (targetSleepHours != null ? targetSleepHours : "null") + "," +
               (targetWaterIntake != null ? targetWaterIntake : "null") + "," +
               creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    // Create Goal from file string
    public static Goal fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid goal format: " + line);
        }
        
        Double targetWeight = parts[0].equals("null") ? null : Double.parseDouble(parts[0]);
        Integer targetSleepHours = parts[1].equals("null") ? null : Integer.parseInt(parts[1]);
        Double targetWaterIntake = parts[2].equals("null") ? null : Double.parseDouble(parts[2]);
        
        Goal goal = new Goal(targetWeight, targetSleepHours, targetWaterIntake);
        goal.creationDate = LocalDate.parse(parts[3]);
        
        return goal;
    }

    @Override
    public String toString() {
        return "Created on: " + creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE) +
               "\nTarget Weight: " + (targetWeight != null ? targetWeight + " kg" : "Not set") +
               "\nTarget Sleep: " + (targetSleepHours != null ? targetSleepHours + " hours" : "Not set") +
               "\nTarget Water Intake: " + (targetWaterIntake != null ? targetWaterIntake + " liters" : "Not set");
    }
}