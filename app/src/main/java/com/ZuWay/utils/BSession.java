package com.ZuWay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ZuWay.activity.HomeActivity;


public class BSession {
    public BSession() {

    }

    public static BSession bSession;

    public static BSession getInstance() {

        if (bSession == null) {
            synchronized (BSession.class) {
                bSession = new BSession();
            }
        }
        return bSession;
    }

    private static String TAG = BSession.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;

    SharedPreferences prefs;

    private String sessionId;
    private String customerid;
    private String user_name;
    private String user_mobile;
    private String user_email, user_id;
    private String user_address;
    private String user_profileimage, status;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String user_id,
                           String user_name,
                           String user_mobile,
                           String user_email,
                           String user_address,
                           String status,
                           String user_profileimage,
                           String sessionId) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("user_mobile", user_mobile);
        editor.putString("user_email", user_email);
        editor.putString("user_address", user_address);
        editor.putString("status", status);
        editor.putString("user_profileimage", user_profileimage);
        editor.putString("sessionId", sessionId);
        editor.apply();

        this.user_id = user_id;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_email = user_email;
        this.user_address = user_address;
        this.status = user_address;
        this.user_profileimage = user_profileimage;
        this.sessionId = sessionId;
    }


    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.user_id = null;
        this.user_name = null;
        this.user_mobile = null;
        this.user_email = null;
        this.user_address = null;
        this.status = null;
        this.user_profileimage = null;
        this.sessionId = null;
    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String[] sharedValues = new String[8];

        sharedValues[0] = this.user_id = prefs.getString("user_id", "");
        sharedValues[1] = this.user_name = prefs.getString("user_name", "");
        sharedValues[2] = this.user_mobile = prefs.getString("user_mobile", "");
        sharedValues[3] = this.user_email = prefs.getString("user_email", "");
        sharedValues[4] = this.user_address = prefs.getString("user_address", "");
        sharedValues[5] = this.status = prefs.getString("status", "");
        sharedValues[6] = this.user_profileimage = prefs.getString("user_profileimage", "");
        sharedValues[7] = this.sessionId = prefs.getString("sessionId", "NosessionId");

        return sharedValues;
    }

    public String getUser_id(Context context) {
        getSession(context);
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public Boolean isAuthenticated(Context context) {

        if (this.sessionId == null || this.user_name == null || this.sessionId.isEmpty() || this.user_name.isEmpty() || this.sessionId.equals("NosessionId") || this.user_name.equals("Nouser_name")) {
            Intent intent = null;
            intent = new Intent(context.getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    public Boolean isApplicationExit(Context context) {

        getSession(context);

        System.out.println("====" + this.sessionId + "UN" + this.user_id);
        if (this.user_id == null || this.user_id.isEmpty() || this.user_id.equals("Nouser_mobile")) {
            return false;

        }
        System.out.println("====" + this.sessionId + "UN--true" + this.user_id);
        return true;
    }

    public String getStatus(Context context) {
        getSession(context);
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId(Context context) {
        getSession(context);
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustomerid(Context context) {
        getSession(context);
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
/* public String getUser_id(Context context) {
        getSession(context);
        return customerid;
    }

    public void setUser_id(String customerid) {
        this.customerid = customerid;
    }*/

    public String getUser_name(Context context) {
        getSession(context);
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile(Context context) {
        getSession(context);
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_email(Context context) {
        getSession(context);
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_address(Context context) {
        getSession(context);
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_profileimage(Context context) {
        getSession(context);
        return user_profileimage;
    }

    public void setUser_profileimage(String user_profileimage) {
        this.user_profileimage = user_profileimage;
    }
}

