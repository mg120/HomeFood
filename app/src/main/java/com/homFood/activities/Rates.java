package com.homFood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.adapter.CommentAdapter;
import com.homFood.model.CommentsModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Rates extends AppCompatActivity {

    TextView back, no_comments;
    RecyclerView commente_recycler;
    RatingBar ratingBar;
    EditText comment_ed;

    CommentAdapter commentAdapter;
    ArrayList<CommentsModel> comments_list = new ArrayList<>();
    private String product_id;
    private String comment;
    private float rate_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        if (getIntent().getExtras() != null) {
            product_id = getIntent().getExtras().getString("product_id");
        }
        back = findViewById(R.id.comments_back);
        no_comments = findViewById(R.id.no_available_comments_txtV_id);
        commente_recycler = findViewById(R.id.item_comments_recycler);
        ratingBar = findViewById(R.id.your_rate_id);
        comment_ed = findViewById(R.id.your_comment_ed_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadItemCommets(product_id);
    }

    private void loadItemCommets(final String product_id) {
        StringRequest comments_request = new StringRequest(Request.Method.POST, Urls.comments_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String prod_id = jsonObject.getString("product_id");
                        String name = jsonObject.getString("name");
                        String comment = jsonObject.getString("comment");
                        String rate = jsonObject.getString("rate");

                        comments_list.add(new CommentsModel(name, comment, rate));
                    }
                    if (comments_list.size() > 0) {
                        no_comments.setVisibility(View.GONE);
                        commente_recycler.setVisibility(View.VISIBLE);

                        commente_recycler.setLayoutManager(new LinearLayoutManager(Rates.this));
                        commente_recycler.addItemDecoration(new DividerItemDecoration(Rates.this, DividerItemDecoration.VERTICAL));
                        commente_recycler.setHasFixedSize(true);
                        commentAdapter = new CommentAdapter(Rates.this, comments_list);
                        commente_recycler.setAdapter(commentAdapter);
                    } else {
                        no_comments.setVisibility(View.VISIBLE);
                        commente_recycler.setVisibility(View.GONE);

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
                params.put("product_id", product_id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(comments_request);
    }

    public void addRate(View view) {
        rate_num = ratingBar.getRating();
        comment = comment_ed.getText().toString().trim();
        if (rate_num == 0.0) {
            Toast.makeText(Rates.this, "برجاء تقييم المنتج!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(comment)) {
            comment_ed.setError("برجاء كتابة التعليق اولا!");
            comment_ed.requestFocus();
            return;
        } else {
            StringRequest rate_request = new StringRequest(Request.Method.POST, Urls.rate_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("response: ", response);
                    System.out.println(response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int flag = jsonObject.getInt("flag");
                            if (flag == 1) {
                                comment_ed.setText("");
                                Toasty.success(Rates.this, "تم تقييم المنتج بنجاح", 1500).show();
                                finish();
                            } else if (flag == 0) {
                                Toasty.error(Rates.this, "تم تقييم هذا المنتج من قبل !", 1500).show();
//                                finish();
                            }
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
                    params.put("product_id", product_id);
                    params.put("rate", String.valueOf(rate_num));
                    params.put("user_id", MainActivity.customer_id);
                    params.put("user_name", MainActivity.Name);
                    params.put("user_mail", MainActivity.email);
                    params.put("comment", comment);

                    System.out.println("Parameters : " + params);

                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(Rates.this);
            queue.add(rate_request);
        }
    }
}
