package com.gabcytn.spring_messaging.model;

public record IncomingMessage(Integer conversationId, String content) {}