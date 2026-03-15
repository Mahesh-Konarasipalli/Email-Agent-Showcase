package com.agent.email_ai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${agent.email.address}")
    private String fromEmail;

    // Use Constructor Injection instead of @Autowired on the field
    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReply(String toAddress, String originalSubject, String bodyText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        
        // Safely extract the email if it's in the "Name <email@domain.com>" format
        String cleanTo = toAddress;
        if (toAddress != null && toAddress.contains("<") && toAddress.contains(">")) {
            cleanTo = toAddress.substring(toAddress.indexOf("<") + 1, toAddress.indexOf(">")).trim();
        }
        
        message.setTo(cleanTo);
        
        // Prevent "Re: Re:" in the subject line
        String replySubject = originalSubject;
        if (originalSubject != null && !originalSubject.toLowerCase().startsWith("re:")) {
            replySubject = "Re: " + originalSubject;
        }
        message.setSubject(replySubject);
        
        message.setText(bodyText);
        
        try {
            mailSender.send(message);
            System.out.println("✅ Reply successfully sent to: " + cleanTo);
        } catch (Exception e) {
            System.err.println("❌ Failed to send reply to " + cleanTo + ". Error: " + e.getMessage());
            throw e; // Rethrow so the Controller catches it and shows an error message
        }
    }
}