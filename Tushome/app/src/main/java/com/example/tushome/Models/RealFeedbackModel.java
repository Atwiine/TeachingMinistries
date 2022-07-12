package com.example.tushome.Models;

public class RealFeedbackModel {
    String id, feedback, from_who;

    public RealFeedbackModel(String id, String feedback, String from_who) {
        this.id = id;
        this.feedback = feedback;
        this.from_who = from_who;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFrom_who() {
        return from_who;
    }

    public void setFrom_who(String from_who) {
        this.from_who = from_who;
    }
}
