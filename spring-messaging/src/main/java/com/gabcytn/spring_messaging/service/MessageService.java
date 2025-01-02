package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.model.User;
import com.gabcytn.spring_messaging.repository.BlocksRepository;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import com.gabcytn.spring_messaging.repository.MessageRepository;
import com.gabcytn.spring_messaging.repository.UserRepository;
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

    public Optional<PrivateMessage> createMessageRequest (SimpMessageHeaderAccessor headerAccessor, Message messageReceived, UUID recipientUUID)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();
            final Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

            if (blocksRepository.existsByBlockerIdAndBlockedId(recipientUUID, senderUUID))
                return Optional.empty();
            if (conversationsRepository.isConversationExisting(recipientUUID, senderUUID))
                return Optional.empty();

            final int conversationId = conversationsRepository.create();
            conversationsRepository.saveMembers(conversationId, senderUUID, recipientUUID);
            messageRepository.save(conversationId, senderUUID, content);

            return Optional.of(new PrivateMessage(senderUsername, content, currentTimestamp));
        }
        catch (Exception e)
        {
            System.err.println("Error creating message request");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<PrivateMessage> acceptMessageRequest (SimpMessageHeaderAccessor headerAccessor, Message messageReceived, int conversationId)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final String content = messageReceived.content();
            final Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
            final Optional<User> recipient = userRepository.findByUsername(messageReceived.recipient());

            if (blocksRepository.existsByBlockerIdAndBlockedId(recipient.orElseThrow().getId(), senderUUID))
                return Optional.empty();
            conversationsRepository.setRequestFalseById(conversationId);
            messageRepository.save(conversationId, senderUUID, content);

            return Optional.of(new PrivateMessage(senderUsername, content, currentTimestamp));
        }
        catch (Exception e)
        {
            System.err.println("Error accepting message request");
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<PrivateMessage> sendPrivateMessage (SimpMessageHeaderAccessor headerAccessor, Message messageReceived, int conversationId)
    {
        try
        {
            final UUID senderUUID = getMessageSenderUUID(headerAccessor);
            final String senderUsername = getMessageSenderUsername(headerAccessor);
            final Optional<User> user = userRepository.findByUsername(messageReceived.recipient());

            if (blocksRepository.existsByBlockerIdAndBlockedId(user.orElseThrow().getId(), senderUUID))
                throw new Error("Sender is blocked by the recipient");
            if (conversationsRepository.existsById(conversationId))
                throw new Error("Conversation does not exists");
            messageRepository.save(conversationId, senderUUID, messageReceived.content());

            final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            return Optional.of(new PrivateMessage(senderUsername, messageReceived.content(), timestamp));
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            System.err.println("Error sending private message");
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
