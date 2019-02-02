package com.homFood.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.ChatListAdapter;
import com.homFood.model.ChatModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Messages extends AppCompatActivity {

    private TextView back, chat_title;
    private ListView messages_list;
    private EditText message_text;
    private FloatingActionButton send_btn;
    private String recive_name;
    ChatListAdapter adapter;
    ArrayList<ChatModel> chat_msgs = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    private String sender_id, receiver_id, sender_name, receiver_name;

    ArrayList<ChatModel> chat_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            sender_id = intent.getStringExtra("sender_id");
            receiver_id = intent.getStringExtra("receiver_id");
            sender_name = intent.getStringExtra("sender_name");
            receiver_name = intent.getStringExtra("receiver_name");

            getLastChat(sender_id, receiver_id);
        }
        chat_title = findViewById(R.id.chat_title);
        chat_title.setText(receiver_name);
        back = findViewById(R.id.back);
        messages_list = findViewById(R.id.list_of_chatmessages);
        adapter = new ChatListAdapter(Messages.this, chat_list);
        messages_list.setAdapter(adapter);
        message_text = findViewById(R.id.input);
        send_btn = findViewById(R.id.fab);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        try {
//            Badges.removeBadge(this);
//            // Alternative way
//            Badges.setBadge(this, 0);
//        } catch (BadgesNotSupportedException badgesNotSupportedException) {
//            Log.d(TAG, badgesNotSupportedException.getMessage());
//        }

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = message_text.getText().toString().trim();
                if (!TextUtils.isEmpty(message) && !message.matches("")) {
                    SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());
                    message_text.getText().clear();
                    chat_list.add(new ChatModel(MainActivity.customer_id, MainActivity.Name, message, date + ""));

                    adapter.notifyDataSetChanged();
                    messages_list.setAdapter(adapter);
                    StringRequest chat_request = new StringRequest(Request.Method.POST, Urls.chat_request_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String noti = jsonObject.getString("noti");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Messages.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("sender_id", MainActivity.customer_id);
                            params.put("message", message);
                            params.put("sender_name", MainActivity.Name);
                            params.put("reciver_name", receiver_name);
                            params.put("reciver_id", receiver_id);
                            Log.d("sender_id: ", MainActivity.customer_id);
                            Log.d("message:", message);
                            Log.d("sender_name:", MainActivity.Name);
                            Log.d("reciver_name:", receiver_name);
                            Log.d("reciver_id:", receiver_id);
                            return params;
                        }
                    };
                    Volley.newRequestQueue(Messages.this).add(chat_request);
                } else {
                    Toast.makeText(Messages.this, "ادخل رسالتك اولا...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLastChat(final String customer_id, final String family_id) {
        StringRequest open_chat_request = new StringRequest(Request.Method.POST, Urls.open_chat_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                chat_list.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("chat");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data_obj = jsonArray.getJSONObject(i);
                        String chat_id = data_obj.getString("chat_id");
                        String sender_id = data_obj.getString("sender_id");
                        sender_name = data_obj.getString("sender_name");
                        String reciver_id = data_obj.getString("reciver_id");
                        recive_name = data_obj.getString("recive_name");
                        String message = data_obj.getString("message");
                        String send_at_time = data_obj.getString("send_at");

                        chat_list.add(new ChatModel(sender_id, sender_name, message, send_at_time));
                    }
                    if (chat_list != null) {
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Messages.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender_id", customer_id);
                params.put("reciver_id", family_id);
                return params;
            }
        };
        Volley.newRequestQueue(Messages.this).add(open_chat_request);
    }
}
