package com.homFood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    private ArrayList<OrdersModel> data_list = new ArrayList<>();
    RecyclerView orders_recycler;
    ProgressBar progressBar;
    TextView back, no_orders_data;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        findViewById(R.id.orders_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar = findViewById(R.id.myOrders_progress_id);
        no_orders_data = findViewById(R.id.no_orders_data_txt_id);
        orders_recycler = findViewById(R.id.myOrders_recyclerView_id);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        controller = AnimationUtils.loadLayoutAnimation(OrderDetails.this, R.anim.layout_down_top);
        orders_recycler.setHasFixedSize(true);
        orders_recycler.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);

        my_Orders_Data(MainActivity.customer_id);
    }

    private void my_Orders_Data(final String id) {
        StringRequest orders_request = new StringRequest(Request.Method.POST, Urls.orders_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    data_list.clear();
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

//                            if (state_type.equals("3") || state_type.equals("4")) {
//                                finished_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
//                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                            }
//                            else if (state_type.equals("0") || state_type.equals("1")) {
                            data_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
                                    Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                            }
//                            else if (state_type.equals("2")) {
//                                completed_list.add(new OrdersModel(Product_img, Product_Name, foodType, Price, Quantity, Order_id, state_type, Seller_id,
//                                        Seller_name, Seller_mail, Customer_id, Customer_name, Customer_mail, Customer_phone));
//                            }
                        }

                        if (data_list.size() > 0) {
                            progressBar.setVisibility(View.GONE);
                            no_orders_data.setVisibility(View.GONE);
                            orders_recycler.setVisibility(View.VISIBLE);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            no_orders_data.setVisibility(View.VISIBLE);
                            orders_recycler.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetails.this, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", id);
                return params;
            }
        };
      Volley.newRequestQueue(OrderDetails.this).add(orders_request);
    }
}
