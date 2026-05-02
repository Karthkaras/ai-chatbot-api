package com.karthik.ai_chatbot_api.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "chat_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String sessionId;

    @Column(columnDefinition = "TEXT")
    private String userMessage;

    @Column(columnDefinition = "TEXT")
    private String aiReply;

    private String model;
    private Instant createdAt = Instant.now();
}
