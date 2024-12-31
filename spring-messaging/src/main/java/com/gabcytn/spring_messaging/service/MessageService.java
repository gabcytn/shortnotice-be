package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationsRepository conversationsRepository;

    public MessageService(
            ConversationsRepository conversationsRepository
    ) {
        this.conversationsRepository = conversationsRepository;
    }

    public Optional<PrivateMessage> createMessageRequest (
            SimpMessageHeaderAccessor headerAccessor,
            Message messageReceived,
            UUID recipientUUID
    ) {
        try {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();
            final Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

            final int conversationId = conversationsRepository.create(true);
            conversationsRepository.saveMembers(conversationId, senderUUID, recipientUUID);

            return Optional.of(new PrivateMessage(senderUsername, content, currentTimestamp));
        } catch (Exception e) {
            System.err.println("Error creating message request");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    private UUID getMessageSenderUUID(SimpMessageHeaderAccessor headerAccessor) {
        return UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
    }

    private String getMessageSenderUsername(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }
}
