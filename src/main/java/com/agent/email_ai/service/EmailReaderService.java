package com.agent.email_ai.service;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.MessageIDTerm;
import jakarta.mail.search.SearchTerm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agent.email_ai.entity.EmailDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReaderService {

    @Value("${agent.email.imap.host:imap.gmail.com}")
    private String imapHost;

    @Value("${agent.email.imap.port:993}")
    private String imapPort;

    @Value("${agent.email.address}")
    private String emailAddress;

    @Value("${agent.email.password}")
    private String emailPassword;

    // --- HELPER METHOD: Reusable Connection Logic ---
    private Store getConnectedStore() throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", imapHost);
        props.put("mail.imaps.port", imapPort);
        
        Session session = Session.getInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect(imapHost, emailAddress, emailPassword);
        
        return store;
    }

    public String testConnection() {
        try {
            Store store = getConnectedStore();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int totalMessages = inbox.getMessageCount();
            
            inbox.close(false);
            store.close();

            return "Success! Securely connected to inbox. Total emails found: " + totalMessages;
        } catch (Exception e) {
            return "Connection Failed. Reason: " + e.getMessage();
        }
    }

    public List<EmailDetails> getUnreadEmails() {
        List<EmailDetails> emailList = new ArrayList<>();
        try {
            Store store = getConnectedStore();
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE); 

            Message[] unreadMessages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            int limit = Math.min(unreadMessages.length, 50);
            
            for (int i = 0; i < limit; i++) {
                try {
                    Message message = unreadMessages[i];
    
                    EmailDetails details = new EmailDetails();
                    details.setSender(message.getFrom()[0].toString());
                    details.setSubject(message.getSubject());
                    details.setMessageId(((jakarta.mail.internet.MimeMessage) message).getMessageID());
                    
                    // --- BUG FIXED: Actually extract the body text for the AI! ---
                    details.setBody(getTextFromMessage(message)); 
                    
                    emailList.add(details);

                    // Mark as seen so we don't read it again next time
                    message.setFlag(Flags.Flag.SEEN, true);

                } catch (Exception e) {
                    System.err.println("Error processing single email: " + e.getMessage());
                }
            }

            inbox.close(true); 
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return emailList;
    }

    public void moveMessageToBin(String messageId) {
        if (messageId == null) {
            System.err.println("⚠️ Cannot delete email: messageId is missing from the original email.");
            return;
        }
        
        try {
            Store store = getConnectedStore(); 
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            SearchTerm searchTerm = new MessageIDTerm(messageId);
            Message[] foundMessages = inbox.search(searchTerm);

            if (foundMessages.length > 0) {
                Message messageToDelete = foundMessages[0];

                // --- THE GMAIL SPECIFIC FIX ---
                // 1. Find the exact Trash folder (Gmail names it differently by region)
                Folder trash = store.getFolder("[Gmail]/Trash");
                if (!trash.exists()) {
                    trash = store.getFolder("[Gmail]/Bin"); 
                }

                // 2. Copy the message to the Trash/Bin folder
                if (trash.exists()) {
                    inbox.copyMessages(new Message[]{messageToDelete}, trash);
                } else {
                    System.out.println("⚠️ Warning: Could not find Gmail Trash folder.");
                }

                // 3. Mark the original as deleted in the Inbox
                messageToDelete.setFlag(Flags.Flag.DELETED, true);
                System.out.println("🗑️ Low Priority email successfully moved to Gmail Bin!");
                
            } else {
                System.out.println("⚠️ Could not find email to delete. ID might be invalid: " + messageId);
            }

            // 4. Close and "expunge" (This formally executes the deletion from the Inbox)
            inbox.close(true); 
            store.close();
            
        } catch (Exception e) {
            System.err.println("⚠️ Could not move message to bin: " + e.getMessage());
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();
                return getTextFromMultipart(multipart);
            } else {
                return new String(message.getInputStream().readAllBytes())
                        .replaceAll("<[^>]*>", " ")
                        .replaceAll("\\s+", " ")
                        .trim();
            }
        } catch (Exception e) {
            return "Email content could not be extracted: " + e.getMessage();
        }
    }

    private String getTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("text/html")) {
                String html = bodyPart.getContent().toString();
                String clean = html.replaceAll("(?s)<style.*?>.*?</style>", "") 
                                .replaceAll("(?s)<script.*?>.*?</script>", "") 
                                .replaceAll("<[^>]*>", " ") 
                                .replaceAll("&nbsp;", " ") 
                                .replaceAll("&amp;", "&")
                                .replaceAll("\\r|\\n|\\t", " ") 
                                .replaceAll("\\s{2,}", " ") 
                                .trim();
                result.append(clean);
            } else if (bodyPart.getContent() instanceof Multipart) {
                result.append(getTextFromMultipart((Multipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}