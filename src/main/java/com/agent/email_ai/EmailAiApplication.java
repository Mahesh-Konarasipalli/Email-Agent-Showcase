package com.agent.email_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // 1. Import this

@SpringBootApplication
@EnableScheduling // 2. Add this annotation!
public class EmailAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailAiApplication.class, args);
    }
}
