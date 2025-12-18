import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestGeminiKeys {
    private static final String API_BASE = "https://generativelanguage.googleapis.com/v1/models/";
    private static final Gson gson = new Gson();
    
    // Test keys (will not be committed)
    private static final String[] TEST_KEYS = {
        "AIzaSyAN3cpNQqHw8cw7ibtikQidHs2wG6qJsac",
        "AIzaSyBzUl9nf82ViJfmtKTlECvtjzb0MAUxkHs",
        "AIzaSyCjIAjWwqSEUr8N3sVKVbs0ItDLRFbkyvQ"
    };
    
    // Models to test (actual available models from API)
    private static final String[] TEST_MODELS = {
        "gemini-2.0-flash-lite",      // Smallest/fastest
        "gemini-2.0-flash-lite-001",  // Alternative lite
        "gemini-2.0-flash",           // Standard
        "gemini-2.5-flash-lite",      // Newer lite
        "gemini-2.5-flash"            // Newer standard (avoid 2.5-pro as requested)
    };
    
    public static void main(String[] args) {
        System.out.println("Testing " + TEST_KEYS.length + " Gemini API keys with " + TEST_MODELS.length + " models...");
        System.out.println();
        
        int workingCount = 0;
        
        for (int i = 0; i < TEST_KEYS.length; i++) {
            String key = TEST_KEYS[i];
            String keyLabel = "Key " + (i + 1) + " (...)" + key.substring(key.length() - 4);
            
            System.out.println("========================================");
            System.out.println("Testing: " + keyLabel);
            System.out.println("========================================");
            
            boolean keyWorks = false;
            String workingModel = null;
            
            for (String model : TEST_MODELS) {
                System.out.print("  Testing model: " + model + " ... ");
                
                try {
                    String response = testModel(key, model);
                    if (response != null && !response.isEmpty()) {
                        System.out.println("✓ WORKS!");
                        System.out.println("    Response: " + response.substring(0, Math.min(50, response.length())) + "...");
                        keyWorks = true;
                        workingModel = model;
                        break; // Found a working model, move to next key
                    } else {
                        System.out.println("✗ No response");
                    }
                } catch (Exception e) {
                    System.out.println("✗ FAILED: " + e.getMessage());
                }
            }
            
            if (keyWorks) {
                System.out.println();
                System.out.println("  ✓✓✓ KEY WORKS with model: " + workingModel);
                workingCount++;
            } else {
                System.out.println();
                System.out.println("  ✗✗✗ KEY FAILED - All models returned errors");
            }
            
            System.out.println();
        }
        
        System.out.println("========================================");
        System.out.println("SUMMARY");
        System.out.println("========================================");
        System.out.println("Working keys: " + workingCount + " / " + TEST_KEYS.length);
        System.out.println();
        
        if (workingCount > 0) {
            System.out.println("✓ You have working Gemini API keys!");
            System.out.println("  These can be added to your .env.SECURE file");
        } else {
            System.out.println("✗ No working Gemini API keys found");
            System.out.println("  All keys may be blocked or quota exceeded");
        }
    }
    
    private static String testModel(String apiKey, String model) throws Exception {
        String urlString = API_BASE + model + ":generateContent?key=" + apiKey;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            
            // Build simple test request
            JsonObject requestBody = new JsonObject();
            JsonArray contents = new JsonArray();
            JsonObject content = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", "Say 'test successful' in 3 words");
            parts.add(part);
            content.add("parts", parts);
            contents.add(content);
            requestBody.add("contents", contents);
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Read response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    
                    // Parse response
                    JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
                    
                    if (jsonResponse.has("candidates")) {
                        JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
                        if (candidates.size() > 0) {
                            JsonObject candidate = candidates.get(0).getAsJsonObject();
                            JsonObject contentObj = candidate.getAsJsonObject("content");
                            JsonArray partsArray = contentObj.getAsJsonArray("parts");
                            if (partsArray.size() > 0) {
                                return partsArray.get(0).getAsJsonObject().get("text").getAsString();
                            }
                        }
                    }
                    return null;
                }
            } else {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    throw new Exception("HTTP " + responseCode + ": " + errorResponse.toString());
                }
            }
        } finally {
            conn.disconnect();
        }
    }
}