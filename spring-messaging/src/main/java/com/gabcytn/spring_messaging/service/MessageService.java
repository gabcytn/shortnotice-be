package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessageView;
import com.gabcytn.spring_messaging.model.User;
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
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public PrivateMessageView savePrivateMessage (
            SimpMessageHeaderAccessor headerAccessor,
            Message messageReceived,
            String receiverUsername
    ) {
        try {
            final UUID senderId = UUID.fromString((String) headerAccessor.getSessionAttributes().get("uuid"));
            final Optional<User> user = userRepository.findByUsername(receiverUsername);
            final UUID receiverId = user.orElseThrow().getId();
            final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            messageRepository.savePrivateMessage(
                    senderId,
                    receiverId,
                    messageReceived.getMessage(),
                    timestamp
            );
            return new PrivateMessageView(
                    messageReceived.getSender(),
                    receiverUsername,
                    messageReceived.getMessage(),
                    timestamp
            );
        } catch (Exception e) {
            System.err.println("Error in saving private message");
            System.err.println(e.getMessage());
            return new PrivateMessageView();
        }
    }
}
