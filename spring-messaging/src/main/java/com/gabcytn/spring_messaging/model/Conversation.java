package com.gabcytn.spring_messaging.model;

import java.util.UUID;

public record Conversation(int id, UUID recipientId, String recipientUsername) { }
