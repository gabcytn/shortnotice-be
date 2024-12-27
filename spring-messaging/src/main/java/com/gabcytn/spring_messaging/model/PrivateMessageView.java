package com.gabcytn.spring_messaging.model;

import java.sql.Timestamp;

public class PrivateMessageView {
    private String sender;
    private String receiver;
    private String message;
    private Timestamp sentAt;

    public PrivateMessageView () {}

    public PrivateMessageView(String sender, String receiver, String message, Timestamp sentAt) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sentAt = sentAt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
