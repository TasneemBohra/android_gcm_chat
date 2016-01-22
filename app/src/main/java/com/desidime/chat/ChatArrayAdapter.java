package com.desidime.chat;

/**
 * Created by tasneem on 12/1/16.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatArrayAdapter extends ArrayAdapter {

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList();
    private LinearLayout singleMessageContainer;

    @Override
    public void add(Object object) {
        chatMessageList.add((ChatMessage) object);
    }

    public   ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        ChatMessage chatMessageObj = getItem(position);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
        chatText.setText(chatMessageObj.message);
        chatText.setBackgroundResource(chatMessageObj.right ? R.drawable.bubble_other : R.drawable.bubble_me);
        singleMessageContainer.setGravity(chatMessageObj.right ? Gravity.RIGHT : Gravity.LEFT);
        /*if (chatMessageObj.right) {
            chatText.setPadding(R.dimen.padding_10, R.dimen.padding_20, R.dimen.padding_10, R.dimen.padding_10);
        } else {
            chatText.setPadding(R.dimen.padding_10, R.dimen.padding_10, R.dimen.padding_10, R.dimen.padding_20);

        }*/
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}