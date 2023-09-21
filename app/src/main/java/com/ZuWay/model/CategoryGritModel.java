package com.ZuWay.model;

public class CategoryGritModel {


    private int grit_image;
    private String grit_name;
    private String grit_dis;

    public CategoryGritModel(int grit_image,String grit_name,String grit_dis) {

        this.grit_image = grit_image;
        this.grit_name  = grit_name;
        this.grit_dis   = grit_dis;

    }

    public int getGrit_image() {
        return grit_image;
    }

    public String getGrit_name() {
        return grit_name;
    }

    public String getGrit_dis() {
        return grit_dis;
    }
}
