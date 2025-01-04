package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.model.SocketResponse;
import com.gabcytn.spring_messaging.repository.BlocksRepository;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import com.gabcytn.spring_messaging.repository.MessageRepository;
import com.gabcytn.spring_messaging.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationsRepository conversationsRepository;
    private final MessageRepository messageRepository;
    private final BlocksRepository blocksRepository;
    private final UserRepository userRepository;

    public MessageService(
            ConversationsRepository conversationsRepository,
            MessageRepository messageRepository,
            BlocksRepository blocksRepository,
            UserRepository userRepository) {
        this.conversationsRepository = conversationsRepository;
        this.messageRepository = messageRepository;
        this.blocksRepository = blocksRepository;
        this.userRepository = userRepository;
    }

    public SocketResponse<PrivateMessage> createMessageRequest (SimpMessageHeaderAccessor headerAccessor, Message messageReceived, UUID recipientUUID)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();

            if (blocksRepository.existsByBlockerIdAndBlockedId(recipientUUID, senderUUID))
                return new SocketResponse<>("ERROR","User is blocked", new PrivateMessage());

            final Integer conversationId = conversationsRepository.existsByMembers(recipientUUID, senderUUID);
            if (conversationId != null && conversationId > 0)
                return handleExistingMessageRequest(conversationId, senderUUID, senderUsername, content);

            return handleNewMessageRequest(senderUUID, senderUsername, recipientUUID, content);
        }
        catch (Exception e)
        {
            System.err.println("Error creating message request");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), new PrivateMessage());
        }
    }

    private SocketResponse<PrivateMessage> handleNewMessageRequest(UUID senderUUID, String senderUsername, UUID recipientUUID, String message) {
        final int newConversationId = conversationsRepository.create();
        conversationsRepository.saveMembers(newConversationId, senderUUID, recipientUUID);
        messageRepository.save(newConversationId, senderUUID, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final PrivateMessage privateMessage = new PrivateMessage(senderUsername, message, newConversationId, true, timestamp);

        return new SocketResponse<>("OK", "New message request handled successfully", privateMessage);
    }

    private SocketResponse<PrivateMessage> handleExistingMessageRequest (int conversationId, UUID senderId, String senderUsername, String message){
        messageRepository.save(conversationId, senderId, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final PrivateMessage privateMessage = new PrivateMessage(senderUsername, message, conversationId, true, timestamp);

        return new SocketResponse<>("OK", "Existing message request handled successfully", privateMessage);
    }

    private UUID getMessageSenderUUID(SimpMessageHeaderAccessor headerAccessor) {
        return UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
    }

    private String getMessageSenderUsername(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }
}
