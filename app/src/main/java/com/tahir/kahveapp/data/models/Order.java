package com.tahir.kahveapp.data.models;

import com.google.firebase.database.Exclude;

public class Order {
    private String orderID;
    private String tableID;
    private String customerID;
    private String orderName;
    private String orderImageUrl;
    private float orderPrice;
    private String orderStatus;

    @Exclude
    private boolean isSucces;
    @Exclude
    private String message;

    public Order(){

    }

    public Order(String orderID,String tableID, String customerID, String orderName, String orderImageUrl, float orderPrice, String orderStatus) {
        this.orderID = orderID;
        this.tableID = tableID;
        this.customerID = customerID;
        this.orderName = orderName;
        this.orderImageUrl = orderImageUrl;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
    }

    public Order(String tableID, String customerID, String orderName, String orderImageUrl, float orderPrice, String orderStatus) {
        this.tableID = tableID;
        this.customerID = customerID;
        this.orderName = orderName;
        this.orderImageUrl = orderImageUrl;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }
}
