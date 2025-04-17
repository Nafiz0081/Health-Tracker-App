import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SummaryService {
    
    // Generate a weekly summary from a list of health records
    public String generateWeeklySummary(List<HealthRecord> records) {
        // Filter records from the last 7 days
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        
        List<HealthRecord> weeklyRecords = records.stream()
                .filter(record -> !record.getDate().isBefore(weekAgo) && !record.getDate().isAfter(today))
                .collect(Collectors.toList());
        
        if (weeklyRecords.isEmpty()) {
            return "No records found for the past week.";
        }
        
        return generateSummary(weeklyRecords, "Weekly", weekAgo, today);
    }
    
    // Generate a monthly summary from a list of health records
    public String generateMonthlySummary(List<HealthRecord> records) {
        // Filter records from the last 30 days
        LocalDate today = LocalDate.now();
        LocalDate monthAgo = today.minusDays(30);
        
        List<HealthRecord> monthlyRecords = records.stream()
                .filter(record -> !record.getDate().isBefore(monthAgo) && !record.getDate().isAfter(today))
                .collect(Collectors.toList());
        
        if (monthlyRecords.isEmpty()) {
            return "No records found for the past month.";
        }
        
        return generateSummary(monthlyRecords, "Monthly", monthAgo, today);
    }
    
    // Helper method to generate summary statistics
    private String generateSummary(List<HealthRecord> records, String period, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        
        // Calculate statistics
        DoubleSummaryStatistics weightStats = records.stream()
                .collect(Collectors.summarizingDouble(HealthRecord::getWeight));
        
        IntSummaryStatistics sleepStats = records.stream()
                .collect(Collectors.summarizingInt(HealthRecord::getSleepHours));
        
        DoubleSummaryStatistics waterStats = records.stream()
                .collect(Collectors.summarizingDouble(HealthRecord::getWaterIntake));
        
        // Count exercise frequency
        Map<String, Long> exerciseCounts = records.stream()
                .flatMap(record -> record.getExercises().stream())
                .collect(Collectors.groupingBy(exercise -> exercise, Collectors.counting()));
        
        // Format the summary
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("%s Summary (%s to %s)\n", 
                period, startDate.format(formatter), endDate.format(formatter)));
        summary.append("=================================\n");
        summary.append(String.format("Number of Records: %d\n\n", records.size()));
        
        summary.append("Weight (kg):\n");
        summary.append(String.format("  Average: %.2f\n", weightStats.getAverage()));
        summary.append(String.format("  Min: %.2f\n", weightStats.getMin()));
        summary.append(String.format("  Max: %.2f\n\n", weightStats.getMax()));
        
        summary.append("Sleep (hours):\n");
        summary.append(String.format("  Average: %.2f\n", sleepStats.getAverage()));
        summary.append(String.format("  Min: %d\n", sleepStats.getMin()));
        summary.append(String.format("  Max: %d\n\n", sleepStats.getMax()));
        
        summary.append("Water Intake (liters):\n");
        summary.append(String.format("  Average: %.2f\n", waterStats.getAverage()));
        summary.append(String.format("  Min: %.2f\n", waterStats.getMin()));
        summary.append(String.format("  Max: %.2f\n\n", waterStats.getMax()));
        
        summary.append("Exercise Frequency:\n");
        if (exerciseCounts.isEmpty()) {
            summary.append("  No exercises recorded\n");
        } else {
            exerciseCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> summary.append(String.format("  %s: %d times\n", entry.getKey(), entry.getValue())));
        }
        
        return summary.toString();
    }
}