package com.homFood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.OrdersAdapter;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Orders extends AppCompatActivity {

    RecyclerView orders_recycler;
    ProgressBar progressBar;
    TextView back, no_orders_data;
    LayoutAnimationController controller;
    OrdersAdapter adapter;

    private ArrayList<OrdersModel> data_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        findViewById(R.id.orders_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        no_orders_data = findViewById(R.id.no_orders_data_txt_id);
        progressBar = findViewById(R.id.myOrders_progress_id);
        orders_recycler = findViewById(R.id.myOrders_recyclerView_id);
        orders_recycler.setLayoutManager(new LinearLayoutManager(this));
        orders_recycler.setHasFixedSize(true);

        getMyOrders(MainActivity.customer_id);
    }

    private void getMyOrders(final String customer_id) {
        StringRequest orders_request = new StringRequest(Request.Method.POST, Urls.orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response:: " + response);
                Log.e("res:", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        String Order_id = jsonObject.getString("Order_id");
                        String Product_Name = jsonObject.getString("Product_name");
                        String Product_img = jsonObject.getString("Product_img");
                        String foodType = jsonObject.getString("FoodType");
                        String Quantity = jsonObject.getString("Quantity");
                        String Price = jsonObject.getString("Price");
                        String Seller_id = jsonObject.getString("Seller_id");
                        String Seller_name = jsonObject.getString("Seller_name");
                        String Seller_mail = jsonObject.getString("Seller_mail");
                        String Customer_id = jsonObject.getString("Customer_id");
                        String Customer_name = jsonObject.getString("Customer_name");
                        String Customer_mail = jsonObject.getString("Customer_mail");
                        String Customer_phone = jsonObject.getString("Customer_phone");
                        String state_type = jsonObject.getString("type");

                        data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
                    }

                    if (data_list.size() > 0) {
                        orders_recycler.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        no_orders_data.setVisibility(View.GONE);
                        adapter = new OrdersAdapter(Orders.this, data_list);
                        orders_recycler.setAdapter(adapter);
                        // Set Animation to Recyceler ...
                        controller = AnimationUtils.loadLayoutAnimation(Orders.this, R.anim.layout_down_top);
                        orders_recycler.setLayoutAnimation(controller);
                        orders_recycler.scheduleLayoutAnimation();
                    } else {
                        orders_recycler.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        no_orders_data.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", customer_id);
                System.out.println("params:: " + params);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Orders.this);
        queue.add(orders_request);
    }

}
