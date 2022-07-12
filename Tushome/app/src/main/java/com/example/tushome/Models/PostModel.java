package com.example.tushome.Models;

public class PostModel {
    String postID,userimage,username,time,postimage,message,likes,comments;

    public PostModel(String postID,
                     String userimage,
                     String username,
                     String time,
                     String postimage,
                     String message,
                     String likes,
                     String comments) {
        this.postID = postID;
        this.userimage = userimage;
        this.username = username;
        this.time = time;
        this.postimage = postimage;
        this.message = message;
        this.likes = likes;
        this.comments = comments;
    }


    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

