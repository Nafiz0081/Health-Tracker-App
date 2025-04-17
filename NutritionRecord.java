import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class NutritionRecord {
    private LocalDate date;
    private String mealType; // Breakfast, Lunch, Dinner, Snack
    private String foodName;
    private double calories;
    private double protein; // in grams
    private double carbs; // in grams
    private double fat; // in grams
    private double fiber; // in grams
    private Map<String, Double> micronutrients; // Optional micronutrients (vitamins, minerals)
    
    // Constructor
    public NutritionRecord(LocalDate date, String mealType, String foodName, double calories, 
                          double protein, double carbs, double fat, double fiber) {
        this.date = date;
        this.mealType = mealType;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.fiber = fiber;
        this.micronutrients = new HashMap<>();
    }
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getMealType() {
        return mealType;
    }
    
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public double getCalories() {
        return calories;
    }
    
    public void setCalories(double calories) {
        this.calories = calories;
    }
    
    public double getProtein() {
        return protein;
    }
    
    public void setProtein(double protein) {
        this.protein = protein;
    }
    
    public double getCarbs() {
        return carbs;
    }
    
    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }
    
    public double getFat() {
        return fat;
    }
    
    public void setFat(double fat) {
        this.fat = fat;
    }
    
    public double getFiber() {
        return fiber;
    }
    
    public void setFiber(double fiber) {
        this.fiber = fiber;
    }
    
    public Map<String, Double> getMicronutrients() {
        return micronutrients;
    }
    
    public void addMicronutrient(String name, double amount) {
        micronutrients.put(name, amount);
    }
    
    // Convert record to string for file storage
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(date.format(DateTimeFormatter.ISO_LOCAL_DATE)).append(",");
        sb.append(mealType).append(",");
        sb.append(foodName.replace(",", "[comma]")).append(",");
        sb.append(calories).append(",");
        sb.append(protein).append(",");
        sb.append(carbs).append(",");
        sb.append(fat).append(",");
        sb.append(fiber);
        
        // Add micronutrients if any
        if (!micronutrients.isEmpty()) {
            sb.append(",");
            for (Map.Entry<String, Double> entry : micronutrients.entrySet()) {
                sb.append(entry.getKey().replace(":", "[colon]"))
                  .append(":")
                  .append(entry.getValue())
                  .append("|");
            }
            // Remove the last pipe character
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return sb.toString();
    }
    
    // Create NutritionRecord from file string
    public static NutritionRecord fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid nutrition record format: " + line);
        }
        
        LocalDate date = LocalDate.parse(parts[0]);
        String mealType = parts[1];
        String foodName = parts[2].replace("[comma]", ",");
        double calories = Double.parseDouble(parts[3]);
        double protein = Double.parseDouble(parts[4]);
        double carbs = Double.parseDouble(parts[5]);
        double fat = Double.parseDouble(parts[6]);
        double fiber = Double.parseDouble(parts[7]);
        
        NutritionRecord record = new NutritionRecord(date, mealType, foodName, calories, protein, carbs, fat, fiber);
        
        // Parse micronutrients if present
        if (parts.length > 8) {
            String[] micronutrientParts = parts[8].split("\\|");
            for (String micronutrient : micronutrientParts) {
                String[] nutrientParts = micronutrient.split(":");
                if (nutrientParts.length == 2) {
                    String name = nutrientParts[0].replace("[colon]", ":");
                    double amount = Double.parseDouble(nutrientParts[1]);
                    record.addMicronutrient(name, amount);
                }
            }
        }
        
        return record;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
          .append("\nMeal: ").append(mealType)
          .append("\nFood: ").append(foodName)
          .append("\nCalories: ").append(calories)
          .append("\nMacronutrients:")
          .append("\n  Protein: ").append(protein).append("g")
          .append("\n  Carbs: ").append(carbs).append("g")
          .append("\n  Fat: ").append(fat).append("g")
          .append("\n  Fiber: ").append(fiber).append("g");
        
        if (!micronutrients.isEmpty()) {
            sb.append("\nMicronutrients:");
            for (Map.Entry<String, Double> entry : micronutrients.entrySet()) {
                sb.append("\n  ").append(entry.getKey()).append(": ").append(entry.getValue());
            }
        }
        
        return sb.toString();
    }

}