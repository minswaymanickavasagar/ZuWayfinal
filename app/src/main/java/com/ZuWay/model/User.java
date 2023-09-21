package com.ZuWay.model;


import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String userid;
    private String status;
    private String action;
    private String message;
    private String name;
    private String mobile;
    private String password;
    private String address;
    private String email;
    private String latitude;
    private String longitude;
    private String user_token;


    public User() {
    }

    public User(JSONObject jsonObject) {
        try {
            if (jsonObject.has("userid")) {
                this.userid = jsonObject.getString("userid");
            }
            if (jsonObject.has("status")) {
                this.status = jsonObject.getString("status");
            }
            if (jsonObject.has("name")) {
                this.name = jsonObject.getString("name");
            }
            if (jsonObject.has("email")) {
                this.email = jsonObject.getString("email");
            }
            if (jsonObject.has("mobile")) {
                this.mobile = jsonObject.getString("mobile");
            }
            if (jsonObject.has("password")) {
                this.password = jsonObject.getString("password");
            }

            if (jsonObject.has("address")) {
                this.address = jsonObject.getString("address");
            }


            if (jsonObject.has("action")) {
                this.action = jsonObject.getString("action");
            }
            if (jsonObject.has("message")) {
                this.message = jsonObject.getString("message");
            }

            if (jsonObject.has("latitude")) {
                this.latitude = jsonObject.getString("latitude");
            }
            if (jsonObject.has("langtitude")) {
                this.longitude = jsonObject.getString("langtitude");
            }
            if (jsonObject.has("user_token")) {
                this.user_token = jsonObject.getString("user_token");
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
