package com.homFood.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.homFood.fragments.FamilyLocationFrag;
import com.homFood.fragments.FamilyProductsFrag;
import com.homFood.fragments.LastDealsFrag;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FamilyProducts extends AppCompatActivity {

    boolean products_clicked;
    boolean location_clicked;
    boolean deals_clicked;

    TextView back;
    Dialog myDialog;
    ImageView products_family_img;
    FloatingActionButton chat_family_fab;
    TextView family_products_name, family_products_position, family_products_email, family_products_phone;
    LinearLayout family_products_layout, family_info_layout;
    LinearLayout contact_with_family_layout, family_map_location;

    String family_id;
    String Lat, Lan, email, Name;
    public static int available;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_products);

        // ------
        myDialog = new Dialog(FamilyProducts.this);
        if (getIntent().getExtras() != null) {
            family_id = getIntent().getExtras().getString("family_id");
        }
        family_info_layout = findViewById(R.id.family_info_layout_id);
        chat_family_fab = findViewById(R.id.chat_family_fab_id);
        products_family_img = findViewById(R.id.family_products_img);
        family_products_name = findViewById(R.id.family_products_name);
        family_products_position = findViewById(R.id.family_products_position);
        family_products_email = findViewById(R.id.family_products_email);
        family_products_phone = findViewById(R.id.family_products_phone);
        family_products_layout = findViewById(R.id.family_products_layout);
        family_map_location = findViewById(R.id.family_map_show_location);
        frameLayout = findViewById(R.id.family_frame_layout);
        contact_with_family_layout = findViewById(R.id.contact_with_family_layout);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get Family Data ...
        FamilyData(family_id);

        // Set Products is clicked By Default
        products_clicked = true;
        if (products_clicked) {
            family_products_layout.setBackgroundResource(R.drawable.selceted_btn_bg);
            family_map_location.setBackgroundResource(R.drawable.edtxt_bg);
            contact_with_family_layout.setBackgroundResource(R.drawable.edtxt_bg);
            final FamilyProductsFrag fragment = new FamilyProductsFrag();
            final Bundle bundle = new Bundle();
            bundle.putString("family_id", family_id);
            fragment.setArguments(bundle);
            displaySelectedFragment(fragment);
            products_clicked = false;
            location_clicked = true;
            deals_clicked = true;
        }
        // then put it to list view ..
        family_products_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (products_clicked) {
                    family_products_layout.setBackgroundResource(R.drawable.selceted_btn_bg);
                    family_map_location.setBackgroundResource(R.drawable.edtxt_bg);
                    contact_with_family_layout.setBackgroundResource(R.drawable.edtxt_bg);

                    FamilyProductsFrag fragment = new FamilyProductsFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("family_id", family_id);
                    fragment.setArguments(bundle);
                    displaySelectedFragment(fragment);
                    products_clicked = false;
                    location_clicked = true;
                    deals_clicked = true;
                }
            }
        });

        family_map_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location_clicked) {
                    family_map_location.setBackgroundResource(R.drawable.selceted_btn_bg);
                    family_products_layout.setBackgroundResource(R.drawable.edtxt_bg);
                    contact_with_family_layout.setBackgroundResource(R.drawable.edtxt_bg);
                    FamilyLocationFrag locationFragment = new FamilyLocationFrag();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("lat", Lat);
                    bundle1.putString("lng", Lan);
                    locationFragment.setArguments(bundle1);
                    displaySelectedFragment(locationFragment);
                    location_clicked = false;
                    products_clicked = true;
                    deals_clicked = true;
                }
            }
        });

        chat_family_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Family_chat(MainActivity.customer_id, family_id, MainActivity.Name, Name);
            }
        });

        contact_with_family_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deals_clicked) {
                    contact_with_family_layout.setBackgroundResource(R.drawable.selceted_btn_bg);
                    family_map_location.setBackgroundResource(R.drawable.edtxt_bg);
                    family_products_layout.setBackgroundResource(R.drawable.edtxt_bg);

                    LastDealsFrag dealsfragment = new LastDealsFrag();
                    Bundle deals_bundle = new Bundle();
                    deals_bundle.putString("family_id", family_id);
                    dealsfragment.setArguments(deals_bundle);
                    displaySelectedFragment(dealsfragment);
                    deals_clicked = false;
                    products_clicked = true;
                    location_clicked = true;
                }
            }
        });
    }

    private void Family_chat(final String sender_id, final String receiver_id, final String sender_name, final String receiver_name) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!MainActivity.customer_id.equals("")) {
                    Intent intent1 = new Intent(FamilyProducts.this, Messages.class);
                    intent1.putExtra("sender_id", sender_id);
                    intent1.putExtra("receiver_id", receiver_id);
                    intent1.putExtra("sender_name", sender_name);
                    intent1.putExtra("receiver_name", receiver_name);
                    startActivity(intent1);
                } else {
                    Toast.makeText(FamilyProducts.this, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                }
            }
        }, 10);
    }

    private void FamilyData(final String family_id) {
        StringRequest family_data_request = new StringRequest(Request.Method.POST, Urls.family_Data_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Name = jsonObject.getString("Name");
                        email = jsonObject.getString("Email");
                        String Phone = jsonObject.getString("Phone");
                        String img = jsonObject.getString("img");
                        Lat = jsonObject.getString("Lat");
                        Lan = jsonObject.getString("Lan");
                        available = jsonObject.getInt("available");
                        String Address = jsonObject.getString("Address");

                        Picasso.with(FamilyProducts.this).load(Urls.base_Images_Url + img).into(products_family_img);
                        family_products_name.setText(Name);
                        family_products_position.setText(Address);
                        family_products_email.setText(email);
                        family_products_phone.setText(Phone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FamilyProducts.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("ID", family_id);
                System.out.println("params: " + params);
                return params;
            }
        };

        Volley.newRequestQueue(FamilyProducts.this).add(family_data_request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.products_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.filter_icon) {
            show_popup();
        }
        return super.onOptionsItemSelected(item);
    }

    private void show_popup() {
        String[] data = getResources().getStringArray(R.array.products_popup_list);
        TextView txt_close;
        ListView filter_list;
        myDialog.setContentView(R.layout.filter_popup_window);
        txt_close = myDialog.findViewById(R.id.txt_close);
        filter_list = myDialog.findViewById(R.id.filter_list);
        txt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FamilyProducts.this, android.R.layout.simple_list_item_1, data);
        filter_list.setAdapter(adapter);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        myDialog.show();
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.family_frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
