package com.ZuWay.model;

public class ItemGritModel {


    private int grit_image;
    private String grit_hartcount;
    private String grit_price;
    private String grit_origiprice;
    private String grit_save;

    public ItemGritModel(int grit_image, String grit_hartcount, String grit_price, String grit_origiprice, String grit_save) {

        this.grit_image = grit_image;
        this.grit_hartcount = grit_hartcount;
        this.grit_price = grit_price;
        this.grit_origiprice = grit_origiprice;
        this.grit_save = grit_save;

    }

    public int getGrit_image() {
        return grit_image;
    }


    public String getGrit_hartcount() {
        return grit_hartcount;
    }

    public String getGrit_price() {
        return grit_price;
    }

    public String getGrit_origiprice() {
        return grit_origiprice;
    }

    public String getGrit_save() {
        return grit_save;
    }
}
