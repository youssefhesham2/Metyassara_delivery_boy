package com.delevery.metyassaradeliveryboy.model;

public class DeliveryBoyModel {
    private String Delivery_id,Name,Phone,Mail,Imag_url,Status,HaveOrderOrNo;
    private double AllMany,MyEarn;
    private float Rate;

    public DeliveryBoyModel(String delivery_id, String name, String phone, String mail, String imag_url, String status, String haveOrderOrNo, float rate, double allMany, double myEarn) {
        Delivery_id = delivery_id;
        Name = name;
        Phone = phone;
        Mail = mail;
        Imag_url = imag_url;
        Status = status;
        HaveOrderOrNo = haveOrderOrNo;
        Rate = rate;
        AllMany = allMany;
        MyEarn = myEarn;
    }

    public DeliveryBoyModel() {
    }

    public String getDelivery_id() {
        return Delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        Delivery_id = delivery_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getMail() {
        return Mail;
    }

    public void setMail(String mail) {
        Mail = mail;
    }

    public String getImag_url() {
        return Imag_url;
    }

    public void setImag_url(String imag_url) {
        Imag_url = imag_url;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getHaveOrderOrNo() {
        return HaveOrderOrNo;
    }

    public void setHaveOrderOrNo(String haveOrderOrNo) {
        HaveOrderOrNo = haveOrderOrNo;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public double getAllMany() {
        return AllMany;
    }

    public void setAllMany(double allMany) {
        AllMany = allMany;
    }

    public double getMyEarn() {
        return MyEarn;
    }

    public void setMyEarn(double myEarn) {
        MyEarn = myEarn;
    }
}
