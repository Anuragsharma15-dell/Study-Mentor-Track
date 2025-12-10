# 🎓 StudyMentor - AI-Powered Study Assistant

A terminal-based AI study companion supporting OpenAI (ChatGPT) and Google Gemini.

## ✨ Features (50-60% Implementation)

- ✅ **Multi-AI Support**: Switch between OpenAI (ChatGPT) and Gemini
- ✅ **Ask Questions**: Get instant answers to academic questions
- ✅ **Study Plans**: Generate customized study schedules
- ✅ **Concept Explanation**: Explain topics in different styles
- ✅ **Motivation**: Get motivational advice and tips
- ✅ **Progress Tracking**: View learning statistics
- ✅ **Profile Management**: Customize your profile
- ✅ **History Export**: Save conversation history

## 🚀 Installation

### Prerequisites

- **Java 11 or higher** installed on your system
- An API key from at least one AI provider:
  - [OpenAI (ChatGPT)](https://platform.openai.com/) - Get API key
  - [Google Gemini](https://ai.google.dev/) - Get API key

### Setup Steps

1. **Download the project files** (StudyMentor.java, Profile.java, AIProvider.java, DataHandler.java, ProgressTracker.java)

2. **Compile the Java files**
   ```bash
   javac *.java
   ```

3. **Set up your API key** (choose one or both)

   **For Windows (Command Prompt):**
   ```cmd
   set OPENAI_API_KEY=your-openai-api-key-here
   set GOOGLE_API_KEY=your-gemini-api-key-here
   ```

   **For Windows (PowerShell):**
   ```powershell
   $env:OPENAI_API_KEY='your-openai-api-key-here'
   $env:GOOGLE_API_KEY='your-gemini-api-key-here'
   ```

   **For Linux/Mac:**
   ```bash
   export OPENAI_API_KEY='your-openai-api-key-here'
   export GOOGLE_API_KEY='your-gemini-api-key-here'
   ```

4. **Run the application**
   ```bash
   java StudyMentor
   ```

## 💻 Usage

### First Time Setup

When you run StudyMentor for the first time:

1. Enter your name, grade, email (optional), and subjects
2. Choose your preferred AI provider (OpenAI or Gemini)
3. Start using the features!

### Main Menu Options

- **1. 💬 Ask a Question** - Get answers to any academic question
- **2. 📋 Create Study Plan** - Generate personalized study schedules
- **3. 🧠 Explain Concept** - Get explanations in different styles (ELI5, Technical, Visual, Analogy)
- **4. 💪 Get Motivation** - Receive motivational advice
- **5. 📈 View Progress** - See your learning statistics
- **6. 👤 Manage Profile** - Update your profile information
- **7. 💾 Export History** - Save conversation history to a text file
- **8. 🔄 Change AI Provider** - Switch between OpenAI and Gemini
- **9. 📊 View Statistics** - Detailed statistics view
- **0. 🚪 Exit** - Save and exit

## 📁 Data Storage

StudyMentor stores data locally:

- `studymentor_config.json` - Your profile information
- `study_history.json` - Conversation history
- `study_stats.json` - Statistics and progress
- `study_history_*.txt` - Exported conversation logs

## ⚙️ Configuration

### Switching AI Providers

You can switch between AI providers at any time:
1. Select option **8** from the main menu
2. Choose OpenAI or Gemini
3. Make sure the corresponding API key is set
4. Continue learning!

### Profile Management

Your profile includes:
- Name
- Grade/Level
- Email (optional)
- Subjects you're studying
- Preferred AI provider

## 🛠️ Troubleshooting

### API Key Not Found

```
⚠️  Warning: OpenAI API key not found!
```

**Solution**: Make sure you've set the API key in your terminal before running the program.

### Connection Errors

```
❌ Error: API Error: 401
```

**Solution**: 
- Verify your API key is correct
- Check if you have sufficient API credits/quota
- Ensure you're using the correct environment variable name

### Compilation Errors

```
error: cannot find symbol
```

**Solution**: 
- Ensure all Java files are in the same directory
- Make sure you're using Java 11 or higher: `java -version`
- Compile all files: `javac *.java`

## 📝 Example Usage

### Asking a Question

```
💬 Ask Your Question
──────────────────────────────────────────────────────────────
Your question: Explain the Pythagorean theorem

🤖 OpenAI is thinking...

📝 Response:
──────────────────────────────────────────────────────────────
The Pythagorean theorem states that in a right triangle...
```

### Creating a Study Plan

```
📋 Study Plan Generator
──────────────────────────────────────────────────────────────
Subject/Topic: Organic Chemistry
Duration (days): 30
Daily study time (hours): 2
Current knowledge level: Beginner

🤖 Gemini is thinking...

📝 30-Day Organic Chemistry Study Plan Generated!
```

## 🎯 Tips for Best Results

1. **Be Specific**: The more detailed your questions, the better the AI can help
2. **Use Study Plans**: Structure your learning for maximum efficiency
3. **Export History**: Keep records of important explanations
4. **Try Both AIs**: Test which provider works best for your needs
5. **Regular Sessions**: Consistent practice improves learning

## 📄 Notes

This is a 50-60% implementation featuring:
- ✅ Core functionality (Ask Questions, Study Plans, Concept Explanation, Motivation)
- ✅ Profile management
- ✅ Progress tracking
- ✅ History export
- ✅ Dual AI provider support (OpenAI & Gemini)

Not yet implemented (for future versions):
- ⏳ Quiz Mode
- ⏳ Flashcard Generator
- ⏳ Problem Solver
- ⏳ Advanced analytics
- ⏳ Spaced repetition

## 📄 License

This project is open source and available for educational purposes.

## 🙏 Acknowledgments

- OpenAI for ChatGPT API
- Google for Gemini API

---

**Happy Studying! 🎓**



     