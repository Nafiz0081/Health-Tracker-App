import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private String name;
    private Goal goal;
    private List<HealthRecord> records;
    private List<MoodRecord> moodRecords;

    private FileStorageService storageService;

    // Constructor
    public UserProfile(String name) {
        this.name = name;
        this.records = new ArrayList<>();
        this.moodRecords = new ArrayList<>();

        this.storageService = new FileStorageService();
        loadData();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
        saveGoal();
    }

    public List<HealthRecord> getRecords() {
        return records;
    }

    // Add a new health record
    public void addRecord(HealthRecord record) {
        records.add(record);
        saveRecords();
    }

    // Delete a health record by index
    public boolean deleteRecord(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
            saveRecords();
            return true;
        }
        return false;
    }

    // Get mood records
    public List<MoodRecord> getMoodRecords() {
        return moodRecords;
    }
    
    // Add a new mood record
    public void addMoodRecord(MoodRecord record) {
        moodRecords.add(record);
        saveMoodRecords();
    }
    
    // Delete a mood record by index
    public boolean deleteMoodRecord(int index) {
        if (index >= 0 && index < moodRecords.size()) {
            moodRecords.remove(index);
            saveMoodRecords();
            return true;
        }
        return false;
    }
    
    // Load records and goal from files
    private void loadData() {
        try {
            this.records = storageService.loadRecords();
            this.goal = storageService.loadGoal();
            this.moodRecords = storageService.loadMoodRecords();
        } catch (IOException e) {
            // If files don't exist yet, that's okay
            System.out.println("No existing data found. Starting fresh.");
        }
    }

    // Save records to file
    private void saveRecords() {
        try {
            storageService.saveRecords(records);
        } catch (IOException e) {
            System.err.println("Error saving records: " + e.getMessage());
        }
    }

    // Save goal to file
    private void saveGoal() {
        try {
            storageService.saveGoal(goal);
        } catch (IOException e) {
            System.err.println("Error saving goal: " + e.getMessage());
        }
    }
    
    // Save mood records to file
    private void saveMoodRecords() {
        try {
            storageService.saveMoodRecords(moodRecords);
        } catch (IOException e) {
            System.err.println("Error saving mood records: " + e.getMessage());
        }
    }
    

}