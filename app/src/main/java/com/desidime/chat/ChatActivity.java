package com.desidime.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

public class ChatActivity extends AppCompatActivity {

        private static final String TAG = "ChatActivity";

        private ChatArrayAdapter chatArrayAdapter;
        private ListView listView;
        private EditText chatText;
        private Button buttonSend;

        GoogleCloudMessaging gcm;
        Intent intent;

        private static Random random;
        private String toUserName;
        MessageSender messageSender;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent i = getIntent();
            toUserName = i.getStringExtra("TOUSER");
            setContentView(R.layout.activity_chat);

            setToolbar();

            buttonSend = (Button) findViewById(R.id.buttonSend);
            intent = new Intent(this, GCMNotificationIntentService.class);

            random = new Random();
            messageSender = new MessageSender();
            listView = (ListView) findViewById(R.id.listView1);
            gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

            chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
            listView.setAdapter(chatArrayAdapter);

            chatText = (EditText) findViewById(R.id.chatText);
            chatText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        return sendChatMessage();
                    }
                    return false;
                }
            });
            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    sendChatMessage();
                }
            });

            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            listView.setAdapter(chatArrayAdapter);

            chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    listView.setSelection(chatArrayAdapter.getCount() - 1);
                }
            });
        }

    private boolean sendChatMessage(){
        //sending gcm message to the paired device
        Bundle dataBundle = new Bundle();
        dataBundle.putString("ACTION", "CHAT");
        dataBundle.putString("TOUSER", toUserName);
        dataBundle.putString("CHATMESSAGE", chatText.getText().toString());
        messageSender.sendMessage(dataBundle, gcm);

        //updating the current device
        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));
        chatText.setText("");
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter("com.desidime.chat.chatmessage"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getStringExtra("CHATMESSAGE"));
            chatArrayAdapter.add(new ChatMessage(true, intent.getStringExtra("CHATMESSAGE")));
            chatArrayAdapter.notifyDataSetChanged();
        }
    };


    /**
     * setting toolbar
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
    }
}
