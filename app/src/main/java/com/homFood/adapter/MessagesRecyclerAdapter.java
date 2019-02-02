package com.homFood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homFood.activities.MainActivity;
import com.homFood.R;
import com.homFood.activities.Messages;
import com.homFood.model.ChatMessagesModel;
import com.homFood.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma7MouD on 8/15/2018.
 */

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    Context context;
    List<ChatMessagesModel> list;
    private String open_chat_url = "http://cookehome.com/CookApp/getChat.php";
    public static ArrayList<ChatModel> chat_list = new ArrayList<>();

    public MessagesRecyclerAdapter(Context context, List<ChatMessagesModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MessagesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesRecyclerAdapter.ViewHolder holder, int position) {
        holder.item_name.setText(list.get(position).getRecive_name());
        holder.item_description.setText(list.get(position).getMessage());
        holder.item_time.setText(list.get(position).getDate() + "");
        holder.viewForeground.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(ChatMessagesModel item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView item_name, item_description;
        public TextView item_time;
        public LinearLayout viewForeground;

        public ViewHolder(View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name_txtv);
            item_description = itemView.findViewById(R.id.item_desc_txtv);
            item_time = itemView.findViewById(R.id.item_price_txtview);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            viewForeground.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            String sender_id = list.get(position).getSender_id();
            String receiver_id = list.get(position).getRecive_id();
            String sender_name = list.get(position).getSender_name();
            String receiver_name = list.get(position).getRecive_name();
            Family_chat(sender_id, receiver_id, sender_name, receiver_name);
        }
    }

    private void Family_chat(final String sender_id, final String receiver_id, final String sender_name, final String receiver_name) {
        if (!MainActivity.customer_id.equals("")) {
            if (sender_id.equals(MainActivity.customer_id)) {
                Intent intent1 = new Intent(context, Messages.class);
                intent1.putExtra("sender_id", sender_id);
                intent1.putExtra("receiver_id", receiver_id);
                intent1.putExtra("sender_name", sender_name);
                intent1.putExtra("receiver_name", receiver_name);
                context.startActivity(intent1);
            } else {
                Intent intent1 = new Intent(context, Messages.class);
                intent1.putExtra("sender_id", receiver_id);
                intent1.putExtra("receiver_id", sender_id);
                intent1.putExtra("sender_name", receiver_name);
                intent1.putExtra("receiver_name", sender_name);
                context.startActivity(intent1);
            }
        } else {
            Toast.makeText(context, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
        }
    }
}