import java.util.*;

public class ProgressTracker {
    private int questionsAsked;
    private int studyPlansCreated;
    private int motivationSessions;
    private int totalSessions;
    private int totalQuestions;
    
    public ProgressTracker() {
        this.questionsAsked = 0;
        this.studyPlansCreated = 0;
        this.motivationSessions = 0;
        this.totalSessions = 1;
        this.totalQuestions = 0;
    }
    
    public void recordQuestion() {
        questionsAsked++;
        totalQuestions++;
    }
    
    public void recordStudyPlan() {
        studyPlansCreated++;
    }
    
    public void recordMotivation() {
        motivationSessions++;
    }
    
    public void loadStats(DataHandler dataHandler) {
        // Stats are loaded from file if exists
        // For simplicity, we'll keep session-based tracking
    }
    
    public void saveStats(DataHandler dataHandler) {
        dataHandler.saveStats(this);
    }
    
    public void displayStatistics() {
        System.out.println(Colors.YELLOW + "\n📊 Current Session:" + Colors.RESET);
        System.out.println("   Questions Asked: " + questionsAsked);
        System.out.println("   Study Plans: " + studyPlansCreated);
        System.out.println("   Motivation Sessions: " + motivationSessions);
        
        System.out.println(Colors.YELLOW + "\n📈 Overall Statistics:" + Colors.RESET);
        System.out.println("   Total Sessions: " + totalSessions);
        System.out.println("   Total Questions: " + totalQuestions);
        if (totalSessions > 0) {
            System.out.println("   Avg Questions/Session: " + (totalQuestions / totalSessions));
        }
    }
    
    // Getters
    public int getQuestionsAsked() { return questionsAsked; }
    public int getStudyPlansCreated() { return studyPlansCreated; }
    public int getMotivationSessions() { return motivationSessions; }
    public int getTotalSessions() { return totalSessions; }
    public int getTotalQuestions() { return totalQuestions; }
}

