import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileStorageService {
    private static final String DATA_DIR = "data";
    private static final String RECORDS_FILE = DATA_DIR + "/records.txt";
    private static final String GOAL_FILE = DATA_DIR + "/goals.txt";
    private static final String MOOD_RECORDS_FILE = DATA_DIR + "/mood_records.txt";

    public FileStorageService() {
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
    
    // Save health records to file
    public void saveRecords(List<HealthRecord> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE))) {
            for (HealthRecord record : records) {
                writer.write(record.toFileString());
                writer.newLine();
            }
        }
    }
    
    // Load health records from file
    public List<HealthRecord> loadRecords() throws IOException {
        List<HealthRecord> records = new ArrayList<>();
        File file = new File(RECORDS_FILE);
        
        if (!file.exists()) {
            return records; // Return empty list if file doesn't exist yet
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(HealthRecord.fromFileString(line));
                }
            }
        }
        
        return records;
    }
    
    // Save goal to file
    public void saveGoal(Goal goal) throws IOException {
        if (goal == null) {
            return; // Don't save if goal is null
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GOAL_FILE))) {
            writer.write(goal.toFileString());
        }
    }
    
    // Load goal from file
    public Goal loadGoal() throws IOException {
        File file = new File(GOAL_FILE);
        
        if (!file.exists()) {
            return null; // Return null if file doesn't exist yet
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                return Goal.fromFileString(line);
            }
        }
        
        return null;
    }
    
    // Save mood records to file
    public void saveMoodRecords(List<MoodRecord> moodRecords) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MOOD_RECORDS_FILE))) {
            for (MoodRecord record : moodRecords) {
                writer.write(record.toFileString());
                writer.newLine();
            }
        }
    }
    
    // Load mood records from file
    public List<MoodRecord> loadMoodRecords() throws IOException {
        List<MoodRecord> moodRecords = new ArrayList<>();
        File file = new File(MOOD_RECORDS_FILE);
        
        if (!file.exists()) {
            return moodRecords; // Return empty list if file doesn't exist yet
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    moodRecords.add(MoodRecord.fromFileString(line));
                }
            }
        }
        
        return moodRecords;
    }
}