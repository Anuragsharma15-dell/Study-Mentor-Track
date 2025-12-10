import java.io.*;
import java.util.*;

public class DataHandler {
    private static final String PROFILE_FILE = "studymentor_config.json";
    private static final String HISTORY_FILE = "study_history.json";
    private static final String STATS_FILE = "study_stats.json";
    
    public Profile loadProfile() {
        try {
            File file = new File(PROFILE_FILE);
            if (!file.exists()) {
                return null;
            }
            
            Scanner scanner = new Scanner(file);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine());
            }
            scanner.close();
            
            return parseProfile(json.toString());
            
        } catch (Exception e) {
            return null;
        }
    }
    
    public void saveProfile(Profile profile) {
        try {
            FileWriter writer = new FileWriter(PROFILE_FILE);
            writer.write("{\n");
            writer.write("  \"name\": \"" + escapeJson(profile.getName()) + "\",\n");
            writer.write("  \"grade\": \"" + escapeJson(profile.getGrade()) + "\",\n");
            writer.write("  \"email\": \"" + escapeJson(profile.getEmail()) + "\",\n");
            writer.write("  \"preferredAI\": \"" + profile.getPreferredAI() + "\",\n");
            writer.write("  \"subjects\": [");
            List<String> subjects = profile.getSubjects();
            for (int i = 0; i < subjects.size(); i++) {
                writer.write("\"" + escapeJson(subjects.get(i)) + "\"");
                if (i < subjects.size() - 1) writer.write(", ");
            }
            writer.write("]\n");
            writer.write("}\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving profile: " + e.getMessage());
        }
    }
    
    public void saveHistory(List<String> history) {
        try {
            FileWriter writer = new FileWriter(HISTORY_FILE);
            writer.write("[\n");
            for (int i = 0; i < history.size(); i++) {
                writer.write("  \"" + escapeJson(history.get(i)) + "\"");
                if (i < history.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }
    
    public List<String> loadHistory() {
        List<String> history = new ArrayList<>();
        try {
            File file = new File(HISTORY_FILE);
            if (!file.exists()) {
                return history;
            }
            
            Scanner scanner = new Scanner(file);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()) {
                json.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            
            // Improved JSON array parsing
            String jsonStr = json.toString();
            int startIdx = jsonStr.indexOf("[");
            int endIdx = jsonStr.lastIndexOf("]");
            if (startIdx >= 0 && endIdx > startIdx) {
                String arrayContent = jsonStr.substring(startIdx + 1, endIdx);
                String[] parts = arrayContent.split("\"");
                for (int i = 1; i < parts.length; i += 2) {
                    if (i < parts.length && parts[i].trim().length() > 0) {
                        history.add(unescapeJson(parts[i]));
                    }
                }
            }
            
        } catch (Exception e) {
            // Return empty list on error
        }
        return history;
    }
    
    public void saveStats(ProgressTracker tracker) {
        try {
            FileWriter writer = new FileWriter(STATS_FILE);
            writer.write("{\n");
            writer.write("  \"questionsAsked\": " + tracker.getQuestionsAsked() + ",\n");
            writer.write("  \"studyPlansCreated\": " + tracker.getStudyPlansCreated() + ",\n");
            writer.write("  \"motivationSessions\": " + tracker.getMotivationSessions() + ",\n");
            writer.write("  \"totalSessions\": " + tracker.getTotalSessions() + ",\n");
            writer.write("  \"totalQuestions\": " + tracker.getTotalQuestions() + "\n");
            writer.write("}\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving stats: " + e.getMessage());
        }
    }
    
    private Profile parseProfile(String json) {
        try {
            String name = extractField(json, "name");
            String grade = extractField(json, "grade");
            String email = extractField(json, "email");
            String preferredAI = extractField(json, "preferredAI");
            List<String> subjects = extractArray(json, "subjects");
            
            return new Profile(name, grade, email, subjects, preferredAI);
        } catch (Exception e) {
            return null;
        }
    }
    
    private String extractField(String json, String fieldName) {
        int start = json.indexOf("\"" + fieldName + "\":\"") + fieldName.length() + 4;
        int end = json.indexOf("\"", start);
        if (end > start) {
            return unescapeJson(json.substring(start, end));
        }
        return "";
    }
    
    private List<String> extractArray(String json, String fieldName) {
        List<String> result = new ArrayList<>();
        String searchKey = "\"" + fieldName + "\":";
        int start = json.indexOf(searchKey);
        if (start < 0) return result;
        
        start = json.indexOf("[", start) + 1;
        int end = json.indexOf("]", start);
        if (end > start) {
            String arrayStr = json.substring(start, end);
            String[] parts = arrayStr.split("\"");
            for (int i = 1; i < parts.length; i += 2) {
                if (i < parts.length && parts[i].trim().length() > 0) {
                    result.add(unescapeJson(parts[i]));
                }
            }
        }
        return result;
    }
    
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n");
    }
    
    private String unescapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\n", "\n")
                  .replace("\\\"", "\"")
                  .replace("\\\\", "\\");
    }
}

