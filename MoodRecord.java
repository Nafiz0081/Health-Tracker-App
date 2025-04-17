import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MoodRecord {
    private LocalDate date;
    private String moodRating; // e.g., "Happy", "Sad", "Stressed", etc.
    private int energyLevel; // Scale of 1-10
    private String notes; // Additional notes about mood/feelings
    
    // Constructor
    public MoodRecord(LocalDate date, String moodRating, int energyLevel, String notes) {
        this.date = date;
        this.moodRating = moodRating;
        this.energyLevel = energyLevel;
        this.notes = notes;
    }
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getMoodRating() {
        return moodRating;
    }
    
    public void setMoodRating(String moodRating) {
        this.moodRating = moodRating;
    }
    
    public int getEnergyLevel() {
        return energyLevel;
    }
    
    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Convert record to string for file storage
    public String toFileString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + 
               moodRating + "," + 
               energyLevel + "," + 
               notes.replace(",", "[comma]"); // Escape commas in notes
    }
    
    // Create MoodRecord from file string
    public static MoodRecord fromFileString(String line) {
        String[] parts = line.split(",", 4); // Limit to 4 parts to handle commas in notes
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid mood record format: " + line);
        }
        
        LocalDate date = LocalDate.parse(parts[0]);
        String moodRating = parts[1];
        int energyLevel = Integer.parseInt(parts[2]);
        String notes = parts[3].replace("[comma]", ","); // Unescape commas
        
        return new MoodRecord(date, moodRating, energyLevel, notes);
    }
    
    @Override
    public String toString() {
        return "Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + 
               "\nMood: " + moodRating + 
               "\nEnergy Level: " + energyLevel + "/10" + 
               "\nNotes: " + notes;
    }
}