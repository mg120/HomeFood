package com.homFood.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.ActiveAdapter;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderedFromMe extends AppCompatActivity {

    SwipeRefreshLayout swipe_layout;
    private RecyclerView recyclerView;
    private ActiveAdapter adapter;
    TextView no_orders_txt;
    ProgressBar progressBar;
    LayoutAnimationController controller;

    ArrayList<OrdersModel> data_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_from_me);

        findViewById(R.id.ordered_fromMe_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipe_layout = findViewById(R.id.fromM_swipeRefreshLayout);
        no_orders_txt = findViewById(R.id.ordered_fromMe_check_txt);
        recyclerView = findViewById(R.id.orders_fromMe_recycler);
        progressBar = findViewById(R.id.from_Me_progress_id);

        recyclerView.setLayoutManager(new LinearLayoutManager(OrderedFromMe.this));
        recyclerView.setHasFixedSize(true);

        ordered_fromMe(MainActivity.customer_id);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ordered_fromMe(MainActivity.customer_id);
                        swipe_layout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        swipe_layout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void ordered_fromMe(final String customer_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.fromMe_orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    data_list.clear();
                    System.out.println("response:: " + response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String Order_id = jsonObject.getString("Order_id");
                            String Product_Name = jsonObject.getString("Product_Name");
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

//                        if (state_type.equals("0") || state_type.equals("1")) {
                            data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                    Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                        } else if (state_type.equals("2") || state_type.equals("3") || state_type.equals("4")) {
//                            finished_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
//                                    Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                        }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    if (data_list.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        no_orders_txt.setVisibility(View.GONE);

                        recyclerView.addItemDecoration(new DividerItemDecoration(OrderedFromMe.this, DividerItemDecoration.VERTICAL));
                        adapter = new ActiveAdapter(OrderedFromMe.this, data_list);
                        recyclerView.setAdapter(adapter);
                        // Set Animation to Recyceler ...
                        controller = AnimationUtils.loadLayoutAnimation(OrderedFromMe.this, R.anim.layout_down_top);
                        recyclerView.setLayoutAnimation(controller);
                        recyclerView.scheduleLayoutAnimation();
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        no_orders_txt.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", customer_id);
                System.out.println("Params:: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
