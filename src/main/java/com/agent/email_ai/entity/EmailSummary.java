package com.agent.email_ai.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_email_summaries")
@Data
public class EmailSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String subject;

    // Use @Lob (Large Object) or a long column definition for AI text
    @Column(columnDefinition = "TEXT") 
    private String aiAnalysis;
    @Column(columnDefinition = "TEXT")
    private String suggestedReply;

    private boolean replied = false;

    private LocalDateTime processedAt;
}