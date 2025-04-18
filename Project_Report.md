# Health Tracker Application - Project Report

## Personal Information
* Name: [Your Name]
* Student ID: [Your ID]

## Project Overview
This Java application helps users track their health metrics, mood, and progress towards health goals. The main objectives are:
- Provide a centralized platform for health data tracking
- Offer insights through summary reports and progress tracking
- Help users correlate mood with health metrics
- Support long-term health goal achievement

## Project Details
### Architecture (OOP/SOLID Principles)
The application follows object-oriented design principles with these main classes:
- `HealthTrackerApp`: Main application class
- `UserProfile`: Manages user data and records
- `ConsoleUI`: Handles all user interaction
- `FileStorageService`: Manages data persistence
- `SummaryService`: Generates summary reports
- `ProgressTracker`: Tracks progress against goals
- `MoodTracker`: Analyzes mood patterns

Data is persisted to local text files in the `data/` directory.

## Features
### Implemented Features
- **Health Record Tracking**: Record weight, blood pressure, exercise, and other metrics
- **Mood Tracking**: Log daily moods with optional notes
- **Goal Setting**: Set and update health goals
- **Progress Analysis**: Compare current health metrics with goals
- **Data Persistence**: Save records and goals to local files
- **Mood-Health Correlation**: Analyze relationships between mood and health metrics

## Technology Used
- Programming Language: Java
- Data Storage: Local text files
- Development Tools: Standard Java Development Kit (JDK)

## Justification
This project aligns with health tracking themes by providing comprehensive tools for monitoring various health metrics and mood patterns. The object-oriented design ensures maintainability and extensibility for future enhancements.