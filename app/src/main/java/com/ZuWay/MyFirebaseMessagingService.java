package com.ZuWay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.transition.Transition;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ZuWay.activity.HomeActivity;
import com.ZuWay.activity.ItemActivity;
import com.ZuWay.activity.NotificationActivity;
import com.ZuWay.utils.NotificationONOff;
import com.bumptech.glide.request.target.CustomTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "ZuWay",pid,scid,cid;
    Bitmap bitmap;
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        // TODO(developer): Handle FCM messages here.
        String notification = NotificationONOff.getInstance().getType(getApplicationContext());

        if (notification.equalsIgnoreCase("1")) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData().toString());
                JSONObject obj2 = new JSONObject(obj.getJSONObject("body").toString());
                String value = obj2.getString("name") + System.getProperty("line.separator") + System.getProperty("line.separator") + obj2.getString("offer");
                String image = obj2.getString("status");
                pid = obj2.getString("pid");
                scid = obj2.getString("scid");
                cid = obj2.getString("cid");
                bitmap = getBitmapFromURL(image);
                sendNotification(value);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                if (/* Check if data needs to be processed by long running job */ true) {

                } else {
                }

                if (remoteMessage.getNotification() != null) {
                    Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                    sendNotification(remoteMessage.getNotification().getBody());

                    try {
                        JSONObject obj = new JSONObject(remoteMessage.getData().toString());
                        JSONObject obj2 = new JSONObject(obj.getJSONObject("body").toString());
                        sendNotification(obj2.getString("title"));

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                Log.e(TAG, "ReactFireBaseMessagingService: Notifications Are Disabled by User");

            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        AppPreferences objAppPreferences = new AppPreferences(this);
        objAppPreferences.Set_FCMToken(token);
    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("pid",pid);
        intent.putExtra("cid",cid);
        intent.putExtra("scid",scid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_big)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setLargeIcon(bitmap)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                             .bigPicture(bitmap))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        notificationBuilder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}