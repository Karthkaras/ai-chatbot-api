package com.karthik.ai_chatbot_api.repository;


import com.karthik.ai_chatbot_api.entity.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatHistoryRepository
        extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findBySessionIdOrderByCreatedAtAsc(String sessionId);
    List<ChatHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}