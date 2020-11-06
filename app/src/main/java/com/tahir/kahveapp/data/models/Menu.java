package com.tahir.kahveapp.data.models;

import com.google.firebase.database.Exclude;

public class Menu {

    private String itemName;
    private String itemImageUrl;
    private String itemPrice;

    @Exclude
    private boolean isSucces;
    @Exclude
    private String message;

    //to handle color of card when choosen or not
    @Exclude
    private boolean isChoosen;

    public Menu(){

    }

    public Menu(String itemName, String itemImageUrl, String itemPrice) {
        this.itemName = itemName;
        this.itemImageUrl = itemImageUrl;
        this.itemPrice = itemPrice;
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(boolean choosen) {
        isChoosen = choosen;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }
}
