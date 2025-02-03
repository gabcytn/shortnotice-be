package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.PrivateMessage;
import com.gabcytn.spring_messaging.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class HttpMessageController {
    private final MessageService messageService;

    public HttpMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<PrivateMessage>> getMessageHistory (@PathVariable int conversationId) {
        return messageService.getMessageHistory(conversationId);
    }

    @GetMapping("/test")
    public String hi () {
        return "hi";
    }
}
