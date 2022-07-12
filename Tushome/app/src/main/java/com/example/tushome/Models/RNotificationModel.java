package com.example.tushome.Models;

public class RNotificationModel {
    String id, title, status,date,reason;


    public RNotificationModel(String id,
                              String title,
                              String status,
                              String date,
                              String reason) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.date = date;
        this.reason = reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
