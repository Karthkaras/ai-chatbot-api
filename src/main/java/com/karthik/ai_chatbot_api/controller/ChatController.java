package com.karthik.ai_chatbot_api.controller;

import com.karthik.ai_chatbot_api.dto.ChatRequest;
import com.karthik.ai_chatbot_api.dto.ChatResponse;
import com.karthik.ai_chatbot_api.entity.ChatHistory;
import com.karthik.ai_chatbot_api.entity.User;
import com.karthik.ai_chatbot_api.repository.ChatHistoryRepository;
import com.karthik.ai_chatbot_api.repository.UserRepository;
import com.karthik.ai_chatbot_api.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(chatService.chat(request, username));
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatHistory>> history(
            @PathVariable String sessionId) {
        return ResponseEntity.ok(
                chatHistoryRepository
                        .findBySessionIdOrderByCreatedAtAsc(sessionId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatHistory>> myHistory(
            Authentication authentication) {
        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow();
        return ResponseEntity.ok(
                chatHistoryRepository
                        .findByUserIdOrderByCreatedAtDesc(user.getId()));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("AI Chatbot API is running!");
    }
}