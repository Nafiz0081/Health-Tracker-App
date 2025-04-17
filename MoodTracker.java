import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoodTracker {
    
    // Generate a mood analysis report based on mood records
    public String generateMoodAnalysis(List<MoodRecord> records) {
        try {
            // Validate input
            if (records == null) {
                return "Error: Mood records list is null.";
            }
            if (records.isEmpty()) {
                return "No mood records available for analysis.";
            }
            
            // Validate record integrity
            if (records.stream().anyMatch(r -> r == null || r.getDate() == null || r.getMoodRating() == null ||
                    r.getEnergyLevel() < 1 || r.getEnergyLevel() > 10)) {
                return "Error: Invalid mood records found. Please check for null values and valid energy levels (1-10).";
            }
            
            // Sort records by date
            List<MoodRecord> sortedRecords = records.stream()
                    .sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                    .collect(Collectors.toList());
            
            // Calculate average energy level
            double avgEnergyLevel = sortedRecords.stream()
                    .mapToInt(MoodRecord::getEnergyLevel)
                    .average()
                    .orElse(0.0);
            
            // Count frequency of each mood
            Map<String, Long> moodFrequency = sortedRecords.stream()
                    .collect(Collectors.groupingBy(MoodRecord::getMoodRating, Collectors.counting()));
            
            // Find most common mood
            String mostCommonMood = moodFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("None");
            
            // Calculate mood stability (how often mood changes)
            int moodChanges = 0;
            String prevMood = null;
            for (MoodRecord record : sortedRecords) {
                if (prevMood != null && !prevMood.equals(record.getMoodRating())) {
                    moodChanges++;
                }
                prevMood = record.getMoodRating();
            }
            
            // Calculate days covered
            long daysCovered = 0;
            if (sortedRecords.size() > 1) {
                LocalDate firstDate = sortedRecords.get(0).getDate();
                LocalDate lastDate = sortedRecords.get(sortedRecords.size() - 1).getDate();
                daysCovered = ChronoUnit.DAYS.between(firstDate, lastDate) + 1;
            } else {
                daysCovered = 1;
            }
            
            // Build the analysis report
            StringBuilder report = new StringBuilder();
            report.append("Mood Analysis Report\n");
            report.append("===================\n\n");
            report.append(String.format("Period: %d days\n", daysCovered));
            report.append(String.format("Number of records: %d\n\n", sortedRecords.size()));
            
            report.append("Mood Distribution:\n");
            moodFrequency.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> {
                        double percentage = (entry.getValue() * 100.0) / sortedRecords.size();
                        report.append(String.format("  %s: %d times (%.1f%%)\n", 
                                entry.getKey(), entry.getValue(), percentage));
                    });
            
            report.append("\nMost common mood: " + mostCommonMood + "\n");
            report.append(String.format("Average energy level: %.1f/10\n", avgEnergyLevel));
            
            // Calculate mood stability percentage
            double stabilityPercentage = 100.0;
            if (sortedRecords.size() > 1) {
                stabilityPercentage = 100.0 - ((moodChanges * 100.0) / (sortedRecords.size() - 1));
            }
            report.append(String.format("Mood stability: %.1f%%\n", stabilityPercentage));
            
            return report.toString();
        } catch (IllegalArgumentException e) {
            return "Invalid data format in mood records: " + e.getMessage();
        } catch (Exception e) {
            return "Error during mood analysis: " + e.getMessage() + 
                   "\nPlease ensure all records are properly formatted and try again.";
        }
    }
    
    // Correlate mood with health metrics
    public String correlateMoodWithHealth(List<MoodRecord> moodRecords, List<HealthRecord> healthRecords) {
        try {
            // Validate input parameters
            if (moodRecords == null || healthRecords == null) {
                return "Error: Mood records or health records are null.";
            }
            if (moodRecords.isEmpty() || healthRecords.isEmpty()) {
                return "Insufficient data: No mood or health records available for analysis.";
            }
            
            // Validate data integrity
            if (moodRecords.stream().anyMatch(r -> r == null || r.getDate() == null) ||
                healthRecords.stream().anyMatch(r -> r == null || r.getDate() == null)) {
                return "Error: Invalid records found with null values.";
            }
        
        // Create a map of health records by date for easy lookup
        Map<LocalDate, HealthRecord> healthByDate = healthRecords.stream()
                .collect(Collectors.toMap(
                    HealthRecord::getDate,
                    record -> record,
                    (existing, replacement) -> existing // Keep first record in case of duplicates
                ));
        
        // Find matching records (same date)
        List<MoodRecord> matchedMoodRecords = moodRecords.stream()
                .filter(mood -> healthByDate.containsKey(mood.getDate()))
                .collect(Collectors.toList());
        
        if (matchedMoodRecords.isEmpty()) {
            return "No matching dates found between mood and health records.\n" +
                   "Tip: Ensure you have mood and health records for the same dates.";
        }
        
        // Validate energy levels
        if (matchedMoodRecords.stream().anyMatch(r -> r.getEnergyLevel() < 1 || r.getEnergyLevel() > 10)) {
            return "Error: Invalid energy levels found. Energy levels must be between 1 and 10.";
        }
        
        // Calculate correlations
        double totalSleepHighEnergy = 0;
        int countHighEnergy = 0;
        double totalSleepLowEnergy = 0;
        int countLowEnergy = 0;
        
        for (MoodRecord moodRecord : matchedMoodRecords) {
            HealthRecord healthRecord = healthByDate.get(moodRecord.getDate());
            
            // Consider energy level >= 7 as high energy
            if (moodRecord.getEnergyLevel() >= 7) {
                totalSleepHighEnergy += healthRecord.getSleepHours();
                countHighEnergy++;
            } else {
                totalSleepLowEnergy += healthRecord.getSleepHours();
                countLowEnergy++;
            }
        }
        
        // Calculate averages
        double avgSleepHighEnergy = countHighEnergy > 0 ? totalSleepHighEnergy / countHighEnergy : 0;
        double avgSleepLowEnergy = countLowEnergy > 0 ? totalSleepLowEnergy / countLowEnergy : 0;
        
        // Build correlation report
        StringBuilder report = new StringBuilder();
        report.append("Mood-Health Correlation Report\n");
        report.append("=============================\n\n");
        report.append(String.format("Matching records found: %d\n\n", matchedMoodRecords.size()));
        
        report.append("Sleep and Energy Level Correlation:\n");
        if (countHighEnergy > 0) {
            report.append(String.format("  Average sleep when energy level is high (7-10): %.1f hours\n", avgSleepHighEnergy));
        }
        if (countLowEnergy > 0) {
            report.append(String.format("  Average sleep when energy level is low (1-6): %.1f hours\n", avgSleepLowEnergy));
        }
        
        // Add sleep difference insight
        if (countHighEnergy > 0 && countLowEnergy > 0) {
            double sleepDiff = avgSleepHighEnergy - avgSleepLowEnergy;
            if (Math.abs(sleepDiff) >= 0.5) {
                report.append(String.format("\nInsight: On days with high energy, you sleep %.1f hours %s than on low energy days.\n",
                        Math.abs(sleepDiff), sleepDiff > 0 ? "more" : "less"));
            } else {
                report.append("\nInsight: Your sleep duration doesn't seem to significantly affect your energy levels.\n");
            }
        }
        
            return report.toString();
        } catch (IllegalArgumentException e) {
            return "Invalid data format: " + e.getMessage();
        } catch (Exception e) {
            return "Error during correlation analysis: " + e.getMessage() + 
                   "\nPlease ensure all records are properly formatted and try again.";
        }
    }
}