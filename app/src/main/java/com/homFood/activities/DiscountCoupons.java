package com.homFood.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.CouponsAdapter;
import com.homFood.model.CouponsModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiscountCoupons extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton create_btn;
    TextView no_coupons;
    ProgressBar progressBar;

    ArrayList<CouponsModel> list = new ArrayList<>();
    CouponsAdapter adapter;
    SwipeRefreshLayout swipe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_coupons);

        findViewById(R.id.dis_coupons_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.coupons_recycler_id);
        create_btn = findViewById(R.id.fab_create_btn_id);
        no_coupons = findViewById(R.id.no_coupons_txt_id);
        progressBar = findViewById(R.id.coupons_progress_id);
        swipe_layout = findViewById(R.id.coupons_refresh_layout_id);


        getCouponsData(MainActivity.customer_id);

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscountCoupons.this, CreateCouponActivity.class);
                startActivity(intent);
            }
        });

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCouponsData(MainActivity.customer_id);
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

    private void getCouponsData(final String customer_id) {
        StringRequest coupons_request = new StringRequest(Request.Method.POST, Urls.discount_coupons_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.clear();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String prod_id = jsonObject.getString("Product_ID");
                        String discount_val = jsonObject.getString("Discount_value");
                        String code = jsonObject.getString("Value");
                        String expire_date = jsonObject.getString("Expiry_date");

                        list.add(new CouponsModel(prod_id, discount_val, code, expire_date));
                    }
                    if (list.size() > 0) {
                        no_coupons.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DiscountCoupons.this));
                        recyclerView.setHasFixedSize(true);
                        adapter = new CouponsAdapter(DiscountCoupons.this, list);
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        no_coupons.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);

                    recyclerView.addItemDecoration(new DividerItemDecoration(DiscountCoupons.this, DividerItemDecoration.VERTICAL));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("response:: " + response);
                Log.e("ress: ", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(DiscountCoupons.this, getString(R.string.time_out), Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError)
                    Toast.makeText(DiscountCoupons.this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                else if (error instanceof ServerError)
                    Toast.makeText(DiscountCoupons.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(DiscountCoupons.this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("User_ID", customer_id);
                System.out.println("params:: " + params);
                return params;
            }
        };

        System.out.println("url : " + coupons_request.getUrl());
        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        coupons_request.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(coupons_request);
    }
}
