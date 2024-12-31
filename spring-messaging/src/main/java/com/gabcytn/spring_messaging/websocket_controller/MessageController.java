package com.gabcytn.spring_messaging.websocket_controller;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.service.MessageService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Optional;
import java.util.UUID;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/request/{uuid}")
    public void privateMessageRequest(
            @DestinationVariable UUID uuid,
            Message messageReceived,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        final Optional<PrivateMessage> privateMessageView =
                messageService.createMessageRequest(headerAccessor, messageReceived, uuid);
        messagingTemplate.convertAndSend("/topic/private/" + uuid, privateMessageView.orElseThrow());
    }

}
