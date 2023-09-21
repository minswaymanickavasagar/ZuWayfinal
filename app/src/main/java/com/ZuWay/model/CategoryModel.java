package com.ZuWay.model;

public class CategoryModel {
    private String category_name;
    private String category_image,cate_id,subcate_id;

 public CategoryModel(){}
    public CategoryModel(String category_name, String category_image) {
        this.category_name = category_name;
        this.category_image = category_image;

    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getSubcate_id() {
        return subcate_id;
    }

    public void setSubcate_id(String subcate_id) {
        this.subcate_id = subcate_id;
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
