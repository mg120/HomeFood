package com.homFood.activities;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourhcode.forhutils.FUtilsValidation;
import com.homFood.model.ContactModel;
import com.homFood.R;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity {

    private EditText contact_name_ed, contact_email_ed, contact_msgtit_ed, contact_msgdesc_ed;
    private CircularProgressButton send_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        findViewById(R.id.contact_us_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contact_name_ed = findViewById(R.id.contact_name_ed);
        contact_email_ed = findViewById(R.id.contact_email_ed);
        contact_msgtit_ed = findViewById(R.id.message_title_ed);
        contact_msgdesc_ed = findViewById(R.id.msg_txt_ed);
        send_msg = findViewById(R.id.send_msg_id);

        contact_name_ed.setText(MainActivity.Name);
        contact_email_ed.setText(MainActivity.email);


//        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.contact_url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                        String id = jsonObject.getString("id");
//                        String title = jsonObject.getString("name");
//                        String desc = jsonObject.getString("describtion");
//
//                        list.add(new ContactModel(id, title, desc));
//                    }
//                    adapter = new ContactAdapter(ContactIdara.this, list);
//                    listView.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ContactIdara.this, "Error Connection", Toast.LENGTH_SHORT).show();
//            }
//        });
//        RequestQueue queue = Volley.newRequestQueue(ContactIdara.this);
//        queue.add(stringRequest);


        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FUtilsValidation.isEmpty(contact_name_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(contact_email_ed, getString(R.string.required))
                        && FUtilsValidation.isValidEmail(contact_email_ed, getString(R.string.enter_valid_email))
                        && !FUtilsValidation.isEmpty(contact_msgtit_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(contact_msgdesc_ed, getString(R.string.required))
                        ) {
                    send_msg.startAnimation();
                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    Call<ContactModel> call = apiService.contact_Us("mg010695@gmail.com", contact_email_ed.getText().toString(), contact_msgtit_ed.getText().toString(), contact_msgdesc_ed.getText().toString());
                    call.enqueue(new Callback<ContactModel>() {
                        @Override
                        public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                            System.out.println("response: " + response);
                            ContactModel contactModel = response.body();
                            String success = contactModel.getSuccess();
                            if (success.equals("message send successfully")) {
                                send_msg.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                                Toasty.success(ContactUs.this, "تم ارسال الرسالة بنجاح", Toast.LENGTH_SHORT).show();
                                contact_msgtit_ed.setText("");
                                contact_msgdesc_ed.setText("");
                                send_msg.revertAnimation();
                            } else {
                                send_msg.revertAnimation();
                                Toasty.error(ContactUs.this, "حدث خطأ ما, برجاء المحاولة مرة أخرى!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ContactModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

    }
}
