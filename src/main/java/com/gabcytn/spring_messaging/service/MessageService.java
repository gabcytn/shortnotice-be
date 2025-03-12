package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.IncomingMessage;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.model.SocketResponse;
import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.repository.BlocksRepository;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import com.gabcytn.spring_messaging.repository.MessageRepository;
import com.gabcytn.spring_messaging.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationsRepository conversationsRepository;
    private final MessageRepository messageRepository;
    private final BlocksRepository blocksRepository;
    private final UserRepository userRepository;
    private final ValidatorService validatorService;

    public MessageService(
            ConversationsRepository conversationsRepository,
            MessageRepository messageRepository,
            BlocksRepository blocksRepository,
            UserRepository userRepository,
            ValidatorService validatorService) {
        this.conversationsRepository = conversationsRepository;
        this.messageRepository = messageRepository;
        this.blocksRepository = blocksRepository;
        this.userRepository = userRepository;
        this.validatorService = validatorService;
    }

    public SocketResponse<PrivateMessage> createMessageRequest (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived, UUID recipientUUID)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();

            if (blocksRepository.existsByBlockerIdAndBlockedId(recipientUUID, senderUUID))
                throw new Error("User is blocked");

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

    public SocketResponse<PrivateMessage> acceptMessageRequest (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived, int conversationId)
    {
        try
        {
            final Optional<User> recipient = userRepository.findByUsername(messageReceived.recipient());
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

            if (recipient.isEmpty())
                throw new Error("Recipient not found");
            if (blocksRepository.existsByBlockerIdAndBlockedId(recipient.get().getId(), senderUUID))
                throw new Error("User is blocked");

            conversationsRepository.setRequestFalseById(conversationId);
            Integer messageId = messageRepository.save(conversationId, senderUUID, messageReceived.content());
            final PrivateMessage privateMessage = new PrivateMessage(conversationId, senderUsername, messageReceived.content(), messageId, false, timestamp);
            return new SocketResponse<>("OK", "Accepting message request handled successfully", privateMessage);
        }
        catch (Exception e)
        {
            System.err.println("Error accepting message request");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), new PrivateMessage());
        }
    }

    public SocketResponse<PrivateMessage> sendNormalMessage (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived, int conversationId)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            // final Optional<User> recipient = userRepository.findByUsername(messageReceived.recipient());
            final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            UUID recipientId = conversationsRepository.findByIdAndNotMemberId(messageReceived.conversationId(), senderUUID);

            validatorService.validateNormalMessage(senderUUID, recipientId, messageReceived);

            Integer messageId = messageRepository.save(conversationId, senderUUID, messageReceived.content());
            final PrivateMessage privateMessage = new PrivateMessage(conversationId, senderUsername, messageReceived.content(), messageId, false, timestamp);
            privateMessage.setRecipientId(recipientId);

            return new SocketResponse<>("OK", "Normal message handled successfully", privateMessage);
        }
        catch (Exception e)
        {
            System.err.println("Error sending normal message");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), null);
        }
    }

    public ResponseEntity<List<PrivateMessage>> getMessageHistory (int conversationId) {
        try {
            final List<PrivateMessage> privateMessages = messageRepository.findAllByConversationId(conversationId);
            return new ResponseEntity<>(privateMessages, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching past messages");
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SocketResponse<PrivateMessage> handleNewMessageRequest(UUID senderUUID, String senderUsername, UUID recipientUUID, String message)
    {
        final int newConversationId = conversationsRepository.create();
        conversationsRepository.saveMembers(newConversationId, senderUUID, recipientUUID);
        Integer messageId = messageRepository.save(newConversationId, senderUUID, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final PrivateMessage privateMessage = new PrivateMessage(newConversationId, senderUsername, message, messageId, true, timestamp);

        return new SocketResponse<>("OK", "New message request handled successfully", privateMessage);
    }

    private SocketResponse<PrivateMessage> handleExistingMessageRequest (int conversationId, UUID senderId, String senderUsername, String message)
    {
        Integer messageId = messageRepository.save(conversationId, senderId, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final PrivateMessage privateMessage = new PrivateMessage(conversationId, senderUsername, message, messageId, true, timestamp);

        return new SocketResponse<>("OK", "Existing message request handled successfully", privateMessage);
    }

    private UUID getMessageSenderUUID(SimpMessageHeaderAccessor headerAccessor) {
        return UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
    }

    private String getMessageSenderUsername(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }
}
