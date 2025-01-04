package com.gabcytn.spring_messaging.model;

import java.sql.Timestamp;

public record Conversation(int id, String senderUsername, String message, Timestamp sentAt) { }
