package com.homFood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.networking.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class TermsAndConditions extends AppCompatActivity {

    TextView terms_txt, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        terms_txt = findViewById(R.id.terms_textV_id);
        back = findViewById(R.id.terms_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.terms_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String terms = jsonObject.getString("policy");

                    terms_txt.setText(terms);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toasty.error(TermsAndConditions.this, getString(R.string.time_out), 1500).show();
                } else if (error instanceof NoConnectionError)
                    Toasty.error(TermsAndConditions.this, getString(R.string.no_connection), 1500).show();
                else if (error instanceof ServerError)
                    Toasty.error(TermsAndConditions.this, getString(R.string.server_error), 1500).show();
                else if (error instanceof NetworkError)
                    Toasty.error(TermsAndConditions.this, getString(R.string.no_connection), 1500).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(TermsAndConditions.this);
        queue.add(stringRequest);
    }
}
