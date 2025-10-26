package org.ehealth.controller;

import org.ehealth.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(
            @RequestParam("conversationId") Long conversationId,
            @RequestBody String message) {

        String reply = chatService.handleUserMessage(conversationId, message);
        return ResponseEntity.ok(reply);
    }
}
