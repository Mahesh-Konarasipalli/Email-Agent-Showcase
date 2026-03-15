package com.agent.email_ai.entity;

import lombok.Data;
// It is not a real entity.
@Data
public class EmailDetails {
    private String sender;
    private String subject;
    private String body;
    private String messageId;
}
