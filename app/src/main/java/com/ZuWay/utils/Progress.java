package com.ZuWay.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;

import com.ZuWay.R;

import dmax.dialog.SpotsDialog;


public class Progress {
    private Progress(){

    }

    private static Progress bProgress;

    public static Progress getInstance() {
        if (bProgress == null) {
            synchronized (Progress.class) {
                bProgress = new Progress();
            }
        }
        return bProgress;
    }

    private AlertDialog dialog;

    public void show(Context context, String message) {
        try {
           // dialog=new SpotsDialog(context,R.style.Custom);
           // dialog = new SpotsDialog(context, R.style.Custom);
            dialog.setMessage(message);
            dialog.show();
        }catch (Exception e){}
    }



    public void hide(Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    }catch (Exception e){}

                }
            }
        }, 1000);
    }

    public boolean isDialogShowing(Context context) {
        try {
            if (dialog.isShowing()) {
                return true;
            }
        }catch (Exception e){}
        return false;
    }

    public void setMessage(String message) {
        try {
            dialog.setMessage(message);
        }catch (Exception e){}
    }

}
