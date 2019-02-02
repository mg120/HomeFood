package com.homFood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.homFood.activities.MainActivity;
import com.homFood.R;
import com.homFood.model.ChatModel;
import java.util.ArrayList;

/**
 * Created by Ma7MouD on 5/27/2018.
 */

public class ChatListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ChatModel> list;

    public ChatListAdapter(Context context, ArrayList<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (list != null && list.get(i).getSender_id().equals(MainActivity.customer_id)) {
            view = LayoutInflater.from(context).inflate(R.layout.right_chat_bubble, viewGroup, false);
            TextView textView = view.findViewById(R.id.txt_msg);
            textView.setText(list.get(i).getMessage() + "\n" + list.get(i).getMeddage_time());
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.left_chat_bubble, viewGroup, false);
            TextView textView = view.findViewById(R.id.txt_msg);
            textView.setText(list.get(i).getMessage() + "\n" + list.get(i).getMeddage_time());
        }

        return view;
    }
}
