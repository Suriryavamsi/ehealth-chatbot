package org.ehealth.controller;

import org.ehealth.model.User;
import org.ehealth.repository.UserRepository;
import org.ehealth.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;

    public ChatController(ChatService chatService, UserRepository userRepository){
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestParam Long conversationId,
                                         @RequestBody String message,
                                         Authentication authentication){
        if(authentication==null) return ResponseEntity.status(401).body("Unauthorized");

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String reply = chatService.handleUserMessage(conversationId, message, user);

        return ResponseEntity.ok(Map.of(
                "userMessage", message,
                "botReply", reply,
                "timestamp", LocalDateTime.now()
        ));
    }
}
