package com.example.tushome.Models;

public class CommentsModel {
    String id, comments, date,comment_fromwho,fromimage;

    public CommentsModel(String id,
                         String comments,
                         String date ,
                         String comment_fromwho,
                         String fromimage) {
        this.id = id;
        this.comments = comments;
        this.date = date;
        this.comment_fromwho = comment_fromwho;
        this.fromimage = fromimage;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment_fromwho() {
        return comment_fromwho;
    }

    public void setComment_fromwho(String comment_fromwho) {
        this.comment_fromwho = comment_fromwho;
    }

    public String getFromimage() {
        return fromimage;
    }

    public void setFromimage(String fromimage) {
        this.fromimage = fromimage;
    }
}
