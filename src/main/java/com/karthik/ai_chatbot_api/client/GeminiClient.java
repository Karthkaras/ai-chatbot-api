package com.karthik.ai_chatbot_api.client;



import com.karthik.ai_chatbot_api.config.GeminiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {


    private final WebClient webClient;
    private final GeminiConfig config;

    public String generateContent(String userMessage, String systemPrompt) {
        String url = config.getBaseUrl()
                + "/v1beta/models/" + config.getModel()
                + ":generateContent?key=" + config.getApiKey();

        Map<String, Object> requestBody = buildRequest(userMessage, systemPrompt);

        log.info("Calling Gemini API: {}", config.getModel());

        Map response = webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return extractText(response);
    }

    private Map<String, Object> buildRequest(String message, String systemPrompt) {
        List<Map<String, Object>> contents = new java.util.ArrayList<>();

        if (systemPrompt != null && !systemPrompt.isBlank()) {
            contents.add(Map.of(
                    "role", "user",
                    "parts", List.of(Map.of("text", systemPrompt))
            ));
            contents.add(Map.of(
                    "role", "model",
                    "parts", List.of(Map.of("text", "Understood."))
            ));
        }

        contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", message))
        ));

        return Map.of(
                "contents", contents,
                "generationConfig", Map.of(
                        "temperature", 0.7,
                        "maxOutputTokens", 2048,
                        "topP", 0.9
                )
        );
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map response) {
        try {
            var candidates = (List<Map>) response.get("candidates");
            var content = (Map<String, Object>) candidates.get(0).get("content");
            var parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            log.error("Failed to parse Gemini response", e);
            throw new RuntimeException("Could not parse AI response");
        }
    }
}
