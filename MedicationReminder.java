import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicationReminder {
    private List<Medication> medications;
    
    public MedicationReminder() {
        this.medications = new ArrayList<>();
    }
    
    // Add a new medication
    public void addMedication(Medication medication) {
        medications.add(medication);
    }
    
    // Remove a medication
    public boolean removeMedication(int index) {
        if (index >= 0 && index < medications.size()) {
            medications.remove(index);
            return true;
        }
        return false;
    }
    
    // Get all medications
    public List<Medication> getMedications() {
        return medications;
    }
    
    // Get today's medication schedule
    public String getTodaySchedule() {
        LocalDate today = LocalDate.now();
        List<Medication> todayMeds = medications.stream()
                .filter(med -> med.shouldTakeToday(today))
                .sorted((m1, m2) -> m1.getTime().compareTo(m2.getTime()))
                .collect(Collectors.toList());
        
        if (todayMeds.isEmpty()) {
            return "No medications scheduled for today.";
        }
        
        StringBuilder schedule = new StringBuilder();
        schedule.append("Today's Medication Schedule:\n");
        schedule.append("============================\n\n");
        
        for (Medication med : todayMeds) {
            schedule.append(med.getTime().format(DateTimeFormatter.ofPattern("hh:mm a")))
                    .append(" - ")
                    .append(med.getName())
                    .append(" (")
                    .append(med.getDosage())
                    .append(")\n");
        }
        
        return schedule.toString();
    }
    
    // Mark medication as taken
    public void markAsTaken(String medicationName, LocalDate date) {
        for (Medication med : medications) {
            if (med.getName().equalsIgnoreCase(medicationName)) {
                med.addTakenDate(date);
                break;
            }
        }
    }
    
    // Calculate adherence rate (percentage of medications taken as prescribed)
    public String calculateAdherence(int days) {
        if (medications.isEmpty()) {
            return "No medications found for adherence calculation.";
        }
        
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);
        
        int totalScheduled = 0;
        int totalTaken = 0;
        
        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            for (Medication med : medications) {
                if (med.shouldTakeToday(date)) {
                    totalScheduled++;
                    if (med.wasTakenOn(date)) {
                        totalTaken++;
                    }
                }
            }
        }
        
        if (totalScheduled == 0) {
            return "No medications were scheduled in the past " + days + " days.";
        }
        
        double adherenceRate = (totalTaken * 100.0) / totalScheduled;
        
        StringBuilder report = new StringBuilder();
        report.append("Medication Adherence Report\n");
        report.append("===========================\n\n");
        report.append(String.format("Period: Last %d days\n", days));
        report.append(String.format("Scheduled doses: %d\n", totalScheduled));
        report.append(String.format("Taken doses: %d\n", totalTaken));
        report.append(String.format("Adherence rate: %.1f%%\n\n", adherenceRate));
        
        // Provide feedback based on adherence rate
        if (adherenceRate >= 90) {
            report.append("Excellent adherence! Keep up the good work.\n");
        } else if (adherenceRate >= 80) {
            report.append("Good adherence. Try to be more consistent with your medication schedule.\n");
        } else if (adherenceRate >= 50) {
            report.append("Moderate adherence. Missing doses can affect your treatment. " +
                          "Consider setting reminders.\n");
        } else {
            report.append("Poor adherence. Please consult with your healthcare provider about " +
                          "strategies to improve medication compliance.\n");
        }
        
        return report.toString();
    }
    
    // Inner class to represent a medication
    public static class Medication {
        private String name;
        private String dosage;
        private LocalTime time;
        private String frequency; // daily, weekdays, weekends, specific days
        private List<String> daysOfWeek; // for specific days (e.g., Monday, Wednesday, Friday)
        private List<LocalDate> takenDates;
        
        public Medication(String name, String dosage, LocalTime time, String frequency) {
            this.name = name;
            this.dosage = dosage;
            this.time = time;
            this.frequency = frequency;
            this.daysOfWeek = new ArrayList<>();
            this.takenDates = new ArrayList<>();
        }
        
        public String getName() {
            return name;
        }
        
        public String getDosage() {
            return dosage;
        }
        
        public LocalTime getTime() {
            return time;
        }
        
        public String getFrequency() {
            return frequency;
        }
        
        public void setDaysOfWeek(List<String> days) {
            this.daysOfWeek = days;
        }
        
        public List<String> getDaysOfWeek() {
            return daysOfWeek;
        }
        
        public void addTakenDate(LocalDate date) {
            if (!takenDates.contains(date)) {
                takenDates.add(date);
            }
        }
        
        public boolean wasTakenOn(LocalDate date) {
            return takenDates.contains(date);
        }
        
        public boolean shouldTakeToday(LocalDate date) {
            String dayOfWeek = date.getDayOfWeek().toString();
            
            switch (frequency.toLowerCase()) {
                case "daily":
                    return true;
                case "weekdays":
                    return !dayOfWeek.equals("SATURDAY") && !dayOfWeek.equals("SUNDAY");
                case "weekends":
                    return dayOfWeek.equals("SATURDAY") || dayOfWeek.equals("SUNDAY");
                case "specific":
                    return daysOfWeek.contains(dayOfWeek);
                default:
                    return false;
            }
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(" - ").append(dosage).append("\n");
            sb.append("Time: ").append(time.format(DateTimeFormatter.ofPattern("hh:mm a"))).append("\n");
            sb.append("Frequency: ");
            
            switch (frequency.toLowerCase()) {
                case "daily":
                    sb.append("Every day");
                    break;
                case "weekdays":
                    sb.append("Weekdays only");
                    break;
                case "weekends":
                    sb.append("Weekends only");
                    break;
                case "specific":
                    sb.append(String.join(", ", daysOfWeek));
                    break;
            }
            
            return sb.toString();
        }
        
        // Convert to string for file storage
        public String toFileString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name.replace(",", "[comma]")).append(",");
            sb.append(dosage.replace(",", "[comma]")).append(",");
            sb.append(time.format(DateTimeFormatter.ISO_LOCAL_TIME)).append(",");
            sb.append(frequency).append(",");
            
            // Add days of week if applicable
            if (!daysOfWeek.isEmpty()) {
                sb.append(String.join("|", daysOfWeek));
            }
            sb.append(",");
            
            // Add taken dates
            if (!takenDates.isEmpty()) {
                List<String> dateStrings = takenDates.stream()
                        .map(date -> date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .collect(Collectors.toList());
                sb.append(String.join("|", dateStrings));
            }
            
            return sb.toString();
        }
        
        // Create from file string
        public static Medication fromFileString(String line) {
            String[] parts = line.split(",");
            if (parts.length < 5) {
                throw new IllegalArgumentException("Invalid medication format: " + line);
            }
            
            String name = parts[0].replace("[comma]", ",");
            String dosage = parts[1].replace("[comma]", ",");
            LocalTime time = LocalTime.parse(parts[2]);
            String frequency = parts[3];
            
            Medication medication = new Medication(name, dosage, time, frequency);
            
            // Parse days of week if present
            if (parts[4].length() > 0) {
                String[] days = parts[4].split("\\|");
                medication.setDaysOfWeek(List.of(days));
            }
            
            // Parse taken dates if present
            if (parts.length > 5 && parts[5].length() > 0) {
                String[] dateStrings = parts[5].split("\\|");
                for (String dateStr : dateStrings) {
                    medication.addTakenDate(LocalDate.parse(dateStr));
                }
            }
            
            return medication;
        }
    }
}