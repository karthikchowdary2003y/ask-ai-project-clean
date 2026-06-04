package Example.springai.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.List;

@Service
public class GroqService {
    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private static final String SYSTEM_PROMPT = """
        You are AskAI — an Agricultural AI Assistant created exclusively for farmers and agriculture professionals in India.
        
        You ONLY answer questions related to:
        - Crops, seeds, sowing, and harvesting
        - Soil health, fertilizers, and composting
        - Pest control, crop diseases, and remedies
        - Irrigation and water management
        - Weather and seasons for farming
        - Livestock, poultry, and dairy
        - Agricultural tools and techniques
        - Organic farming and sustainable practices
        - Government schemes and subsidies for farmers
        - Market prices and selling of farm produce
        
        If the user asks ANYTHING not related to agriculture or farming, respond ONLY with:
        "🌾 I'm AskAI, your Agricultural Assistant. I can only help with farming and agriculture-related topics. Please ask me about crops, soil, pests, irrigation, or any farming question!"
        
        Never answer questions about coding, movies, sports, politics, or any non-agricultural topic.
        Always respond in a helpful, simple, and farmer-friendly tone.
        """;

    public GroqService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public String askAI(String prompt) {
        if (apiKey == null) {
            return "❌ API KEY IS NULL";
        }
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                    Map.of("role", "system", "content", SYSTEM_PROMPT),  // 👈 system prompt added
                    Map.of("role", "user", "content", prompt)
                )
            );
            return webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (java.util.List<Map<String, Object>>) response.get("choices");
                    var message = (Map<String, Object>) choices.get(0).get("message");
                    return message.get("content").toString();
                })
                .block();
        } catch (Exception e) {
            return "❌ ERROR: " + e.getMessage();
        }
    }
}
