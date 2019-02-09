package com.homFood.activities;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.homFood.R;
import com.homFood.adapter.CardAdapter;
import com.homFood.model.CardModel;
import com.homFood.networking.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CheckCoupon extends AppCompatActivity implements View.OnClickListener {

    EditText coupon_code_ed;
    Button send_code, exit_page;

    String address, name, email, date, code;
    double lat, lan;
    int total_quantity;
    float total_price;
    ArrayList<CardModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_coupon);

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            address = intent.getStringExtra("address");
            lat = intent.getDoubleExtra("lat", 0.0);
            lan = intent.getDoubleExtra("lan", 0.0);
            list = intent.getExtras().getParcelableArrayList("card_list");

            System.out.println("list:: " + list);

            Gson gson = new Gson();
            String listtt = gson.toJson(list);

            Log.d("address: ", address + "");
            Log.d("lat: ", lat + "");
            Log.d("lan: ", lan + "");
            Log.d("list:: ", listtt);
        }
        coupon_code_ed = findViewById(R.id.coupon_code_ed_id);
        send_code = findViewById(R.id.send_coupon_code_btn_id);
        exit_page = findViewById(R.id.exit_btn_id);

        send_code.setOnClickListener(this);
        exit_page.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send_coupon_code_btn_id:
                // send Coupon Code ...
                StringRequest send_email = new StringRequest(Request.Method.POST, Urls.check_coupon, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("respone: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {

                                String discout_val = jsonObject.getString("Discount_value");
                                String dis_prod_id = jsonObject.getString("Product_ID");

                                Intent intent = new Intent(CheckCoupon.this, CheckOut.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("dis_value", discout_val);
                                bundle.putString("dis_prod_id", dis_prod_id);
                                bundle.putString("name", name);
                                bundle.putString("email", email);
                                bundle.putString("address", address);
                                bundle.putDouble("lat", lat);
                                bundle.putDouble("lan", lan);
                                bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) CardAdapter.cart_list);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                Toasty.success(CheckCoupon.this, "تم إرسال الكود بنجاح!", 1500).show();
                                finish();
                            } else {
                                Toasty.error(CheckCoupon.this, "هذا الكود غير موجود ,برجاء التحقق من الكود المرسل!", 1500).show();
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
                        params.put("Code", coupon_code_ed.getText().toString().trim());
                        System.out.println("Params: " + params);
                        return params;
                    }
                };
                int socketTimeout = 60000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                send_email.setRetryPolicy(policy);

                Volley.newRequestQueue(CheckCoupon.this).add(send_email);
                break;

            case R.id.exit_btn_id:
                Intent intent = new Intent(CheckCoupon.this, CheckOut.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("address", address);
                bundle.putDouble("lat", lat);
                bundle.putDouble("lan", lan);
                bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) CardAdapter.cart_list);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
        }
    }
}
