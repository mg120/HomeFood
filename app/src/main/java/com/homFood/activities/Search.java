package com.homFood.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.SearchProductsAdapter;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {

    TextView back, no_data_txt;
    ListView results_list;
    private EditText search_ed;
    private Button seach_btn;
    ProgressBar progressBar;

    private SearchProductsAdapter productsAdapter;
    ArrayList<ProdImagesModel> search_prod_images = new ArrayList<>();
    ArrayList<HomeModel> search_prod_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.searck_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        results_list = findViewById(R.id.search_results_list_id);
        search_ed = findViewById(R.id.home_search_ed);
        seach_btn = findViewById(R.id.seach_btn);
        no_data_txt = findViewById(R.id.no_search_data_txt_id);
        progressBar = findViewById(R.id.search_progress_id);

        seach_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = search_ed.getText().toString().trim();
                if (text != null && !text.equals("")) {
                    search_prod_data.clear();
                    search_prod_images.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    RequestQueue requestQueue = Volley.newRequestQueue(Search.this);
                    StringRequest search_request = new StringRequest(Request.Method.POST, Urls.search_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("response: " + response);
                            Log.e("response: ", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        String product_id = jsonObject.getString("Product_ID");
                                        String Name = jsonObject.getString("Name");
                                        String img = jsonObject.getString("img");
                                        String img2 = jsonObject.getString("img2");
                                        String img3 = jsonObject.getString("img3");
                                        String img4 = jsonObject.getString("img4");
                                        String img5 = jsonObject.getString("img5");
                                        String food_type = jsonObject.getString("FoodType");
                                        String time = jsonObject.getString("Timeee");
                                        String price = jsonObject.getString("Price");
                                        String desc = jsonObject.getString("Description");
                                        String Customer_id = jsonObject.getString("Customer_id");
                                        String customer_name = jsonObject.getString("customer_name");
                                        String customer_mail = jsonObject.getString("customer_mail");
                                        String rate = jsonObject.getString("rate");
                                        search_prod_images.add(new ProdImagesModel(img, img2, img3, img4, img5));
                                        search_prod_data.add(new HomeModel(img, product_id, Name, food_type, rate, price, desc, time, Customer_id, customer_name, customer_mail));
                                    }
                                }
                                if (!search_prod_data.isEmpty()) {
                                    results_list.setVisibility(View.VISIBLE);
                                    productsAdapter = new SearchProductsAdapter(Search.this, search_prod_data);
                                    results_list.setAdapter(productsAdapter);
                                } else {
                                    results_list.setVisibility(View.GONE);
                                    no_data_txt.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<>();
                            params.put("gsearch", text);
                            System.out.println("params: " + params);
                            return params;
                        }
                    };
                    int socketTimeout = 60000;
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    search_request.setRetryPolicy(policy);
                    requestQueue.add(search_request);
                }
            }
        });

        results_list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HomeModel familyProductsModel = search_prod_data.get(i);
                ProdImagesModel imagesModel = search_prod_images.get(i);
                Intent intent1 = new Intent(Search.this, Details.class);
                intent1.putExtra("product_data", familyProductsModel);
                intent1.putExtra("Imageslist", imagesModel);
                startActivity(intent1);
            }
        });
    }

}
