# Health Tracker Application

## Project Overview
This Java application helps users track their health metrics, mood, and progress towards health goals. The main goals are:
- Provide a centralized platform for health data tracking
- Offer insights through summary reports and progress tracking
- Help users correlate mood with health metrics
- Support long-term health goal achievement

## Key Features

### Implemented Features
- **Health Record Tracking**: Record weight, blood pressure, exercise, and other metrics
- **Mood Tracking**: Log daily moods with optional notes
- **Goal Setting**: Set and update health goals
- **Progress Analysis**: Compare current health metrics with goals
- **Data Persistence**: Save records and goals to local files
- **Mood-Health Correlation**: Analyze relationships between mood and health metrics

### Future Prospects
- Medication reminders with scheduling
- Health metrics visualization (charts/graphs)
- Social sharing features
- Nutrition tracking integration

## Structural Design (OOP/SOLID Principles)

### SOLID Principles Applied
1. **Single Responsibility Principle**:
   - Each class has a single responsibility (e.g., `UserProfile` manages user data, `FileStorageService` handles file operations)
   - Separate classes for UI (`ConsoleUI`), business logic (`SummaryService`, `ProgressTracker`), and data storage

2. **Open/Closed Principle**:
   - Classes are open for extension but closed for modification
   - New features can be added without changing existing code (e.g., adding new trackers)

3. **Liskov Substitution Principle**:
   - Subtypes (`HealthRecord`, `MoodRecord`) can substitute their parent types

4. **Interface Segregation Principle**:
   - Small, focused interfaces rather than large general ones

5. **Dependency Inversion Principle**:
   - High-level modules depend on abstractions (interfaces) rather than concrete implementations

### Class Structure Overview
- **Main Classes**:
  - `HealthTrackerApp`: Main application class
  - `UserProfile`: Manages user data and records
  - `ConsoleUI`: Handles all user interaction
  - `FileStorageService`: Manages data persistence
  - `SummaryService`: Generates summary reports
  - `ProgressTracker`: Tracks progress against goals
  - `MoodTracker`: Analyzes mood patterns

### Data Storage
- Records stored in `data/records.txt`
- Goals stored in `data/goals.txt`
- Mood records stored in `data/mood_records.txt`

## How to Run
1. Compile all Java files
2. Run `HealthTrackerApp`
3. Follow the console menu instructions