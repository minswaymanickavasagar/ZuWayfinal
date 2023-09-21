package com.ZuWay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ZuWay.activity.HomeActivity;


public class PlaceOrderSession {
    public PlaceOrderSession() {

    }

    public static PlaceOrderSession bSession;

    public static PlaceOrderSession getInstance() {

        if (bSession == null) {
            synchronized (PlaceOrderSession.class) {
                bSession = new PlaceOrderSession();
            }
        }
        return bSession;
    }

    private static String TAG = PlaceOrderSession.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;

    SharedPreferences prefs;

    private String sessionId;
    private String user_id;
    private String user_name;
    private String user_mobile;
    private String user_email;
    private String address;
    private String user_profileimage;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public void initialize(Context context,
                           String user_id,
                           String user_name,
                           String address,
                           String user_email,
                           String sessionId) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_id", user_id);
        editor.putString("user_name", user_name);
        editor.putString("address", address);
        editor.putString("user_email", user_email);
        editor.putString("sessionId", sessionId);
        editor.apply();

        this.user_id = user_id;
        this.user_name = user_name;
        this.address = address;
        this.user_email = user_email;
        this.sessionId = sessionId;
    }


    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.user_id = null;
        this.user_name = null;
        this.address = null;
        this.user_email = null;
        this.sessionId = null;
    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("Krishnan Store", Context.MODE_PRIVATE);
        String[] sharedValues = new String[7];

        sharedValues[0] = this.user_id = prefs.getString("user_id", "");
        sharedValues[1] = this.user_name = prefs.getString("user_name", "");
        sharedValues[2] = this.address = prefs.getString("address", "");
        sharedValues[3] = this.user_email = prefs.getString("user_email", "");
        sharedValues[4] = this.sessionId = prefs.getString("sessionId", "");

        return sharedValues;
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
       /* System.out.println("====" + this.sessionId + "UN" + this.name);
        if (this.sessionId == null || this.name == null || this.sessionId.isEmpty() || this.name.isEmpty()||
                this.sessionId.equals("NoID") || this.name.equals("NoUsername")||this.userid==null){
            return false;

              System.out.println("====" + this.sessionId + "UN" + this.userid);
        if (this.userid == null ||  this.userid.isEmpty()||
                this.userid.equals("Nouserid") ){
            return false;

        }

        }*/
        System.out.println("====" + this.sessionId + "UN" + this.user_mobile);
        if (this.user_mobile == null || this.user_mobile.isEmpty() || this.user_mobile.equals("Nouser_mobile")) {
            return false;

        }
        System.out.println("====" + this.sessionId + "UN--true" + this.user_mobile);
        return true;
    }

    public String getUser_id(Context context) {
        getSession(context);
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSessionId(Context context) {
        getSession(context);
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAddress(Context context) {
        getSession(context);
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


}
