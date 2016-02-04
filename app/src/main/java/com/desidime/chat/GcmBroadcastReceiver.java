package com.desidime.chat;

/**
 * Created by tasneem on 12/1/16.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GcmReceiver;

/**
 * Created by Joe on 5/28/2014.
 */
public class GcmBroadcastReceiver extends GcmReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("GcmBroadcastReceiver", "onReceive: notification received.");
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}