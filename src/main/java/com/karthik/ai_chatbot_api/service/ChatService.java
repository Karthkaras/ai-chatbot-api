package com.karthik.ai_chatbot_api.service;

import com.karthik.ai_chatbot_api.client.GeminiClient;
import com.karthik.ai_chatbot_api.config.GeminiConfig;
import com.karthik.ai_chatbot_api.dto.ChatRequest;
import com.karthik.ai_chatbot_api.dto.ChatResponse;
import com.karthik.ai_chatbot_api.entity.ChatHistory;
import com.karthik.ai_chatbot_api.entity.User;
import com.karthik.ai_chatbot_api.repository.ChatHistoryRepository;
import com.karthik.ai_chatbot_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GeminiClient geminiClient;
    private final GeminiConfig config;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    public ChatResponse chat(ChatRequest request, String username) {
        String sessionId = request.getSessionId() != null
                ? request.getSessionId()
                : UUID.randomUUID().toString();

        String systemPrompt = request.getSystemPrompt() != null
                ? request.getSystemPrompt()
                : "You are a helpful AI assistant.";

        String reply = geminiClient.generateContent(
                request.getMessage(), systemPrompt);

        // Save to DB
        User user = userRepository.findByUsername(username).orElse(null);
        chatHistoryRepository.save(ChatHistory.builder()
                .user(user)
                .sessionId(sessionId)
                .userMessage(request.getMessage())
                .aiReply(reply)
                .model(config.getModel())
                .build());

        return ChatResponse.builder()
                .reply(reply)
                .sessionId(sessionId)
                .model(config.getModel())
                .timestamp(Instant.now())
                .build();
    }
}