package com.homFood.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.homFood.R;
import com.homFood.adapter.CheckOutListAdapter;
import com.homFood.model.CardModel;
import com.homFood.model.CheckoutModel;
import com.homFood.model.FatoraModel;
import com.homFood.networking.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckOut extends AppCompatActivity {

    private TextView back;
    private ListView listView;
    private Button sure_add_order;
    private TextView user_location, total_txtV;
    private CheckOutListAdapter adapter;
    float total = 0.0f;
    int total_Quantity = 0;
    String newDataArray;
    String sellerIdData;

    ArrayList<FatoraModel> fatoraModels = new ArrayList<>();
    private ArrayList<CheckoutModel> list = new ArrayList<>();

    private ArrayList<String> seller_id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        Intent intent = getIntent();
        final String address = intent.getStringExtra("address");
        final double lat = intent.getDoubleExtra("lat", 0.0);
        final double lan = intent.getDoubleExtra("lan", 0.0);
        final ArrayList<CardModel> list = intent.getExtras().getParcelableArrayList("card_list");

        user_location = findViewById(R.id.checkout_address_txtv);
        sure_add_order = findViewById(R.id.checkout_confirm_order);
        listView = findViewById(R.id.checkout_products_list);
        total_txtV = findViewById(R.id.checkout_total_txtV);
        back = findViewById(R.id.back);

        user_location.setText(address);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        adapter = new CheckOutListAdapter(CheckOut.this, list);
        listView.setAdapter(adapter);
        for (int x = 0; x < list.size(); x++) {
            total += Float.parseFloat(list.get(x).getOrder_pro_price()) * Integer.parseInt(list.get(x).getOrder_pro_quantity());
            total_Quantity += Integer.parseInt(list.get(x).getOrder_pro_quantity());
        }
        total_txtV.setText(total + "");

        sure_add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                final String currentDateandTime = sdf.format(new Date());

                // now here we convert this list array into json string
                Gson gson = new Gson();
                newDataArray = gson.toJson(list); // dataarray is list aaray

                for (int i = 0; i < list.size(); i++) {
                    seller_id_list.add(list.get(i).getSeller_id());
                }
                sellerIdData = gson.toJson(seller_id_list);
                StringRequest sure_order_request = new StringRequest(Request.Method.POST, Urls.sure_add_order_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            final String code = jsonObject.getString("code");
                            if (success == 1) {
                                final Dialog myDialog = new Dialog(CheckOut.this);
                                TextView close_txt;
                                Button goto_fatora_btn;
                                myDialog.setContentView(R.layout.thanks_layout);
                                close_txt = myDialog.findViewById(R.id.close_thanks_dialog);
                                close_txt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog.dismiss();
                                    }
                                });
                                goto_fatora_btn = myDialog.findViewById(R.id.goto_fatora_btn);
                                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                                myDialog.show();

                                goto_fatora_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        myDialog.dismiss();
                                        Intent intent = new Intent(CheckOut.this, Fatora.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putFloat("total", total);
                                        bundle.putInt("total_quantity", total_Quantity);
                                        bundle.putString("date", currentDateandTime);
                                        bundle.putString("address", address);
                                        bundle.putDouble("lat", lat);
                                        bundle.putDouble("lan", lan);
                                        bundle.putParcelableArrayList("cart_list", list);
                                        bundle.putString("code", code);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        error.getMessage();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("orderarr", newDataArray);
                        params.put("Seller_id", sellerIdData);
                        Log.d("Orderarr", newDataArray);
                        Log.d("Seller_id_data", sellerIdData);
                        System.out.println("parameters:: " + params);
                        return params;
                    }
                };
                RequestQueue queue1 = Volley.newRequestQueue(CheckOut.this);
                queue1.add(sure_order_request);
            }
        });
    }
}
