package com.desidime.chat;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserListActivity extends AppCompatActivity {
    private static final String TAG = "UserListActivity";
    Button refreshButton;
    private Intent intent;
    MessageSender messageSender;
    GoogleCloudMessaging gcm;
    private  ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setToolbar();
        refreshButton = (Button)findViewById(R.id.refreshButton);
        mList = (ListView)findViewById(R.id.listView);
        intent = new Intent(this, GCMNotificationIntentService.class);
        messageSender = new MessageSender();
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        Log.d(TAG, "GCM: "+gcm);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // get user list
                Bundle dataBundle = new Bundle();
                dataBundle.putString("ACTION", "USERLIST");
                messageSender.sendMessage(dataBundle, gcm);
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue    = (String)parent.getItemAtPosition(position);


                Intent i = new Intent(getApplicationContext(),
                        ChatActivity.class);
                i.putExtra("TOUSER",itemValue);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,
                new IntentFilter("com.desidime.chat.userlist"));

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent.getStringExtra("USERLIST"));
            updateUI(intent.getStringExtra("USERLIST"));
        }
    };

    private void updateUI(String userList) {
        //get userlist from the intents and update the list

        String[] userListArr = userList.split(":");
        Log.d(TAG,"userListArr: "+userListArr.length+" tostr "+userListArr.toString());
        //remove empty strings :-)
        List<String> list = new ArrayList();
        for(String s : userListArr) {
            if(s != null && s.length() > 0) {
                list.add(s);
            }
        }
        userListArr = list.toArray(new String[list.size()]);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userListArr);
        mList.setAdapter(adapter);

    }



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
