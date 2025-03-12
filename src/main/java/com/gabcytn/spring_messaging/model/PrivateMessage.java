package com.gabcytn.spring_messaging.model;

import java.sql.Timestamp;
import java.util.UUID;

public class PrivateMessage {
    private int conversationId;
    private String sender;
    private UUID recipientId;
    private String message;
    private int messageId;
    private boolean isRequest;
    private Timestamp sentAt;

    public PrivateMessage() {
    }

    public PrivateMessage(int conversationId, String sender, String message, int messageId, boolean isRequest,
            Timestamp sentAt) {
        this.sender = sender;
        this.message = message;
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.isRequest = isRequest;
        this.sentAt = sentAt;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                ", conversationId=" + conversationId +
                ", isRequest=" + isRequest +
                ", sentAt=" + sentAt +
                '}';
    }
}
