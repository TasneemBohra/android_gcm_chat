package com.desidime.chat;

/**
 * Created by tasneem on 12/1/16.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Joe on 6/1/2014.
 */
public class MessageSender {
    private static final String TAG = "MessageSender";
    AsyncTask sendTask;
    AtomicInteger ccsMsgId = new AtomicInteger();

    public void sendMessage(final Bundle data, final GoogleCloudMessaging gcm) {

        sendTask = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                sendTask = null;
                Log.d(TAG, "onPostExecute: result: " + o);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String id = Integer.toString(ccsMsgId.incrementAndGet());

                try {
                    Log.d(TAG, "messageid: " + id);
                    gcm.send("559461873841@gcm.googleapis.com", id, data);
                    Log.d(TAG, "After gcm.send successful.");
                } catch (IOException e) {
                    Log.d(TAG, "Exception: " + e);
                    e.printStackTrace();
                }
                return "Message ID: " + id + " Sent.";
            }
        };
        sendTask.execute(null, null, null);
    }
}

