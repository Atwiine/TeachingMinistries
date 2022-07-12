package com.example.teachingministries.Models;

public class StudentsModel {
    String id, names, phone,remarks, track;
    private boolean isSelected;

    public StudentsModel(String id, String names, String phone, String remarks, String track) {
        this.id = id;
        this.names = names;
        this.phone = phone;
        this.remarks = remarks;
        this.track = track;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

}
