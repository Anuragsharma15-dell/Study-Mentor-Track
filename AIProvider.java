import java.net.*;
import java.io.*;

public class AIProvider {
    private String providerName;
    private String apiKey="";
    private boolean available;

    public AIProvider(String providerName) {
        this.providerName = providerName;
        this.apiKey = getAPIKey();
        this.available = (apiKey != null && !apiKey.isEmpty());
    }

    private String getAPIKey() {
        String key = null;
        if (providerName.equalsIgnoreCase("OpenAI")) {
            key = System.getenv("OPENAI_API_KEY");
        } else {
            key = System.getenv("GEMINI_API_KEY");
            if (key == null || key.isEmpty()) {
                key = System.getenv("GOOGLE_API_KEY");
            }
        }
        return key;
    }

    public boolean isAvailable() {
        return available;
    }

    public String askQuestion(String question) throws Exception {
        if (!available) {
            throw new Exception("API key not available for " + providerName);
        }

        if (providerName.equalsIgnoreCase("OpenAI")) {
            return callOpenAI(question);
        } else {
            return callGemini(question);
        }
    }

    // ============================
    // OpenAI integration
    // ============================
    private String callOpenAI(String question) throws Exception {
    String apiUrl = "https://api.openai.com/v1/chat/completions";

    URL url = new URL(apiUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Authorization", "Bearer " + apiKey);
    conn.setDoOutput(true);

    String jsonInput = "{"
            + "\"model\": \"gpt-3.5-turbo\","
            + "\"messages\": [{\"role\": \"user\", \"content\": \"" + escapeJson(question) + "\"}]"
            + "}"; //GSON 

    try (OutputStream os = conn.getOutputStream()) {
        os.write(jsonInput.getBytes("UTF-8"));
    }

    int code = conn.getResponseCode();
    InputStream stream = (code == 200) ? conn.getInputStream() : conn.getErrorStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) response.append(line);

    if (code != 200) {
        throw new Exception("OpenAI API Error: " + response.toString());
    }

    String resp = response.toString();
    int idx = resp.indexOf("\"content\":\"");
    if (idx == -1) return resp;

    int start = idx + 10;
    int end = resp.indexOf("\"", start);
    if (start < 0 || end < start) return resp;

    return unescapeJson(resp.substring(start, end));
}

    // Gemini integration
    // ============================
    private String callGemini(String question) throws Exception {
        try {
            String model = "gemini-2.1"; // safer than flash for v1beta
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model
                    + ":generateContent?key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            String jsonInputString = "{"
                    + "\"contents\": [{\"parts\": [{\"text\": \"" + escapeJson(question) + "\"}]}]"
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInputString.getBytes("utf-8"));
            }

            int responseCode = conn.getResponseCode();
            InputStream stream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            if (responseCode != 200) {
                throw new Exception("Gemini API Error: " + responseCode + " - " + response.toString());
            }

            // Extract text content
            String resp = response.toString();
            int idx = resp.indexOf("\"text\":");
            if (idx == -1) return resp;

            int start = resp.indexOf("\"", idx + 7) + 1;
            int end = resp.indexOf("\"", start);
            if (start < 0 || end < start) return resp;

            return unescapeJson(resp.substring(start, end));

        } catch (Exception e) {
            throw new Exception("Error calling Gemini API: " + e.getMessage());
        }
    }

    // ============================
    // JSON helpers
    // ============================
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String unescapeJson(String str) {
        return str.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}
                