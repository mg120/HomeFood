package com.homFood.activities;

import android.app.DatePickerDialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.Gson;
import com.homFood.R;
import com.homFood.adapter.DisscountProductsAdapter;
import com.homFood.adapter.MyProductsAdapter;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import br.com.simplepass.loading_button_lib.Utils;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;

public class CreateCouponActivity extends AppCompatActivity implements View.OnClickListener {

    EditText discount_percent_ed;
    TextView finished_date_txt;
    CircularProgressButton create_btn;
    RecyclerView recyclerView;
    DisscountProductsAdapter adapter;
    ProgressBar progressBar;
    TextView no_data_txt, back;
    RadioGroup radioGroup;
    String selected_percent = "%";

    ArrayList<ProdImagesModel> osra_images = new ArrayList<>();
    ArrayList<HomeModel> osra_data = new ArrayList<>();
    public static String seleceted_item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coupon);

        findViewById(R.id.discount_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        discount_percent_ed = findViewById(R.id.discount_percent_ed_id);
        progressBar = findViewById(R.id.discount_progressBar_id);
        no_data_txt = findViewById(R.id.dis_no_produts_data_id);
        finished_date_txt = findViewById(R.id.finished_date_txt_id);
        create_btn = findViewById(R.id.create_coupon_btn_id);
        radioGroup = findViewById(R.id.radio_group);
        recyclerView = findViewById(R.id.discount_products_recycler_id);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        radioGroup.check(R.id.percent_btn_id);

        my_Products_Data(MainActivity.customer_id);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                finished_date_txt.setText(sdf.format(myCalendar.getTime()));
            }
        };
        finished_date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateCouponActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        create_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.create_coupon_btn_id:
                if (TextUtils.isEmpty(discount_percent_ed.getText().toString().trim())) {
                    Toast.makeText(this, "برجاء إدخال نسبة الخصم اولأ !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(finished_date_txt.getText().toString().trim())) {
                    Toast.makeText(this, "برجاء تحديد تاريخ نهاية الخصم !", Toast.LENGTH_SHORT).show();
                    return;
                }

                create_btn.startAnimation();
                // Create Random Value ....
                Random r = new Random();
                final int rand_Num = r.nextInt(1000 - 2) + 65;

                // Convert List to String Object ...
//                Gson gson = new Gson();
//                final String list_obj = gson.toJson(selected_items_list);
//                System.out.println("list:: " + list_obj);

                StringRequest create_coupon = new StringRequest(Request.Method.POST, Urls.create_coupons_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response : " + response);
                        Log.e("ress: ", response);

                        create_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                        Toasty.success(CreateCouponActivity.this, "تم إنشاء الكوبون بنجاح!", 1500).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(CreateCouponActivity.this, getString(R.string.time_out), Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError)
                            Toast.makeText(CreateCouponActivity.this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                        else if (error instanceof ServerError)
                            Toast.makeText(CreateCouponActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(CreateCouponActivity.this, getString(R.string.no_connection), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("User_ID", MainActivity.customer_id);
                        params.put("Product_ID", seleceted_item_id);
                        params.put("Discount_value", discount_percent_ed.getText().toString().trim() + selected_percent);
                        params.put("Expiry_date", finished_date_txt.getText().toString().trim());
                        params.put("Value", rand_Num + "");
                        System.out.println("Paramms: " + params);
                        return params;
                    }
                };

                int socketTimeout = 60000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                create_coupon.setRetryPolicy(policy);
                Volley.newRequestQueue(this).add(create_coupon);

                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.real_saudi_btn_id:
                if (checked) {
                    // Pirates are the best
                    selected_percent = "ريال";
                    Toast.makeText(this, "ريال", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.percent_btn_id:
                if (checked) {
                    // Ninjas rule
                    selected_percent = "%";
                    Toast.makeText(this, "%%", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                    no_data_txt.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter = new DisscountProductsAdapter(CreateCouponActivity.this, osra_data, osra_images);
                    recyclerView.setAdapter(adapter);
                    // Set Animation to Recyceler ...
//                    products_recycler.setLayoutAnimation(controller);
//                    products_recycler.getAdapter().notifyDataSetChanged();
//                    products_recycler.scheduleLayoutAnimation();
                } else {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    no_data_txt.setVisibility(View.VISIBLE);
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
