package com.ZuWay.model;

public class MyOrderModel {
    private String item_name;
    private String item_deleverydate;
    private int item_image;
    private String item_rating;

    public MyOrderModel(String item_name,String item_deleverydate,int item_image,String item_rating) {

        this.item_name = item_name;
        this.item_deleverydate = item_deleverydate;
        this.item_image = item_image;
        this.item_rating = item_rating;

    }


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_deleverydate() {
        return item_deleverydate;
    }

    public void setItem_deleverydate(String item_deleverydate) {
        this.item_deleverydate = item_deleverydate;
    }

    public int getItem_image() {
        return item_image;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }

    public String getItem_rating() {
        return item_rating;
    }

    public void setItem_rating(String item_rating) {
        this.item_rating = item_rating;
    }
}
