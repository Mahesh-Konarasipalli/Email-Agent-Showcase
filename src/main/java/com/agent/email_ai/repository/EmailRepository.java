package com.agent.email_ai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agent.email_ai.entity.EmailSummary;

public interface EmailRepository extends JpaRepository<EmailSummary, Long> {
    
}
