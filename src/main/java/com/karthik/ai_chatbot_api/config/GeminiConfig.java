package com.karthik.ai_chatbot_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gemini")
public class GeminiConfig {
    private String apiKey;
    private String model = "gemini-1.5-flash";
    private String baseUrl = "https://generativelanguage.googleapis.com";
}
