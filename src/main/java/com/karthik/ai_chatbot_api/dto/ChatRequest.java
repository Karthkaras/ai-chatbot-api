package com.karthik.ai_chatbot_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor   // needed when @Builder is present
@AllArgsConstructor  // needed when @Builder is present
public class ChatRequest {

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 4000, message = "Message too long")
    private String message;

    private String sessionId;   // for multi-turn chat
    private String systemPrompt; // optional custom persona
}
