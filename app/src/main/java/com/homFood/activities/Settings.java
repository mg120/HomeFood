package com.homFood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourhcode.forhutils.FUtilsValidation;
import com.homFood.R;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    EditText name_ed, mail_ed, phone_ed, pass_ed, confirm_pass_ed;
    CircularProgressButton save_btn;
    FrameLayout image_layout;
    ImageView imageView;
    MultipartBody.Part body = null;
    private static final int INTENT_REQUEST_CODE = 110;

    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.settings_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image_layout = findViewById(R.id.settings_image_layout_id);
        imageView = findViewById(R.id.profile_image_id);
        name_ed = findViewById(R.id.profile_name_ed_id);
        mail_ed = findViewById(R.id.profile_email_ed_id);
        phone_ed = findViewById(R.id.profile_phone_ed);
        pass_ed = findViewById(R.id.profile_pass_ed_id);
        confirm_pass_ed = findViewById(R.id.settings_confirm_pass_ed);
        save_btn = findViewById(R.id.save_info_btn_id);


        Picasso.with(Settings.this)
                .load(Urls.base_Images_Url + MainActivity.user_image)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(imageView);

        name_ed.setText(MainActivity.Name);
        mail_ed.setText(MainActivity.email);
        phone_ed.setText(MainActivity.phone);

        save_btn.setOnClickListener(this);
        image_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save_info_btn_id:
                if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(mail_ed, getString(R.string.required))
                        && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {

                    if (TextUtils.isEmpty(pass_ed.getText().toString().trim())) {
                        if (!pass_ed.getText().toString().trim().equals(confirm_pass_ed.getText().toString().trim())) {
                            Toasty.error(Settings.this, "كلمتى المرور غير متطابقتين", 1500).show();
                            return;
                        }
                    }
                    save_btn.startAnimation();

                    StringRequest update_request = new StringRequest(Request.Method.POST, Urls.update_profile_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("ress: " + response);
                            try {
                                save_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                JSONObject jsonObject = new JSONObject(response);
                                final String id = jsonObject.getString("ID");
                                final String name = jsonObject.getString("Name");
                                final String email = jsonObject.getString("Email");
                                final String password = jsonObject.getString("Password");
                                final String Phone = jsonObject.getString("Phone");
                                final String img = jsonObject.getString("img");
                                final String Lat = jsonObject.getString("Lat");
                                final String Lan = jsonObject.getString("Lan");
                                final String Address = jsonObject.getString("Address");
                                final int available = Integer.parseInt(jsonObject.getString("available"));
                                final int type = Integer.parseInt(jsonObject.getString("type"));

                                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                                builder.setMessage("تم تحديث البيانات")
                                        .setCancelable(false)
                                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //   get data from shared preferences ...
                                                sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
                                                editor = sharedPreferences.edit();
                                                editor.putString("email", email);
                                                editor.putString("password", password);
                                                editor.apply();

                                                user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                user_data_edito.putString("user_id", id);
                                                user_data_edito.putString("email", email);
                                                user_data_edito.putString("password", password);
                                                user_data_edito.putString("userName", name);
                                                user_data_edito.putString("address", Address);
                                                user_data_edito.putInt("available", available);
                                                user_data_edito.putString("phone", Phone);
                                                user_data_edito.putString("img", img);
                                                user_data_edito.putString("type", type + "");
                                                user_data_edito.apply();
                                                dialogInterface.dismiss();

                                                Intent intent1 = new Intent(Settings.this, MainActivity.class);
                                                intent1.putExtra("customer_id", id);
                                                intent1.putExtra("email", email);
                                                intent1.putExtra("Name", name);
                                                intent1.putExtra("password", password);
                                                intent1.putExtra("img", img);
                                                intent1.putExtra("Phone", Phone);
                                                intent1.putExtra("Address", Address);
                                                startActivity(intent1);

                                                finish();
                                            }
                                        }).create().show();

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
                            params.put("ID", MainActivity.customer_id);
                            params.put("Name", name_ed.getText().toString().trim());
                            params.put("Email", mail_ed.getText().toString().trim());
                            params.put("Phone", phone_ed.getText().toString().trim());
                            params.put("Password", pass_ed.getText().toString().trim());

                            System.out.println("paramms:: " + params);
                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(Settings.this);
                    queue.add(update_request);
                }
                break;
            case R.id.settings_image_layout_id:
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                try {
//                    startActivityForResult(intent, INTENT_REQUEST_CODE);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }
}
