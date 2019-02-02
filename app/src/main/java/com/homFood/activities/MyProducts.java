package com.homFood.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.homFood.adapter.MyProductsAdapter;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProducts extends AppCompatActivity {

    ArrayList<ProdImagesModel> osra_images = new ArrayList<>();
    ArrayList<HomeModel> osra_data = new ArrayList<>();
    private MyProductsAdapter adapter;

    public static RecyclerView products_recycler;
    ProgressBar progressBar;
    public static TextView back, no_products_data;
    SwipeRefreshLayout swipe_layout;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        back = findViewById(R.id.myProducts_back);
        progressBar = findViewById(R.id.myProducts_progress_id);
        no_products_data = findViewById(R.id.no_products_data_txt_id);
        products_recycler = findViewById(R.id.myProducts_recyclerView_id);
        swipe_layout = findViewById(R.id.myProducts_swipeRefreshLayout);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        controller = AnimationUtils.loadLayoutAnimation(MyProducts.this, R.anim.layout_down_top);
        products_recycler.setHasFixedSize(true);
        products_recycler.setLayoutManager(layoutManager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get Data ...
        my_Products_Data(MainActivity.customer_id);

        // For Swipe Refresh ...
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        my_Products_Data(MainActivity.customer_id);
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

    private void my_Products_Data(final String id) {
        StringRequest products_request = new StringRequest(Request.Method.POST, Urls.products_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    osra_data.clear();
                    osra_images.clear();
                    System.out.println("response:: " + response);
                    Log.e("res:: ", response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String product_id = jsonObject.getString("Product_ID");
                        String name = jsonObject.getString("Name");
                        String price = jsonObject.getString("Price");
                        String rate = jsonObject.getString("rate");
                        String desc = jsonObject.getString("Description");
                        String time = jsonObject.getString("Timeee");
                        String img1 = jsonObject.getString("img");
                        String img2 = jsonObject.getString("img2");
                        String img3 = jsonObject.getString("img3");
                        String img4 = jsonObject.getString("img4");
                        String img5 = jsonObject.getString("img5");
                        String FoodType = jsonObject.getString("FoodType");
                        String customer_id = jsonObject.getString("Customer_id");
                        String customer_name = jsonObject.getString("customer_name");
                        String customer_mail = jsonObject.getString("customer_mail");

                        osra_images.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                        osra_data.add(new HomeModel(img1, product_id, name, FoodType, rate, price, desc, time, customer_id, customer_name, customer_mail));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (osra_data.size() > 0) {
                    progressBar.setVisibility(View.GONE);
                    no_products_data.setVisibility(View.GONE);
                    products_recycler.setVisibility(View.VISIBLE);
                    adapter = new MyProductsAdapter(MyProducts.this, osra_data, osra_images);
                    products_recycler.setAdapter(adapter);
                    // Set Animation to Recyceler ...
//                    products_recycler.setLayoutAnimation(controller);
//                    products_recycler.getAdapter().notifyDataSetChanged();
//                    products_recycler.scheduleLayoutAnimation();
                } else {
                    progressBar.setVisibility(View.GONE);
                    products_recycler.setVisibility(View.GONE);
                    no_products_data.setVisibility(View.VISIBLE);
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
                params.put("Customer_id", id);
                System.out.println("params: " + params);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(products_request);
    }
}