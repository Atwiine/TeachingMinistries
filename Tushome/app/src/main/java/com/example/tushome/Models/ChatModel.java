package com.example.tushome.Models;

public class ChatModel {
    String message, time, sender, receiver, image,recieverID;

    public ChatModel() {
    }


    public ChatModel(String message, String time, String sender, String receiver, String image, String recieverID) {
        this.message = message;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.image = image;
        this.recieverID = recieverID;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }
}

