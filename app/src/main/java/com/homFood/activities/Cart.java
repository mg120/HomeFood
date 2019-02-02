package com.homFood.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.homFood.R;
import com.homFood.adapter.CardAdapter;
import com.homFood.model.CardModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Cart extends AppCompatActivity implements View.OnClickListener {

    LayoutAnimationController controller;
    ProgressBar progressBar;
    RecyclerView cart_recycler;
    CardAdapter adapter;
    public static LinearLayout cart_layout, empty_layout;
    public static TextView total_txtV;
    Button order_now_btn;
    private ArrayList<CardModel> list = new ArrayList<>();
    public static float total = 0.0f;
    String newDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView back = findViewById(R.id.cart_back);
        cart_recycler = findViewById(R.id.cart_recycler_id);
        progressBar = findViewById(R.id.card_progress);
        cart_layout = findViewById(R.id.cart_layout_id);
        empty_layout = findViewById(R.id.empty_cart);
        cart_recycler = findViewById(R.id.cart_recycler_id);
        order_now_btn = findViewById(R.id.btn_orderNow);
        total_txtV = findViewById(R.id.total_card_price_id);

        cartData(MainActivity.customer_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        order_now_btn.setOnClickListener(this);
    }

    private void cartData(final String id) {
        total = 0.0f;
        list.clear();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest card_request = new StringRequest(Request.Method.POST, Urls.card_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    total = 0.0f;
                    progressBar.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String order_id = jsonObject.getString("Order_id");
                            String seller_id = jsonObject.getString("Seller_id");
                            String Product_Name = jsonObject.getString("Product_Name");
                            String Quantity = jsonObject.getString("Quantity");
                            String Price = jsonObject.getString("Price");
                            String type = jsonObject.getString("type");
                            String Product_img = jsonObject.getString("Product_img");

                            total += Float.parseFloat(Price) * Integer.parseInt(Quantity);
                            list.add(new CardModel(seller_id, order_id, Product_img, Product_Name, Price, Quantity));
                        }
                    }

                    Gson gson = new Gson();
                    String json_obj = gson.toJson(list);
                    Log.e("obj: ", json_obj);

                    if (!list.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.GONE);
                        cart_layout.setVisibility(View.VISIBLE);

                        cart_recycler.setHasFixedSize(true);
                        cart_recycler.setLayoutManager(new LinearLayoutManager(Cart.this));
                        adapter = new CardAdapter(Cart.this, list);
                        cart_recycler.setAdapter(adapter);
                        total_txtV.setText(total + "");

                    } else {
                        progressBar.setVisibility(View.GONE);
                        cart_layout.setVisibility(View.GONE);
                        empty_layout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", id);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(card_request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_orderNow:
                Intent intent = new Intent(Cart.this, ShippingAddress.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) CardAdapter.cart_list);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }
}
