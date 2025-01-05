package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Conversation;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConversationService {
    private final ConversationsRepository conversationsRepository;

    public ConversationService(ConversationsRepository conversationsRepository) {
        this.conversationsRepository = conversationsRepository;
    }

    public ResponseEntity<List<Conversation>> getConversationList (HttpServletRequest request) {
        try {
            final UUID requesterId = UUID.fromString((String) request.getSession().getAttribute("uuid"));
            final List<Conversation> conversations = conversationsRepository.findByIdAndRequestFalse(requesterId);

            return new ResponseEntity<>(conversations, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching conversations list");
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
