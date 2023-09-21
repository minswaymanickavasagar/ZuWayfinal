package com.ZuWay.utils;

import android.content.Context;
import android.content.SharedPreferences;



/*import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;*/

/**
 * Created by Senthamil Selvan on 24-Apr-2019.
 */
public class BSessionType {
    public BSessionType() {

    }

    public static BSessionType bSession;

    public static BSessionType getInstance() {

        if (bSession == null) {
            synchronized (BSessionType.class) {
                bSession = new BSessionType();
            }
        }
        return bSession;
    }

    private static String TAG = BSessionType.class.getSimpleName();
    SharedPreferences.Editor editor;
    Context _context;
    SharedPreferences prefs;
    private String type;
    private String sessionId;



    public void initialize(Context context,
                           String type) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("type", type);
        editor.putString("sessionId", sessionId);
        editor.apply();
        this.type      = type;
        this.sessionId = sessionId;


    }


    public void destroy(Context context) {

        SharedPreferences sharedpreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit().clear();
        editor.apply();
        this.type      = null;
        this.sessionId = null;



    }

    public String[] getSession(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String[] sharedValues = new String[24];

        sharedValues[0] = this.type = prefs.getString("type", "Notype");
        sharedValues[1] = this.sessionId = prefs.getString("sessionId", "NosessionId");

           return sharedValues;
    }

    public String getKey(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SaveLife", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "noKey");
        return key;

    }

    public String getType(Context context) {
        getSession(context);
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}

