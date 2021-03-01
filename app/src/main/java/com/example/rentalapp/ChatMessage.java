package com.example.rentalapp;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    long messageTime;
    private String from;
    private String to;

    public ChatMessage(String messageText, String from, String to, long messageTime) {
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.from = from;
        this.to = to;
    }

    public ChatMessage(){

    }

    public ChatMessage(String from) {
        this.from = from;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
