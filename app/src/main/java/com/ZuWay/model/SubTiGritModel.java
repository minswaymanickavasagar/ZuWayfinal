package com.ZuWay.model;

public class SubTiGritModel {
    private String category_name;
    private String category_image;

    public SubTiGritModel() {
    }

    public SubTiGritModel(String category_name, String category_image) {
        this.category_name = category_name;
        this.category_image = category_image;

    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

}
