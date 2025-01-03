package com.gabcytn.spring_messaging.model;

import java.sql.Timestamp;

public class PrivateMessage {
    private String sender;
    private String message;
    private int conversationId;
    private boolean isRequest;
    private Timestamp sentAt;

    public PrivateMessage() {}

    public PrivateMessage(String sender, String message, int conversationId, boolean isRequest, Timestamp sentAt) {
        this.sender = sender;
        this.message = message;
        this.conversationId = conversationId;
        this.isRequest = isRequest;
        this.sentAt = sentAt;
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
                ", conversationId=" + conversationId +
                ", isRequest=" + isRequest +
                ", sentAt=" + sentAt +
                '}';
    }
}
