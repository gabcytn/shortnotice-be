package com.gabcytn.spring_messaging.websocket_controller;

import com.gabcytn.spring_messaging.model.Message;
import com.gabcytn.spring_messaging.model.PrivateMessageView;
import com.gabcytn.spring_messaging.service.MessageService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/test")
    @SendTo("/topic/message")
    public Message message(Message message, SimpMessageHeaderAccessor headerAccessor) throws InterruptedException {
        final Message messageToBeSent = new Message();
        final String sender = (String) headerAccessor.getSessionAttributes().get("username");

        messageToBeSent.setMessage(message.getMessage());
        messageToBeSent.setSender(sender);

        return messageToBeSent;
    }

    @MessageMapping("/private/{username}")
    public void testPrivate(
            @DestinationVariable String username,
            Message messageReceived,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        final PrivateMessageView messageToSend = messageService
                .savePrivateMessage(headerAccessor, messageReceived, username);
        messagingTemplate.convertAndSend("/topic/private/" + username, messageToSend);
    }

    /*
     * @MessageMapping("/group/{groupId}")
     * public void testGroup(@DestinationVariable String groupId, Message
     * messageReceived) {
     * System.out.println("Message for group: " + groupId + ", received!");
     * 
     * messagingTemplate.convertAndSend("topic/group/" + groupId, new Message());
     * }
     */
}
