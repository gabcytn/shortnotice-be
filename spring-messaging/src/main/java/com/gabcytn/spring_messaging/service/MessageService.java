package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import com.gabcytn.spring_messaging.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationsRepository conversationsRepository;
    private final MessageRepository messageRepository;

    public MessageService(
            ConversationsRepository conversationsRepository,
            MessageRepository messageRepository
    ) {
        this.conversationsRepository = conversationsRepository;
        this.messageRepository = messageRepository;
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

            if (isMessageRequestExisting(senderUUID, recipientUUID)) return Optional.empty();

            final int conversationId = conversationsRepository.create();
            conversationsRepository.saveMembers(conversationId, senderUUID, recipientUUID);
            messageRepository.save(conversationId, senderUUID, content);

            return Optional.of(new PrivateMessage(senderUsername, content, currentTimestamp));
        } catch (Exception e) {
            System.err.println("Error creating message request");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<PrivateMessage> acceptMessageRequest (
            SimpMessageHeaderAccessor headerAccessor,
            Message messageReceived,
            int conversationId
    ) {
        try {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();
            final Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

            conversationsRepository.setRequestFalseById(conversationId);
            messageRepository.save(conversationId, senderUUID, content);

            return Optional.of(new PrivateMessage(senderUsername, content, currentTimestamp));
        } catch (Exception e) {
            System.err.println("Error accepting message request");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    private Boolean isMessageRequestExisting (UUID sender, UUID recipient) {
        return conversationsRepository.isConversationExisting(sender, recipient);
    }

    private UUID getMessageSenderUUID(SimpMessageHeaderAccessor headerAccessor) {
        return UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
    }

    private String getMessageSenderUsername(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }
}
