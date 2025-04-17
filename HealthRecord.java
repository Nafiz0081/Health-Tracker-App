import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HealthRecord {
    private LocalDate date;
    private double weight;
    private int sleepHours;
    private double waterIntake;
    private List<String> exercises;

    // Constructor
    public HealthRecord(LocalDate date, double weight, int sleepHours, double waterIntake, List<String> exercises) {
        this.date = date;
        this.weight = weight;
        this.sleepHours = sleepHours;
        this.waterIntake = waterIntake;
        this.exercises = exercises;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }

    public double getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(double waterIntake) {
        this.waterIntake = waterIntake;
    }

    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }

    // Convert record to string for file storage
    public String toFileString() {
        // Join exercises with pipe character
        String exercisesStr = String.join("|", exercises);
        // Format: date,weight,sleepHours,waterIntake,exercises
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + 
               weight + "," + 
               sleepHours + "," + 
               waterIntake + "," + 
               exercisesStr;
    }

    // Create HealthRecord from file string
    public static HealthRecord fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid record format: " + line);
        }
        
        LocalDate date = LocalDate.parse(parts[0]);
        double weight = Double.parseDouble(parts[1]);
        int sleepHours = Integer.parseInt(parts[2]);
        double waterIntake = Double.parseDouble(parts[3]);
        
        // Handle exercises (may be empty or missing)
        List<String> exercises = new ArrayList<>();
        if (parts.length > 4 && parts[4].trim().length() > 0) {
            exercises = Arrays.asList(parts[4].split("\\|"));
        }
        
        return new HealthRecord(date, weight, sleepHours, waterIntake, exercises);
    }

    @Override
    public String toString() {
        return "Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + 
               "\nWeight: " + weight + " kg" + 
               "\nSleep: " + sleepHours + " hours" + 
               "\nWater Intake: " + waterIntake + " liters" + 
               "\nExercises: " + String.join(", ", exercises);
    }
}