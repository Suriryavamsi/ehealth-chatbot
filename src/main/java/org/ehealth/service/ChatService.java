package org.ehealth.service;

import org.ehealth.model.Conversation;
import org.ehealth.model.Message;
import org.ehealth.repository.ConversationRepository;
import org.ehealth.repository.MessageRepository;
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

    public String handleUserMessage(Long conversationId, String userMessage) {
        // Find or create conversation
        Conversation convo = conversationRepo.findById(conversationId).orElseGet(() -> {
            Conversation c = new Conversation();
            c.setUserId(null); // optional: set userId if you have it
            c.setType("Chatbot");
            return conversationRepo.save(c);
        });

        // Save user message
        Message userMsg = new Message();
        userMsg.setConversation(convo);
        userMsg.setSender("user");
        userMsg.setContent(userMessage);
        messageRepo.save(userMsg);

        // Get AI reply
        String reply = aiService.getReply(userMessage);

        // Save bot message
        Message botMsg = new Message();
        botMsg.setConversation(convo);
        botMsg.setSender("bot");
        botMsg.setContent(reply);
        messageRepo.save(botMsg);

        return reply;
    }
}
