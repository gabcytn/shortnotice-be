package com.gabcytn.spring_messaging.http_controller;

import com.gabcytn.spring_messaging.model.OutgoingMessage;
import com.gabcytn.spring_messaging.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class HttpMessageController {
    private final MessageService messageService;

    public HttpMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/history/{conversationId}")
    public ResponseEntity<List<OutgoingMessage>> getMessageHistory (
            HttpServletRequest request,
            @PathVariable int conversationId,
            @RequestParam(value="cursor", defaultValue = "0") int cursor
    ) {
        return messageService.getMessageHistory(request, conversationId, cursor);
    }
}
