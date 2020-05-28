package com.delevery.metyassaradeliveryboy.model;

public class RequestsModel {
    private String restaurant_name, addreasr, phone, describe_order, client_id, delevery_id, date, status;
    private double cash;

    public RequestsModel(String restaurant_name, String addreasr, String phone, String describe_order, String client_id, String delevery_id, String date, String status, double cash) {
        this.restaurant_name = restaurant_name;
        this.addreasr = addreasr;
        this.phone = phone;
        this.describe_order = describe_order;
        this.client_id = client_id;
        this.delevery_id = delevery_id;
        this.date = date;
        this.status = status;
        this.cash = cash;
    }

    public RequestsModel() {
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getAddreasr() {
        return addreasr;
    }

    public void setAddreasr(String addreasr) {
        this.addreasr = addreasr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescribe_order() {
        return describe_order;
    }

    public void setDescribe_order(String describe_order) {
        this.describe_order = describe_order;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getDelevery_id() {
        return delevery_id;
    }

    public void setDelevery_id(String delevery_id) {
        this.delevery_id = delevery_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }
}
