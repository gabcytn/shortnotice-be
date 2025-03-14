package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.IncomingMessage;
import com.gabcytn.spring_messaging.model.OutgoingMessage;
import com.gabcytn.spring_messaging.model.SocketResponse;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import com.gabcytn.spring_messaging.repository.MessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationsRepository conversationsRepository;
    private final MessageRepository messageRepository;
    private final ValidatorService validatorService;

    public MessageService(
            ConversationsRepository conversationsRepository,
            MessageRepository messageRepository,
            ValidatorService validatorService) {
        this.conversationsRepository = conversationsRepository;
        this.messageRepository = messageRepository;
        this.validatorService = validatorService;
    }

    public SocketResponse<OutgoingMessage> createMessageRequest (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived, UUID recipientUUID)
    {
        try
        {
            UUID senderId = getMessageSenderUUID(headerAccessor);
            String senderUsername = getMessageSenderUsername(headerAccessor);
            String content = messageReceived.content();

            validatorService.validateMessageRequest(recipientUUID, senderId);

            final Integer conversationId = conversationsRepository.existsByMembers(recipientUUID, senderId);

            if (conversationId != null && conversationId > 0) {
                UUID recipientId = conversationsRepository.findByIdAndNotMemberId(conversationId, senderId);
                return handleExistingMessageRequest(conversationId, recipientId, senderId, senderUsername, content);
            }

            return handleNewMessageRequest(senderId, senderUsername, recipientUUID, content);
        }
        catch (Exception e)
        {
            System.err.println("Error creating message request");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), new OutgoingMessage());
        }
    }

    public SocketResponse<OutgoingMessage> acceptMessageRequest (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived)
    {
        try
        {
            int conversationId = messageReceived.conversationId();
            UUID senderId = getMessageSenderUUID(headerAccessor);
            UUID recipientId = conversationsRepository.findByIdAndNotMemberId(messageReceived.conversationId(), senderId);
            String senderUsername = getMessageSenderUsername(headerAccessor);
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

            validatorService.validateMessageRequestAcceptance(senderId, recipientId, messageReceived);

            conversationsRepository.setRequestFalseById(conversationId);
            Integer messageId = messageRepository.save(conversationId, senderId, messageReceived.content());

            OutgoingMessage outgoingMessage = setOutgoingMessage(conversationId, senderUsername, recipientId, messageId, messageReceived.content(), false, timestamp);
            return new SocketResponse<>("OK", "Accepting message request handled successfully", outgoingMessage);
        }
        catch (Exception e)
        {
            System.err.println("Error accepting message request");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), new OutgoingMessage());
        }
    }

    public SocketResponse<OutgoingMessage> sendNormalMessage (SimpMessageHeaderAccessor headerAccessor, IncomingMessage messageReceived)
    {
        try
        {
            UUID senderUUID = getMessageSenderUUID(headerAccessor);
            String senderUsername = getMessageSenderUsername(headerAccessor);
            UUID recipientId = conversationsRepository.findByIdAndNotMemberId(messageReceived.conversationId(), senderUUID);
            int conversationId = messageReceived.conversationId();

            validatorService.validateNormalMessage(senderUUID, recipientId, messageReceived);

            Integer messageId = messageRepository.save(conversationId, senderUUID, messageReceived.content());
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

            final OutgoingMessage outgoingMessage = setOutgoingMessage(
                    conversationId, senderUsername, recipientId, messageId,
                    messageReceived.content(), false, timestamp);

            return new SocketResponse<>("OK", "Normal message handled successfully", outgoingMessage);
        }
        catch (Exception e)
        {
            System.err.println("Error sending normal message");
            System.err.println(e.getMessage());
            return new SocketResponse<>("ERROR", e.getMessage(), null);
        }
    }

    public ResponseEntity<List<OutgoingMessage>> getMessageHistory (HttpServletRequest request, int conversationId, int cursor) {
        cursor = cursor == 0 ? messageRepository.getLastMessageId() + 1 : cursor;
        try {
            UUID senderId = UUID.fromString((String) request.getSession().getAttribute("uuid"));
            if (!conversationsRepository.existsByIdAndMemberId(conversationId, senderId))
                throw new AccessDeniedException("User is not a member of this conversation");

            final List<OutgoingMessage> outgoingMessages = messageRepository.findAllByConversationId(conversationId, cursor);
            return new ResponseEntity<>(outgoingMessages, HttpStatus.OK);
        } catch (AccessDeniedException e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            System.err.println("Error fetching past messages");
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private SocketResponse<OutgoingMessage> handleNewMessageRequest(UUID senderUUID, String senderUsername, UUID recipientUUID, String message)
    {
        final int newConversationId = conversationsRepository.create();
        conversationsRepository.saveMembers(newConversationId, senderUUID, recipientUUID);
        Integer messageId = messageRepository.save(newConversationId, senderUUID, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final OutgoingMessage outgoingMessage = new OutgoingMessage(newConversationId, senderUsername, message, messageId, true, timestamp);

        return new SocketResponse<>("OK", "New message request handled successfully", outgoingMessage);
    }

    private SocketResponse<OutgoingMessage> handleExistingMessageRequest (int conversationId, UUID senderId, UUID recipientId, String senderUsername, String message)
    {
        Integer messageId = messageRepository.save(conversationId, senderId, message);

        final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        final OutgoingMessage outgoingMessage = setOutgoingMessage(conversationId, senderUsername, recipientId, messageId, message, true, timestamp);

        return new SocketResponse<>("OK", "Existing message request handled successfully", outgoingMessage);
    }

    private UUID getMessageSenderUUID(SimpMessageHeaderAccessor headerAccessor) {
        return UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
    }

    private String getMessageSenderUsername(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }

    private OutgoingMessage setOutgoingMessage (int conversationId, String senderUsername, UUID recipientId, int messageId, String message, boolean isRequest, Timestamp timestamp)
    {
        final OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.setConversationId(conversationId);
        outgoingMessage.setSender(senderUsername);
        outgoingMessage.setRecipientId(recipientId);
        outgoingMessage.setMessageId(messageId);
        outgoingMessage.setMessage(message);
        outgoingMessage.setRequest(false);
        outgoingMessage.setSentAt(timestamp);

        return outgoingMessage;
    }
}
