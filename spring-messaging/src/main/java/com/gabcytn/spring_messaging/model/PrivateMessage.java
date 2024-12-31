package com.gabcytn.spring_messaging.model;

import java.sql.Timestamp;

public class PrivateMessage {
    private String sender;
    private String message;
    private Timestamp sentAt;

    public PrivateMessage() {}

    public PrivateMessage(String sender, String message, Timestamp sentAt) {
        this.sender = sender;
        this.message = message;
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

    @Override
    public String toString() {
        return "PrivateMessageView{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
