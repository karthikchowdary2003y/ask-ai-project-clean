package Example.springai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GroqService {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    public GroqService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public String askAI(String prompt) {

    	Map<String, Object> requestBody = Map.of(
    	        "model","llama-3.1-8b-instant",
    	        "messages", java.util.List.of(
    	                Map.of("role", "user", "content", prompt)
    	        )
    	);

    	return webClient.post()
    	        .uri(apiUrl)
    	        .header("Authorization", "Bearer " + apiKey)
    	        .header("Content-Type", "application/json")
    	        .bodyValue(requestBody)
    	        .retrieve()
    	        .onStatus(status -> status.isError(), response ->
    	                response.bodyToMono(String.class)
    	                        .map(error -> new RuntimeException("Groq Error: " + error))
    	        )
    	        .bodyToMono(Map.class)
    	        .map(response -> {
    	            var choices = (java.util.List<Map<String, Object>>) response.get("choices");
    	            var message = (Map<String, Object>) choices.get(0).get("message");
    	            return message.get("content").toString();
    	        })
    	        .block();
    }
}