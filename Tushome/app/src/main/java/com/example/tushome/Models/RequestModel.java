package com.example.tushome.Models;

public class RequestModel {
    String id, title, onlinerequests, hardrequests, totalrequests;

    public RequestModel(String id, String title, String onlinerequests, String hardrequests, String totalrequests) {
        this.id = id;
        this.title = title;
        this.onlinerequests = onlinerequests;
        this.hardrequests = hardrequests;
        this.totalrequests = totalrequests;
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

    public String getOnlinerequests() {
        return onlinerequests;
    }

    public void setOnlinerequests(String onlinerequests) {
        this.onlinerequests = onlinerequests;
    }

    public String getHardrequests() {
        return hardrequests;
    }

    public void setHardrequests(String hardrequests) {
        this.hardrequests = hardrequests;
    }

    public String getTotalrequests() {
        return totalrequests;
    }

    public void setTotalrequests(String totalrequests) {
        this.totalrequests = totalrequests;
    }
}
