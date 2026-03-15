package com.agent.email_ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils; 

@Service
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

// ... inside your TelegramService class:

public void sendHighPriorityAlert(String sender, String subject, String analyzeUrl) {
    try {
        String safeSender = HtmlUtils.htmlEscape(sender != null ? sender : "Unknown");
        String safeSubject = HtmlUtils.htmlEscape(subject != null ? subject : "No Subject");

        // 🚀 FIX: Removed the HTML <a> tags. Just passing the raw URL so Telegram auto-links it!
        String message = "🚨 <b>AI ALERT: High Priority Email!</b>\n\n" +
                         "<b>From:</b> " + safeSender + "\n" +
                         "<b>Subject:</b> " + safeSubject + "\n\n" +
                         "<b>Reply Here:</b> " + analyzeUrl;

        String url = "https://api.telegram.org/bot{botToken}/sendMessage?chat_id={chatId}&text={text}&parse_mode=HTML";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(url, String.class, botToken, chatId, message);
        
        System.out.println("✅ Telegram alert sent successfully!");
        
    } catch (Exception e) {
        System.err.println("❌ Failed to send Telegram alert: " + e.getMessage());
    }
}
}
