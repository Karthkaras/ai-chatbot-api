package com.karthik.ai_chatbot_api.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class ChatResponse {
    private String reply;
    private String sessionId;
    private String model;
    private Instant timestamp;
    private int inputTokens;
    private int outputTokens;
}
