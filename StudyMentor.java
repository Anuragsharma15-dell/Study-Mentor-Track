import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class StudyMentor {



    private static DataHandler dataHandler;
    private static ProgressTracker progressTracker;
    private static AIProvider aiProvider;
    private static Scanner scanner;
    private static Profile profile; // FIXED: added missing profile variable
    private static String currentAIProvider = "OpenAI"; 
    private static List<String> conversationHistory;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);
        dataHandler = new DataHandler();
        conversationHistory = dataHandler.loadHistory();

        System.out.println("\n" + Colors.BLUE + "═══════════════════════════════════════════════════════════════");
        System.out.println(Colors.CYAN + "🎓 Welcome to StudyMentor - Your AI-Powered Study Assistant!");
        System.out.println(Colors.BLUE + "═══════════════════════════════════════════════════════════════" + Colors.RESET);

        profile = dataHandler.loadProfile();
        if (profile == null) {
            createProfile();
        } else {
            System.out.println(Colors.GREEN + "\n✅ Profile loaded: " + profile.getName() + Colors.RESET);
            currentAIProvider = profile.getPreferredAI();
        }

        progressTracker = new ProgressTracker();
        progressTracker.loadStats(dataHandler);

        initializeAIProvider();

        boolean running = true;

        while (running) {
            showMainMenu();
            int choice = getChoice(13);

            switch (choice) {
                case 1 -> askQuestion();
                case 2 -> createStudyPlan();
                case 3 -> explainConcept();
                case 4 -> getMotivation();
                case 5 -> viewProgress();
                case 6 -> manageProfile();
                case 7 -> exportHistory();
                case 8 -> changeAIProvider();
                case 9 -> viewStatistics();
                case 0 -> {
                    System.out.println(Colors.YELLOW + "\n👋 Thanks for using StudyMentor! Keep studying!" + Colors.RESET);
                    running = false;
                }
                default -> System.out.println(Colors.RED + "❌ Invalid choice. Try again." + Colors.RESET);
            }
        }

        dataHandler.saveProfile(profile);
        progressTracker.saveStats(dataHandler);
        scanner.close();
    }


    private static void initializeAIProvider() {
        aiProvider = new AIProvider(currentAIProvider);

        if (!aiProvider.isAvailable()) {
            System.out.println(Colors.RED + "\n⚠️ AI key not found for " + currentAIProvider + Colors.RESET);
            System.out.println(Colors.YELLOW + """
                    Set your environment variable:

                    OpenAI →  set OPENAI_API_KEY=your_key
                    Gemini →  set GOOGLE_API_KEY=your_key
                    """ + Colors.RESET);
        } else {
            System.out.println(Colors.GREEN + "✅ " + currentAIProvider + " initialized!" + Colors.RESET);
        }
    }


    private static void createProfile() {
        System.out.println(Colors.CYAN + "\n📝 Create Profile" + Colors.RESET);

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Grade/Level: ");
        String grade = scanner.nextLine();

        System.out.print("Email (optional): ");
        String email = scanner.nextLine();

        System.out.print("Subjects (comma-separated): ");
        List<String> subjects = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim).toList();

        System.out.println("\nChoose AI Provider:");
        System.out.println("1. OpenAI");
        System.out.println("2. Gemini");
        int choice = getChoice(2);

        currentAIProvider = (choice == 1) ? "OpenAI" : "Gemini";

        profile = new Profile(name, grade, email, subjects, currentAIProvider);

        dataHandler.saveProfile(profile);

        System.out.println(Colors.GREEN + "\n✅ Profile created!" + Colors.RESET);
    }


    private static void showMainMenu() {
        System.out.println("\n" + Colors.BLUE + "═══════════════════════════════════════════════════════════════");
        System.out.println(Colors.CYAN + "🏠 MAIN MENU | AI: " + currentAIProvider + " | Student: " + profile.getName());
        System.out.println(Colors.BLUE + "═══════════════════════════════════════════════════════════════" + Colors.RESET);

        System.out.println("""
                1. 💬 Ask Question
                2. 📋 Create Study Plan
                3. 🧠 Explain Concept
                4. 💪 Motivation

                5. 📈 View Progress
                6. 👤 Manage Profile
                7. 💾 Export History
                8. 🔄 Change AI Provider
                9. 📊 View Statistics

                0. 🚪 Exit
                """);

        System.out.print(Colors.CYAN + "Enter choice: " + Colors.RESET);
    }


    private static int getChoice(int max) {
        try {
            int c = Integer.parseInt(scanner.nextLine().trim());
            if (c >= 0 && c <= max) return c;
        } catch (Exception ignored) {}
        return -1;
    }


    private static void askQuestion() {
        System.out.println(Colors.CYAN + "\n💬 Ask your question" + Colors.RESET);
        System.out.print("Your question: ");
        String q = scanner.nextLine();

        if (!aiProvider.isAvailable()) {
            System.out.println(Colors.RED + "❌ AI Provider not available." + Colors.RESET);
            return;
        }

        System.out.println(Colors.YELLOW + "\n🤖 Thinking..." + Colors.RESET);

        try {
            String ans = aiProvider.askQuestion(q);
            System.out.println(Colors.GREEN + "\n📝 Answer:\n" + Colors.RESET + ans);

            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            conversationHistory.add("[" + time + "] Q: " + q);
            conversationHistory.add("[" + time + "] A: " + ans);

            dataHandler.saveHistory(conversationHistory);
            progressTracker.recordQuestion();

        } catch (Exception e) {
            System.out.println(Colors.RED + "❌ Error: " + e.getMessage() + Colors.RESET);
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void createStudyPlan() {
        System.out.print("\nSubject: ");
        String subject = scanner.nextLine();

        System.out.print("Duration (days): ");
        String days = scanner.nextLine();

        System.out.print("Daily hours: ");
        String hours = scanner.nextLine();

        System.out.print("Level (Beginner/Intermediate/Advanced): ");
        String level = scanner.nextLine();

        String prompt = """
                Create a %s-day study plan for %s.
                Level: %s
                Daily time: %s hours
                Include daily goals, topics, and weekly review.
                """.formatted(days, subject, level, hours);

        try {
            String ans = aiProvider.askQuestion(prompt);
            System.out.println(Colors.GREEN + "\n📋 Study Plan:\n" + Colors.RESET + ans);
            progressTracker.recordStudyPlan();

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void explainConcept() {
        System.out.print("\nConcept: ");
        String concept = scanner.nextLine();

        System.out.println("""
                1. ELI5
                2. Technical
                3. Visual
                4. Analogy
                """);

        int c = getChoice(4);

        String[] styles = {"ELI5", "Technical", "Visual", "Analogy"};
        String style = styles[c - 1];

        String prompt = "Explain " + concept + " in a " + style + " style.";

        try {
            String ans = aiProvider.askQuestion(prompt);
            System.out.println(Colors.GREEN + "\n🧠 Explanation:\n" + Colors.RESET + ans);

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void getMotivation() {
        try {
            String ans = aiProvider.askQuestion("Give motivational study tips.");
            System.out.println(Colors.GREEN + "\n💪 Motivation:\n" + Colors.RESET + ans);
            progressTracker.recordMotivation();

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void viewProgress() {
        System.out.println(Colors.CYAN + "\n📈 Progress" + Colors.RESET);
        System.out.println("Name: " + profile.getName());
        System.out.println("Grade: " + profile.getGrade());
        System.out.println("Subjects: " + String.join(", ", profile.getSubjects()));
        System.out.println("Sessions: " + progressTracker.getTotalSessions());
        System.out.println("Questions: " + progressTracker.getTotalQuestions());

        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }


    private static void manageProfile() {
        System.out.println("""
                1. View profile
                2. Change name
                3. Change grade
                4. Change subjects
                """);

        int c = getChoice(4);

        switch (c) {
            case 1 -> {
                System.out.println("Name: " + profile.getName());
                System.out.println("Grade: " + profile.getGrade());
                System.out.println("Email: " + profile.getEmail());
                System.out.println("Subjects: " + String.join(", ", profile.getSubjects()));
            }
            case 2 -> {
                System.out.print("New name: ");
                profile.setName(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("New grade: ");
                profile.setGrade(scanner.nextLine());
            }
            case 4 -> {
                System.out.print("New subjects: ");
                List<String> subs = Arrays.stream(scanner.nextLine().split(","))
                        .map(String::trim).toList();
                profile.setSubjects(subs);
            }
        }

        dataHandler.saveProfile(profile);
        System.out.println("Updated!");

        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }


    private static void exportHistory() {
        if (conversationHistory.isEmpty()) {
            System.out.println("⚠️ No history.");
            return;
        }

        try {
            String file = "study_history_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".txt";

            FileWriter fw = new FileWriter(file);

            for (String s : conversationHistory) fw.write(s + "\n");

            fw.close();

            System.out.println("Saved → " + file);

        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }

        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }


    private static void changeAIProvider() {
        System.out.println("""
                1. OpenAI
                2. Gemini
                """);

        int c = getChoice(2);

        String newProvider = (c == 1) ? "OpenAI" : "Gemini";

        if (!newProvider.equals(currentAIProvider)) {
            currentAIProvider = newProvider;
            profile.setPreferredAI(newProvider);
            dataHandler.saveProfile(profile);
            initializeAIProvider();
            System.out.println("Changed!");
        } else {
            System.out.println("Already selected.");
        }

        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }


    private static void viewStatistics() {
        progressTracker.displayStatistics();

        System.out.println("\nPress Enter...");
        scanner.nextLine();
    }

}
