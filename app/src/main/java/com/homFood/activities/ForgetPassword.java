package com.homFood.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.networking.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ForgetPassword extends AppCompatActivity {

    TextView back;
    Button send_btn;
    EditText email_ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        back = findViewById(R.id.forget_pass_back_id);
        send_btn = findViewById(R.id.send_email_btn_id);
        email_ed = findViewById(R.id.forget_pass_email_ed_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email_ed.getText().toString().trim())) {
                    email_ed.setError(getString(R.string.enter_your_email));
                    email_ed.requestFocus();
                    return;
                }
                StringRequest send_email = new StringRequest(Request.Method.POST, Urls.restore_pass, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("respone: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toasty.success(ForgetPassword.this, "تم إرسال البريد بنجاح, برجاء التحقق من البريد الخاص بك !", 1500).show();
                                finish();
                            } else {
                                Toasty.error(ForgetPassword.this, "برجاء التحقق من البريد المرسل!", 1500).show();
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
                        params.put("Email", email_ed.getText().toString().trim());
                        System.out.println("Params: " + params);
                        return params;
                    }
                };
                int socketTimeout = 60000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                send_email.setRetryPolicy(policy);

                Volley.newRequestQueue(ForgetPassword.this).add(send_email);
            }
        });
    }
}
