package com.tahir.kahveapp.data.models;

import com.google.firebase.database.Exclude;

public class Order {
    private String tableID;
    private String customerID;
    private String orderName;
    private String orderImageUrl;
    private String orderPrice;

    @Exclude
    private boolean isSucces;
    @Exclude
    private String message;

    public Order(){

    }

    public Order(String orderName, String orderImageUrl, String orderPrice) {
        this.orderName = orderName;
        this.orderImageUrl = orderImageUrl;
        this.orderPrice = orderPrice;
    }

    public boolean isSucces() {
        return isSucces;
    }

    public void setSucces(boolean succes) {
        isSucces = succes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderImageUrl() {
        return orderImageUrl;
    }

    public void setOrderImageUrl(String orderImageUrl) {
        this.orderImageUrl = orderImageUrl;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
}
