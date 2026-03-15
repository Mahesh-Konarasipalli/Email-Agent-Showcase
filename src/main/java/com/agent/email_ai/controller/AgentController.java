package com.agent.email_ai.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agent.email_ai.entity.EmailDetails;
import com.agent.email_ai.entity.EmailSummary;
import com.agent.email_ai.repository.EmailRepository;
import com.agent.email_ai.service.AiAgentService;
import com.agent.email_ai.service.EmailReaderService;
import com.agent.email_ai.service.EmailSenderService;
import com.agent.email_ai.service.TelegramService;

@Controller
public class AgentController {
    
    private final EmailReaderService emailReaderService;
    private final AiAgentService aiAgentService;
    private final EmailRepository repository;
    private final EmailSenderService emailSenderService;
    private final TelegramService telegramService;

    // --- NEW: Dynamic URL for Render ---
    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    @Autowired
    public AgentController(EmailReaderService emailReaderService,
                           AiAgentService aiAgentService,
                           EmailRepository repository,
                           EmailSenderService emailSenderService,
                           TelegramService telegramService) {
        this.emailReaderService = emailReaderService;
        this.aiAgentService = aiAgentService;
        this.repository = repository;
        this.emailSenderService = emailSenderService;
        this.telegramService = telegramService; 
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard"; // Redirects root to your actual dashboard
    }
    
    @GetMapping("/ping")
    @ResponseBody
    public String keepAlive() {
        System.out.println("💓 Heartbeat detected: Pinger kept the server awake at " + java.time.LocalDateTime.now());
        return "PONG - Server is Awake";
    }

    @GetMapping("/status")
    @ResponseBody 
    public String checkStatus(){
        return "✅ AI Email Agent is up and scanning every 10 minutes.";
    }

    @GetMapping("/check-inbox")
    @ResponseBody
    public String checkInbox() {
        return emailReaderService.testConnection();
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<EmailSummary> summaries = repository.findAll();
        model.addAttribute("summaries", summaries);
        return "list-emails"; 
    }

    @GetMapping("/analyze-latest")
    public String analyzeLatest(RedirectAttributes redirectAttributes) {
        executeAutonomousWorkflow(); 
        redirectAttributes.addFlashAttribute("message", "Manual analysis complete!");
        redirectAttributes.addFlashAttribute("alertType", "success");
        return "redirect:/dashboard"; 
    }

    @PostMapping("/send-reply/{id}")
    public String sendAiReply(@PathVariable("id") Long id, 
                              @RequestParam("editedDraft") String editedDraft, 
                              RedirectAttributes redirectAttributes) {
        try {
            EmailSummary summary = repository.findById(id)
                .orElseThrow(() -> new Exception("Summary not found"));
            
            emailSenderService.sendReply(summary.getSender(), summary.getSubject(), editedDraft);
            
            summary.setSuggestedReply(editedDraft);
            summary.setReplied(true); 
            repository.save(summary);
            
            redirectAttributes.addFlashAttribute("message", "Success! Reply sent to " + summary.getSender());
            redirectAttributes.addFlashAttribute("alertType", "success");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error sending email: " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }
        
        return "redirect:/dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteSummary(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            repository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Success! The summary has been deleted.");
            redirectAttributes.addFlashAttribute("alertType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error: Could not delete.");
            redirectAttributes.addFlashAttribute("alertType", "danger");
        }
        return "redirect:/dashboard";
    }

   @Scheduled(cron = "0 */10 * * * *") 
    public void executeAutonomousWorkflow() {
        System.out.println("🤖 Waking up to check inbox...");
        List<EmailDetails> emails = emailReaderService.getUnreadEmails();
        
        if (emails.isEmpty()) {
            System.out.println("😴 No new emails. Going back to sleep.");
            return;
        }

        for (EmailDetails email : emails) {
            try {
                // 🚀 STEP 1: SMART FILTERING (Save tokens by skipping junk)
                String sender = email.getSender().toLowerCase();
                String subject = email.getSubject().toLowerCase();

                if (sender.contains("no-reply") || sender.contains("newsletter") || 
                    subject.contains("unsubscribe") || sender.contains("promotion")) {
                    
                    System.out.println("⏩ Skipping automated email from: " + sender);
                    // Optional: Move junk to bin automatically without AI
                    // emailReaderService.moveMessageToBin(email.getMessageId());
                    continue; 
                }

                // 🚀 STEP 2: CALL AI
                String rawResponse = aiAgentService.analyzeAndDraft(email.getBody());
                
                // --- BUG FIXED: Safely extracting both Analysis AND Draft ---
                String[] parts = rawResponse.split("DRAFT:");
                String analysis = parts[0].trim().toUpperCase();
                String draft = (parts.length > 1) ? parts[1].trim() : "No draft generated by AI.";

                // 1. DATABASE SAVE 
                EmailSummary summary = new EmailSummary();
                summary.setSender(email.getSender());
                summary.setSubject(email.getSubject());
                summary.setAiAnalysis(analysis);
                summary.setSuggestedReply(draft); 
                summary.setProcessedAt(LocalDateTime.now());
                repository.save(summary);

                // 2. DECISION LOGIC
                if (analysis.contains("PRIORITY: HIGH") || analysis.contains("PRIORITY: MEDIUM")) {
                    String analyzeUrl = appBaseUrl + "/dashboard"; 
                    telegramService.sendHighPriorityAlert(email.getSender(), email.getSubject(), analyzeUrl);
                    System.out.println("✅ Important email kept in Inbox.");
                } else {
                    emailReaderService.moveMessageToBin(email.getMessageId());
                    System.out.println("🗑️ Low Priority moved to Bin.");
                }

                // 🚀 STEP 3: COOLDOWN (Prevents Per-Minute Rate Limits)
                Thread.sleep(15000); 

            } catch (Exception e) {
                // 🚀 STEP 4: RATE LIMIT DETECTION
                if (e.getMessage().contains("429")) {
                    System.err.println("🛑 STOPPING BATCH: Groq daily limit reached. Try again later.");
                    break; // Stop the loop for this batch to avoid spamming errors
                }
                System.err.println("⚠️ Error processing email: " + e.getMessage());
            }
        }
        System.out.println("✅ Finished processing batch.");
    }
}