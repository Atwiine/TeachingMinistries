package com.example.tushome.Models;

public class AllRequestsModel {
    String id;
    String email;
    String district;
    String sub_county;
    String residence;
    String soft;
    String hard;
    String status;
    String total_request;
    String reader_name;

    public AllRequestsModel(String id,
                            String email,
                            String district,
                            String sub_county,
                            String residence,
                            String soft,
                            String hard,
                            String status,
                            String total_request,
                            String reader_name) {
        this.id = id;
        this.email = email;
        this.district = district;
        this.sub_county = sub_county;
        this.residence = residence;
        this.soft = soft;
        this.hard = hard;
        this.status = status;
        this.total_request = total_request;
        this.reader_name = reader_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSub_county() {
        return sub_county;
    }

    public void setSub_county(String sub_county) {
        this.sub_county = sub_county;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getSoft() {
        return soft;
    }

    public void setSoft(String soft) {
        this.soft = soft;
    }

    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_request() {
        return total_request;
    }

    public void setTotal_request(String total_request) {
        this.total_request = total_request;
    }

    public String getReader_name() {
        return reader_name;
    }

    public void setReader_name(String reader_name) {
        this.reader_name = reader_name;
    }
}
