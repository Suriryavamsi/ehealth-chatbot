package org.ehealth.service;

import org.ehealth.model.Conversation;
import org.ehealth.model.Message;
import org.ehealth.model.User;
import org.ehealth.repository.ConversationRepository;
import org.ehealth.repository.MessageRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ConversationRepository conversationRepo;
    private final MessageRepository messageRepo;
    private final AIService aiService;

    public ChatService(ConversationRepository conversationRepo,
                       MessageRepository messageRepo,
                       AIService aiService) {
        this.conversationRepo = conversationRepo;
        this.messageRepo = messageRepo;
        this.aiService = aiService;
    }

    public String handleUserMessage(Long conversationId, String userMessage, User user) {
        // Create or fetch conversation
        Conversation convo = conversationRepo.findById(conversationId).orElseGet(() -> {
            Conversation c = new Conversation();
            c.setUserId(user.getId());
            c.setType("Chatbot");
            return conversationRepo.save(c);
        });

        // Save user message
        Message userMsg = new Message();
        userMsg.setConversation(convo);
        userMsg.setSender(user.getRole().getName());
        userMsg.setContent(userMessage);
        messageRepo.save(userMsg);

        // Pass role-specific context to AI
        String contextPrompt = buildRoleContext(user);
        String reply = aiService.getReply(contextPrompt + "\nUser: " + userMessage);

        // Save bot message
        Message botMsg = new Message();
        botMsg.setConversation(convo);
        botMsg.setSender("bot");
        botMsg.setContent(reply);
        messageRepo.save(botMsg);

        return reply;
    }

    private String buildRoleContext(User user) {
        return switch (user.getRole().getName()) {
            case "DOCTOR" -> "You are a doctor assistant AI. Summarize patient lab results, appointments, and provide medical suggestions when asked.";
            case "PATIENT" -> "You are a friendly healthcare assistant. Explain lab reports, appointments, and medical advice in simple terms for the patient.";
            case "NURSE" -> "You are a nurse assistant AI. Provide patient info, doctor appointments, and guidance for medical tasks.";
            default -> "You are a general health assistant.";
        };
    }
}
