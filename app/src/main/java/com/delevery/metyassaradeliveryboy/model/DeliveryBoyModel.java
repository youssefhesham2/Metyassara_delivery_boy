package com.delevery.metyassaradeliveryboy.model;

public class DeliveryBoyModel {
    private String Delivery_id,Name,Phone,Imag_url,Status,HaveOrderOrNo,token;
    private double AllMany,MyEarn,Rate,totalrate;
    private int totalnumberofrate;

    public DeliveryBoyModel(String delivery_id, String name, String phone, String imag_url, String status, String haveOrderOrNo, String token, double allMany, double myEarn, double rate, double totalrate, int totalnumberofrate) {
        Delivery_id = delivery_id;
        Name = name;
        Phone = phone;
        Imag_url = imag_url;
        Status = status;
        HaveOrderOrNo = haveOrderOrNo;
        this.token = token;
        AllMany = allMany;
        MyEarn = myEarn;
        Rate = rate;
        this.totalrate = totalrate;
        this.totalnumberofrate = totalnumberofrate;
    }

    public double getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(double totalrate) {
        this.totalrate = totalrate;
    }

    public int getTotalnumberofrate() {
        return totalnumberofrate;
    }

    public void setTotalnumberofrate(int totalnumberofrate) {
        this.totalnumberofrate = totalnumberofrate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DeliveryBoyModel() {
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
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
