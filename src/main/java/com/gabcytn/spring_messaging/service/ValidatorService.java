package com.gabcytn.spring_messaging.service;

import com.gabcytn.spring_messaging.model.IncomingMessage;
import com.gabcytn.spring_messaging.repository.BlocksRepository;
import com.gabcytn.spring_messaging.repository.ConversationsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidatorService {
    private final ConversationsRepository conversationsRepository;
    private final BlocksRepository blocksRepository;

    public ValidatorService (ConversationsRepository conversationsRepository, BlocksRepository blocksRepository) {
        this.conversationsRepository = conversationsRepository;
        this.blocksRepository = blocksRepository;
    }

    public void validateNormalMessage (UUID senderId, UUID recipientId, IncomingMessage incomingMessage) {
        // checks if a conversation id exists
        if (!conversationsRepository.existsById(incomingMessage.conversationId()))
            throw new Error("Conversation does not exist");

        // validate that the sender is a member of the conversation
        if (!conversationsRepository.existsByIdAndMemberId(incomingMessage.conversationId(), senderId))
            throw new Error("User is not a member of this conversation");

        // checks if the sender is blocked by the recipient
        if (blocksRepository.existsByBlockerIdAndBlockedId(recipientId, senderId))
            throw new Error("Sender is blocked by the recipient");
    }

    public void validateMessageRequest (UUID recipientUUID, UUID senderUUID) {
        if (blocksRepository.existsByBlockerIdAndBlockedId(recipientUUID, senderUUID))
            throw new Error("User is blocked");
    }

    public void validateMessageRequestAcceptance (UUID senderId, UUID recipientId, IncomingMessage incomingMessage) {
        validateNormalMessage(senderId, recipientId, incomingMessage);

        // checks if the conversation (id) is a request
        if (!conversationsRepository.existsByIdAndRequestTrue(incomingMessage.conversationId()))
            throw new Error("Conversation is not a request");
    }
}
