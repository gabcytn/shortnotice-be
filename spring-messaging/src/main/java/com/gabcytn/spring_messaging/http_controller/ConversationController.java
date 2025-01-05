package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.Conversation;
import com.gabcytn.spring_messaging.service.ConversationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Conversation>> getConversationsList (HttpServletRequest request) {
        return conversationService.getConversationList(request);
    }
}
