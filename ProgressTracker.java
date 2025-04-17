public class ProgressTracker {
    
    // Compare the most recent health record with the user's goal
    public String compareWithGoals(HealthRecord latest, Goal goal) {
        if (latest == null || goal == null) {
            return "No data available for comparison.";
        }
        
        StringBuilder progress = new StringBuilder();
        progress.append("Progress Report (" + latest.getDate().toString() + ")\n");
        progress.append("=================================\n\n");
        
        // Compare weight
        if (goal.getTargetWeight() != null) {
            double weightDiff = latest.getWeight() - goal.getTargetWeight();
            progress.append("Weight:\n");
            progress.append(String.format("  Current: %.2f kg\n", latest.getWeight()));
            progress.append(String.format("  Target: %.2f kg\n", goal.getTargetWeight()));
            
            if (weightDiff > 0) {
                progress.append(String.format("  Status: %.2f kg above target\n\n", Math.abs(weightDiff)));
            } else if (weightDiff < 0) {
                progress.append(String.format("  Status: %.2f kg below target\n\n", Math.abs(weightDiff)));
            } else {
                progress.append("  Status: Target achieved!\n\n");
            }
        } else {
            progress.append("Weight: No target set\n\n");
        }
        
        // Compare sleep hours
        if (goal.getTargetSleepHours() != null) {
            int sleepDiff = latest.getSleepHours() - goal.getTargetSleepHours();
            progress.append("Sleep:\n");
            progress.append(String.format("  Current: %d hours\n", latest.getSleepHours()));
            progress.append(String.format("  Target: %d hours\n", goal.getTargetSleepHours()));
            
            if (sleepDiff < 0) {
                progress.append(String.format("  Status: %d hours below target\n\n", Math.abs(sleepDiff)));
            } else if (sleepDiff > 0) {
                progress.append(String.format("  Status: %d hours above target\n\n", Math.abs(sleepDiff)));
            } else {
                progress.append("  Status: Target achieved!\n\n");
            }
        } else {
            progress.append("Sleep: No target set\n\n");
        }
        
        // Compare water intake
        if (goal.getTargetWaterIntake() != null) {
            double waterDiff = latest.getWaterIntake() - goal.getTargetWaterIntake();
            progress.append("Water Intake:\n");
            progress.append(String.format("  Current: %.2f liters\n", latest.getWaterIntake()));
            progress.append(String.format("  Target: %.2f liters\n", goal.getTargetWaterIntake()));
            
            if (waterDiff < 0) {
                progress.append(String.format("  Status: %.2f liters below target\n\n", Math.abs(waterDiff)));
            } else if (waterDiff > 0) {
                progress.append(String.format("  Status: %.2f liters above target\n\n", Math.abs(waterDiff)));
            } else {
                progress.append("  Status: Target achieved!\n\n");
            }
        } else {
            progress.append("Water Intake: No target set\n\n");
        }
        
        // Overall status
        boolean allGoalsMet = goal.isGoalMet(latest);
        progress.append("Overall Status: " + (allGoalsMet ? "All targets achieved!" : "Some targets not yet achieved"));
        
        return progress.toString();
    }
}