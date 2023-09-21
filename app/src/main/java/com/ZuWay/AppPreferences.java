package com.ZuWay;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Android on 10/13/2017.
 */

public class AppPreferences {

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mPrefsEditor;
    private Context mContext;
    private long Dayinsec = (1000 * 60 * 60 * 12);

    public AppPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPrefsEditor = mSharedPrefs.edit();
    }

    public void Set_FCMToken(String FCMToken) {
        mPrefsEditor.putString("FCMToken", FCMToken);
        mPrefsEditor.commit();
    }

    public String Get_FCMToken() {
        return mSharedPrefs.getString("FCMToken", "");
    }

    public void Set_Mobile(String Mobile) {
        mPrefsEditor.putString("mobile", Mobile);
        mPrefsEditor.commit();
    }

    public String Get_Mobile() {
        return mSharedPrefs.getString("mobile", "");
    }


    public void Set_completed(String completed) {
        mPrefsEditor.putString("completed", completed);
        mPrefsEditor.commit();
    }

    public String Get_completed() {
        return mSharedPrefs.getString("completed", "");
    }


    public void Set_swipe(String completed) {
        mPrefsEditor.putString("swipe", completed);
        mPrefsEditor.commit();
    }

    public String Get_swipe() {
        return mSharedPrefs.getString("swipe", "");
    }

    public void Set_TimelyDeliveryBoysPrivacy(boolean TimelyDeliveryBoysPrivacy) {
        mPrefsEditor.putBoolean("TimelyDeliveryBoysPrivacy", TimelyDeliveryBoysPrivacy);
        mPrefsEditor.commit();
    }

    public boolean Get_TimelyDeliveryBoysPrivacy() {
        return mSharedPrefs.getBoolean("TimelyDeliveryBoysPrivacy", false);
    }


    public void Set_TimelyDeliveryBoysupdatetime() {
        mPrefsEditor.putString("TimelyDeliveryBoysupdatetime", new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
        mPrefsEditor.commit();
    }

    public boolean Get_TimelyDeliveryBoysupdatetime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String Getdate = mSharedPrefs.getString("TimelyDeliveryBoysupdatetime", "");
        if (Getdate != null && Getdate != "") {
            try {
                Date date1 = simpleDateFormat.parse(Getdate);
                long value = CheckDifference(date1);
                if (value < Dayinsec) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void Set_Socialvalues(String likevalues) {
        mPrefsEditor.putString("Socialvalues", likevalues);
        mPrefsEditor.commit();
    }

    public String Get_Socialvalues() {
        return mSharedPrefs.getString("Socialvalues", "");
    }

    public void Set_Socialupdatetime() {
        mPrefsEditor.putString("Socialupdatetime", new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
        mPrefsEditor.commit();
    }

    public boolean Get_Socialupdatetime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String Getdate = mSharedPrefs.getString("Socialupdatetime", "");
        if (Getdate != null && Getdate != "") {
            try {
                Date date1 = simpleDateFormat.parse(Getdate);
                long value = CheckDifference(date1);
                if (value < Dayinsec) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public long CheckDifference(Date startDate) {
        long different = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date endDate = simpleDateFormat.parse(new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
            //milliseconds
            different = endDate.getTime() - startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return different;
    }
    public void Set_Versionupdatetime() {
        mPrefsEditor.putString("Versionupdatetime", new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()));
        mPrefsEditor.commit();
    }

    public boolean Get_Versionupdatetime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String Getdate = mSharedPrefs.getString("Versionupdatetime", "");
        if (Getdate != null && Getdate != "") {
            try {
                Date date1 = simpleDateFormat.parse(Getdate);
                long value = CheckDifference(date1);
                if (value < Dayinsec) {
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
