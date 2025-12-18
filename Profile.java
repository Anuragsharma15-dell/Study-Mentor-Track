import java.util.*;



public class Profile {
    private String name;
    private String grade;
    private String email;
    private List<String> subjects;
    private String preferredAI;
   
    public Profile(String name, String grade, String email, List<String> subjects, String preferredAI, String  response) {
        this.name = name;
        this.grade = grade;
        this.email = email;
        this.subjects = subjects;
        this.preferredAI = preferredAI;
        

    }
    
    // Getters
    public String getName() { return name; }
    public String getGrade() { return grade; }
    public String getEmail() { return email; }
    public List<String> getSubjects() { return subjects; }
    public String getPreferredAI() { return preferredAI; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setEmail(String email) { this.email = email; }
    public void setSubjects(List<String> subjects) { this.subjects = subjects; }
    public void setPreferredAI(String preferredAI) { this.preferredAI = preferredAI; }
    public void setprefeered(String response){
        
       
    
    }

}
