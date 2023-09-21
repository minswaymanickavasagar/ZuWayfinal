package com.ZuWay.model;

public class MyWishModel {
    private String item_name,item_price,item_origiprice,item_save;
    private String item_deleverydate;
    private int item_image;
    String rating_value;;

    public MyWishModel(String item_name,String item_price,String item_origiprice,String item_save,String rating_value, int item_image) {

        this.item_name = item_name;
        this.item_price = item_price;
        this.item_origiprice = item_origiprice;
        this.item_save = item_save;
        this.item_image = item_image;
        this.rating_value = rating_value;

    }


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_origiprice() {
        return item_origiprice;
    }

    public void setItem_origiprice(String item_origiprice) {
        this.item_origiprice = item_origiprice;
    }

    public String getItem_save() {
        return item_save;
    }

    public void setItem_save(String item_save) {
        this.item_save = item_save;
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

    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }
}
