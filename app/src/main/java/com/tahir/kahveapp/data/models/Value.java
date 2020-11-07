package com.tahir.kahveapp.data.models;

public class Value {

    private float discountFactor;
    private float discountPercent;
    private float staticDiscount;
    private float maxDiscount;

    public Value(){}

    public Value(float discountFactor, float staticDiscount, float discountPercent, float maxDiscount) {
        this.discountFactor = discountFactor;
        this.staticDiscount = staticDiscount;
        this.discountPercent = discountPercent;
        this.maxDiscount = maxDiscount;
    }

    public float getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(float maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getDiscountFactor() {
        return discountFactor;
    }

    public void setDiscountFactor(float discountFactor) {
        this.discountFactor = discountFactor;
    }

    public float getStaticDiscount() {
        return staticDiscount;
    }

    public void setStaticDiscount(float staticDiscount) {
        this.staticDiscount = staticDiscount;
    }
}
