package com.desidime.chat;

/**
 * Created by tasneem on 12/1/16.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GcmReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joe on 5/28/2014.
 */
public class GCMNotificationIntentService extends GcmListenerService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public static final String TAG = "GCMIntent";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        Log.d(TAG, "from: " + from);
        Log.d(TAG, "data: "+data);

        if (data != null) {
            if (!data.isEmpty()) {

                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(data.get("message_type"))) {
                        sendNotification("Send error: " + data.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(data.get("message_type"))) {
                        sendNotification("Deleted messages on server: " + data.toString());
                    } else if ("USERLIST".equals(data.getString("SM"))) {
                           Log.d(TAG, "onHandleIntent - USERLIST ");
                           //update the userlist view
                           Intent userListIntent = new Intent("com.desidime.chat.userlist");
                           String userList = data.get("USERLIST").toString();
                           userListIntent.putExtra("USERLIST", userList);
                           sendBroadcast(userListIntent);
                       } else if ("CHAT".equals(data.get("SM"))) {
                           Log.d(TAG, "onHandleIntent - CHAT ");
                           Intent chatIntent = new Intent("com.desidime.chat.chatmessage");
                           chatIntent.putExtra("CHATMESSAGE", data.get("CHATMESSAGE").toString());
                           sendBroadcast(chatIntent);
                       }
                       Log.i(TAG, "SERVER_MESSAGE: " + data.toString());


            }
        }

    }

    private void sendNotification(String msg) {
        Log.d(TAG, "Preparing to send: " + msg);
        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SignUpActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.bubble_b)
                .setContentTitle("GCM XMPP Message")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Sent successful.");
    }




}
