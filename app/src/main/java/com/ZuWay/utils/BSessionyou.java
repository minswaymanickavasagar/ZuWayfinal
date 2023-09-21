package com.ZuWay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ZuWay.activity.LoginActivity;





/**
 * Created by Senthamil Selvan on 24-Apr-2019.
 */
public class BSessionyou {
    public BSessionyou() {

    }

    public static BSessionyou bSession;

    public static BSessionyou getInstance() {

        if (bSession == null) {
            synchronized (BSessionyou.class) {
                bSession = new BSessionyou();
            }
        }
        return bSession;
    }

    private static String TAG = BSessionyou.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;
    SharedPreferences prefs;
    private String mot_video;
    private String sessionId;

    public void initialize(Context context,
                           String mot_video) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("mot_video", mot_video);
        editor.putString("sessionId", sessionId);

        editor.apply();
        this.mot_video = mot_video;
        this.sessionId = sessionId;

    }


    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.mot_video = null;
        this.sessionId = null;

    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String[] sharedValues = new String[24];

        sharedValues[0] = this.mot_video = prefs.getString("mot_video", "Nomot_video");
        sharedValues[1] = this.sessionId = prefs.getString("sessionId", "NosessionId");


        return sharedValues;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public Boolean isAuthenticated(Context context) {

        if (this.sessionId == null || this.mot_video == null || this.sessionId.isEmpty() || this.mot_video.isEmpty() ||
                this.sessionId.equals("NoID") || this.mot_video.equals("Nomot_video")) {
            Intent intent = null;
            intent = new Intent(context.getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return false;
        }
        return true;
    }

    public Boolean isApplicationExit(Context context) {

        getSession(context);
        System.out.println("====" + this.sessionId + "UN" + this.mot_video);
        if (this.mot_video == null || this.mot_video.isEmpty() ||
                this.mot_video.equals("Nomot_video")) {
            return false;

        }
        System.out.println("====" + this.sessionId + "UN--true" + this.mot_video);
        return true;
    }


    public String getMot_video(Context context) {
        getSession(context);
        return mot_video;
    }

    public void setMot_video(String mot_video) {
        this.mot_video = mot_video;
    }


    public String getSessionId(Context context) {
        getSession(context);
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}

