package com.agent.email_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiAgentService {

    private final ChatClient chatClient;
    private static final int MAX_LENGTH = 3500; // Roughly 3000 tokens

    public AiAgentService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // --- NEW: A helper method to easily truncate any string ---
    private String getSafeContent(String originalContent) {
        if (originalContent != null && originalContent.length() > MAX_LENGTH) {
            System.out.println("✂️ Huge email detected! Truncating down to safe limits.");
            return originalContent.substring(0, MAX_LENGTH) + "\n\n...[EMAIL TRUNCATED DUE TO LENGTH]...";
        }
        return originalContent != null ? originalContent : "No content provided.";
    }

    public String analyzeEmailText(String emailContent) {
        String safeBody = getSafeContent(emailContent);
        
        return chatClient.prompt()
                .user("Extract info from this email. Be concise. \n" +
                    "Use this exact format:\n" +
                    "• CATEGORY: \n" +
                    "• SENDER INTENT: \n" +
                    "• KEY TOPICS: \n" +
                    "• ACTION REQUIRED: [Yes/No]\n\n" +
                    "CONTENT: " + safeBody) // <-- Fixed: Using safeBody
                .call()
                .content();
    }

    public String generateReplyDraft(String emailContent) {
        String safeBody = getSafeContent(emailContent);
        
        return chatClient.prompt()
                .user("Based on the following email content, draft a professional, polite, and concise reply. " +
                    "If it's a marketing email, draft a 'not interested' reply. " +
                    "If it's educational, draft a 'thank you' reply. \n\n" +
                    "CONTENT: " + safeBody) // <-- Fixed: Using safeBody
                .call()
                .content();
    }

    public String analyzeAndDraft(String emailContent) {
        String safeBody = getSafeContent(emailContent); // <-- Fixed: Added truncation here!
        
        return chatClient.prompt()
                .user("Perform two tasks on the email content provided below.\n" +
                    "1. Analysis: Provide a bulleted summary, category, and priority (HIGH, MEDIUM, LOW).\n" +
                    "2. Draft: Provide a polite, professional reply draft.\n\n" +
                    "Format your response exactly like this:\n" +
                    "ANALYSIS: [Your summary here]\n" +
                    "DRAFT: [Your reply here]\n\n" +
                    "EMAIL CONTENT: " + safeBody) // <-- Fixed: Using safeBody
                .call()
                .content();
    }
}