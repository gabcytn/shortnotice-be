package com.gabcytn.spring_messaging.websocket_controller;

import com.gabcytn.spring_messaging.model.IncomingMessage;
import com.gabcytn.spring_messaging.model.OutgoingMessage;
import com.gabcytn.spring_messaging.model.SocketResponse;
import com.gabcytn.spring_messaging.service.MessageService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    // send a message request (initializing of a conversation by one user) (may be multiple times)
    @MessageMapping("/request/{uuid}")
    public void privateMessageRequest(
            @DestinationVariable UUID uuid,
            IncomingMessage messageReceived,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        final SocketResponse<OutgoingMessage> messageToSend =
                messageService.createMessageRequest(headerAccessor, messageReceived, uuid);
        messagingTemplate.convertAndSend("/topic/private/" + messageToSend.getBody().getRecipientId(), messageToSend);
    }

    @MessageMapping("/approve")
    public void approveMessageRequest(
            IncomingMessage messageReceived,
            SimpMessageHeaderAccessor headerAccessor
    ) {
         final SocketResponse<OutgoingMessage> messageToSend =
                 messageService.acceptMessageRequest(headerAccessor, messageReceived);
         messagingTemplate.convertAndSend("/topic/private/" + messageToSend.getBody().getRecipientId(), messageToSend);
    }

    @MessageMapping("/message")
    public void sendPrivateMessage (
            IncomingMessage messageReceived,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        final SocketResponse<OutgoingMessage> messageToSend =
                messageService.sendNormalMessage(headerAccessor, messageReceived);
        messagingTemplate.convertAndSend("/topic/private/" + messageToSend.getBody().getRecipientId(), messageToSend);
    }
}
