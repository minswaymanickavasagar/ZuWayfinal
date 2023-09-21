package com.ZuWay.model;

public class MyCartModel {
    private String item_name,item_price,item_origiprice,item_save,item_size,item_color,item_dd,item_dch;
    private int item_image;


    public MyCartModel(String item_name, String item_price, String item_origiprice, String item_save, String item_size,String item_color,String item_dd,String item_dch, int item_image){


        this.item_name = item_name;
        this.item_price = item_price;
        this.item_origiprice = item_origiprice;
        this.item_save = item_save;
        this.item_size = item_size;
        this.item_color = item_color;
        this.item_dd = item_dd;
        this.item_dch = item_dch;
        this.item_image=item_image;
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

    public String getItem_size() {
        return item_size;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }

    public String getItem_color() {
        return item_color;
    }

    public void setItem_color(String item_color) {
        this.item_color = item_color;
    }

    public String getItem_dd() {
        return item_dd;
    }

    public void setItem_dd(String item_dd) {
        this.item_dd = item_dd;
    }

    public String getItem_dch() {
        return item_dch;
    }

    public void setItem_dch(String item_dch) {
        this.item_dch = item_dch;
    }

    public int getItem_image() {
        return item_image;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }
}
