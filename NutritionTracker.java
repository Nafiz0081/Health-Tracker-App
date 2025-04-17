import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NutritionTracker {
    
    // Daily recommended values (can be customized per user in future)
    private static final double RECOMMENDED_CALORIES = 2000.0; // calories
    private static final double RECOMMENDED_PROTEIN = 50.0;   // grams
    private static final double RECOMMENDED_CARBS = 275.0;    // grams
    private static final double RECOMMENDED_FAT = 78.0;       // grams
    private static final double RECOMMENDED_FIBER = 28.0;     // grams
    
    // Generate nutrition summary for a given period
    public String generateNutritionSummary(List<NutritionRecord> records, int days) {
        if (records == null || records.isEmpty()) {
            return "No nutrition records available for analysis.";
        }
        
        // Filter records for the specified period
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days);
        
        List<NutritionRecord> filteredRecords = records.stream()
                .filter(record -> !record.getDate().isBefore(startDate) && !record.getDate().isAfter(today))
                .collect(Collectors.toList());
        
        if (filteredRecords.isEmpty()) {
            return "No nutrition records found for the past " + days + " days.";
        }
        
        // Calculate daily averages
        Map<LocalDate, List<NutritionRecord>> recordsByDate = filteredRecords.stream()
                .collect(Collectors.groupingBy(NutritionRecord::getDate));
        
        // Calculate statistics
        DoubleSummaryStatistics caloriesStats = new DoubleSummaryStatistics();
        DoubleSummaryStatistics proteinStats = new DoubleSummaryStatistics();
        DoubleSummaryStatistics carbsStats = new DoubleSummaryStatistics();
        DoubleSummaryStatistics fatStats = new DoubleSummaryStatistics();
        DoubleSummaryStatistics fiberStats = new DoubleSummaryStatistics();
        
        // Calculate daily totals and add to statistics
        for (Map.Entry<LocalDate, List<NutritionRecord>> entry : recordsByDate.entrySet()) {
            double dailyCalories = entry.getValue().stream().mapToDouble(NutritionRecord::getCalories).sum();
            double dailyProtein = entry.getValue().stream().mapToDouble(NutritionRecord::getProtein).sum();
            double dailyCarbs = entry.getValue().stream().mapToDouble(NutritionRecord::getCarbs).sum();
            double dailyFat = entry.getValue().stream().mapToDouble(NutritionRecord::getFat).sum();
            double dailyFiber = entry.getValue().stream().mapToDouble(NutritionRecord::getFiber).sum();
            
            caloriesStats.accept(dailyCalories);
            proteinStats.accept(dailyProtein);
            carbsStats.accept(dailyCarbs);
            fatStats.accept(dailyFat);
            fiberStats.accept(dailyFiber);
        }
        
        // Build the summary report
        StringBuilder report = new StringBuilder();
        report.append("Nutrition Summary Report\n");
        report.append("========================\n\n");
        report.append(String.format("Period: Last %d days\n", days));
        report.append(String.format("Days with records: %d\n\n", recordsByDate.size()));
        
        report.append("Daily Averages:\n");
        report.append(String.format("  Calories: %.1f (%.1f%% of recommended)\n", 
                caloriesStats.getAverage(), (caloriesStats.getAverage() / RECOMMENDED_CALORIES) * 100));
        report.append(String.format("  Protein: %.1fg (%.1f%% of recommended)\n", 
                proteinStats.getAverage(), (proteinStats.getAverage() / RECOMMENDED_PROTEIN) * 100));
        report.append(String.format("  Carbs: %.1fg (%.1f%% of recommended)\n", 
                carbsStats.getAverage(), (carbsStats.getAverage() / RECOMMENDED_CARBS) * 100));
        report.append(String.format("  Fat: %.1fg (%.1f%% of recommended)\n", 
                fatStats.getAverage(), (fatStats.getAverage() / RECOMMENDED_FAT) * 100));
        report.append(String.format("  Fiber: %.1fg (%.1f%% of recommended)\n\n", 
                fiberStats.getAverage(), (fiberStats.getAverage() / RECOMMENDED_FIBER) * 100));
        
        // Most common meal types
        Map<String, Long> mealTypeCounts = filteredRecords.stream()
                .collect(Collectors.groupingBy(NutritionRecord::getMealType, Collectors.counting()));
        
        report.append("Meal Distribution:\n");
        mealTypeCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(entry -> {
                    double percentage = (entry.getValue() * 100.0) / filteredRecords.size();
                    report.append(String.format("  %s: %.1f%%\n", entry.getKey(), percentage));
                });
        
        // Nutrition insights
        report.append("\nNutrition Insights:\n");
        
        // Calorie balance
        if (caloriesStats.getAverage() < RECOMMENDED_CALORIES * 0.8) {
            report.append("  • Your calorie intake is significantly below recommendations. " +
                          "Consider increasing your food intake.\n");
        } else if (caloriesStats.getAverage() > RECOMMENDED_CALORIES * 1.2) {
            report.append("  • Your calorie intake is above recommendations. " +
                          "Consider moderating your food intake.\n");
        } else {
            report.append("  • Your calorie intake is within a healthy range.\n");
        }
        
        // Protein intake
        if (proteinStats.getAverage() < RECOMMENDED_PROTEIN * 0.8) {
            report.append("  • Your protein intake is low. Consider adding more lean meats, " +
                          "dairy, beans, or protein supplements.\n");
        }
        
        // Fiber intake
        if (fiberStats.getAverage() < RECOMMENDED_FIBER * 0.8) {
            report.append("  • Your fiber intake is low. Try to eat more fruits, vegetables, " +
                          "and whole grains.\n");
        }
        
        // Macronutrient balance
        double totalMacros = proteinStats.getAverage() + carbsStats.getAverage() + fatStats.getAverage();
        if (totalMacros > 0) {
            double proteinPercentage = (proteinStats.getAverage() / totalMacros) * 100;
            double carbsPercentage = (carbsStats.getAverage() / totalMacros) * 100;
            double fatPercentage = (fatStats.getAverage() / totalMacros) * 100;
            
            report.append(String.format("\nMacronutrient Distribution:\n"));
            report.append(String.format("  Protein: %.1f%%\n", proteinPercentage));
            report.append(String.format("  Carbs: %.1f%%\n", carbsPercentage));
            report.append(String.format("  Fat: %.1f%%\n", fatPercentage));
            
            // Check if macronutrient distribution is balanced
            if (proteinPercentage < 10 || proteinPercentage > 35) {
                report.append("  • Your protein distribution is outside the recommended range (10-35%).\n");
            }
            if (carbsPercentage < 45 || carbsPercentage > 65) {
                report.append("  • Your carbohydrate distribution is outside the recommended range (45-65%).\n");
            }
            if (fatPercentage < 20 || fatPercentage > 35) {
                report.append("  • Your fat distribution is outside the recommended range (20-35%).\n");
            }
        }
        
        return report.toString();
    }
    
    // Calculate daily calorie needs based on user profile (can be expanded)
    public double calculateDailyCalorieNeeds(double weight, int age, String gender, String activityLevel) {
        // Basic BMR calculation using Mifflin-St Jeor Equation
        double bmr = 0;
        if (gender.equalsIgnoreCase("male")) {
            bmr = 10 * weight + 6.25 * 170 - 5 * age + 5; // Assuming average height of 170cm
        } else {
            bmr = 10 * weight + 6.25 * 160 - 5 * age - 161; // Assuming average height of 160cm
        }
        
        // Activity multiplier
        double activityMultiplier = 1.2; // Sedentary default
        switch (activityLevel.toLowerCase()) {
            case "sedentary": activityMultiplier = 1.2; break;
            case "light": activityMultiplier = 1.375; break;
            case "moderate": activityMultiplier = 1.55; break;
            case "active": activityMultiplier = 1.725; break;
            case "very active": activityMultiplier = 1.9; break;
        }
        
        return bmr * activityMultiplier;
    }
    
    // Generate meal suggestions based on nutritional goals
    public String generateMealSuggestions(double targetCalories, double targetProtein) {
        StringBuilder suggestions = new StringBuilder();
        suggestions.append("Meal Suggestions\n");
        suggestions.append("================\n\n");
        suggestions.append(String.format("Based on your target of %.0f calories and %.0fg protein:\n\n", 
                targetCalories, targetProtein));
        
        // Breakfast suggestions
        suggestions.append("Breakfast Options:\n");
        suggestions.append("  • Greek yogurt with berries and honey (300 cal, 20g protein)\n");
        suggestions.append("  • Oatmeal with milk, banana, and peanut butter (400 cal, 15g protein)\n");
        suggestions.append("  • Vegetable omelet with whole grain toast (350 cal, 22g protein)\n\n");
        
        // Lunch suggestions
        suggestions.append("Lunch Options:\n");
        suggestions.append("  • Grilled chicken salad with olive oil dressing (450 cal, 35g protein)\n");
        suggestions.append("  • Tuna sandwich on whole grain bread with side salad (500 cal, 30g protein)\n");
        suggestions.append("  • Lentil soup with whole grain roll (400 cal, 18g protein)\n\n");
        
        // Dinner suggestions
        suggestions.append("Dinner Options:\n");
        suggestions.append("  • Baked salmon with quinoa and roasted vegetables (550 cal, 40g protein)\n");
        suggestions.append("  • Lean beef stir-fry with brown rice (600 cal, 35g protein)\n");
        suggestions.append("  • Bean and vegetable chili with small side of cornbread (500 cal, 25g protein)\n\n");
        
        // Snack suggestions
        suggestions.append("Snack Options:\n");
        suggestions.append("  • Apple with 2 tbsp almond butter (200 cal, 7g protein)\n");
        suggestions.append("  • Protein smoothie with fruit (250 cal, 20g protein)\n");
        suggestions.append("  • Hummus with carrot and celery sticks (150 cal, 5g protein)\n");
        
        return suggestions.toString();
    }
}