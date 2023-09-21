package com.ZuWay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import com.ZuWay.activity.HomeActivity;



/*import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;*/

/**
 * Created by Senthamil Selvan on 24-Apr-2019.
 */
public class BSession1 {
    public BSession1() {

    }

    public static BSession1 bSession;

    public static BSession1 getInstance() {

        if (bSession == null) {
            synchronized (BSession1.class) {
                bSession = new BSession1();
            }
        }
        return bSession;
    }

    private static String TAG = BSession1.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;
    SharedPreferences prefs;
    private String customerid;
    private String sessionId;
    private String user_name;
    private String user_mobile;
    private String user_email;
    private String user_address,user_profileimage;
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String customerid,
                           String user_name,
                           String user_mobile,
                           String user_email,
                           String user_address,String user_profileimage,String sessionId) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("customerid", customerid);
        editor.putString("user_name", user_name);
        editor.putString("user_mobile", user_mobile);
        editor.putString("user_email", user_email);
        editor.putString("user_address", user_address);
        editor.putString("user_profileimage", user_profileimage);
        editor.putString("sessionId", sessionId);
        editor.apply();

        this.customerid        = customerid;
        this.sessionId         = sessionId;
        this.user_name         = user_name;
        this.user_mobile       = user_mobile;
        this.user_email        = user_email;
        this.user_address      = user_address;
        this.user_profileimage = user_profileimage;
    }


    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();

        this.customerid        = null;
        this.sessionId         = null;
        this.user_name         = null;
        this.user_mobile       = null;
        this.user_email        = null;
        this.user_address      = null;
        this.user_profileimage = null;
    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String[] sharedValues = new String[24];

        sharedValues[0] = this.customerid = prefs.getString("customerid", "Nocustomerid");
        sharedValues[1] = this.sessionId = prefs.getString("sessionId", "NosessionId");
        sharedValues[2] = this.user_name = prefs.getString("user_name", "Nouser_name");
        sharedValues[3] = this.user_mobile = prefs.getString("user_mobile", "");
        sharedValues[5] = this.user_email = prefs.getString("user_email", "");
        sharedValues[6] = this.user_address = prefs.getString("user_address", "");
        sharedValues[7] = this.user_profileimage = prefs.getString("user_profileimage", "Nouser_profileimage");
        sharedValues[8] = this.sessionId = prefs.getString("sessionId", "NosessionId");


        return sharedValues;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public Boolean isAuthenticated(Context context) {

        if (this.sessionId == null || this.user_name == null || this.sessionId.isEmpty() || this.user_name.isEmpty() ||
                this.sessionId.equals("NoID") || this.user_name.equals("Nouser_name")) {
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

        System.out.println("====" + this.sessionId + "UN" + this.user_name);
        if (this.user_name == null || this.user_name.isEmpty() ||
                this.user_name.equals("Nouser_name")) {
            return false;

        }
        System.out.println("====" + this.sessionId + "UN--true" + this.user_name);
        return true;
    }


    public String getCustomerid(Context context) {
        getSession(context);
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

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


    public String getSessionId(Context context) {
        getSession(context);
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}

