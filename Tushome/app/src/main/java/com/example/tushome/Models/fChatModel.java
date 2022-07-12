package com.example.tushome.Models;

public class fChatModel {
    String message, time, sender, receiver;

    public fChatModel() {
    }

    public fChatModel(String message, String time, String sender, String receiver) {
        this.message = message;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
