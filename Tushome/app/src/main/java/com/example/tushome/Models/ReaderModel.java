package com.example.tushome.Models;

public class ReaderModel {
    String id, title, image, preview, author;


    public ReaderModel(String id, String title, String image, String preview, String author) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.preview = preview;
        this.author = author;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
