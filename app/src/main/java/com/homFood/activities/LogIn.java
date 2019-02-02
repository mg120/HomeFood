package com.homFood.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.homFood.model.LoginModel;
import com.homFood.R;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.NetworkAvailable;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    NetworkAvailable networkAvailable;
    EditText email_ed, pass_ed;
    //    Button login_btn;
    CircularProgressButton login_btn;
    TextView signUp_txt, forget_pass_txt;
    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String MY_PREFS_NAME = "all_user_data";
    public static final String MY_PREFS_login = "login_data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        networkAvailable = new NetworkAvailable(this);
        email_ed = findViewById(R.id.login_email_ed_id);
        pass_ed = findViewById(R.id.login_pass_ed_id);
        login_btn = findViewById(R.id.login_btn_id);
        signUp_txt = findViewById(R.id.sign_txt_id);
        forget_pass_txt = findViewById(R.id.forget_pass_txt_id);

        //   get data from shared preferences ...
        sharedPreferences = getSharedPreferences(MY_PREFS_login, MODE_PRIVATE);
        email_ed.setText(sharedPreferences.getString("email", ""));//"No name defined" is the default value.
        pass_ed.setText(sharedPreferences.getString("password", "")); //0 is the default value.

        login_btn.setOnClickListener(this);
        signUp_txt.setOnClickListener(this);
        forget_pass_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_btn_id:

                if (!FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                        && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                        && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))
                        ) {
                    login_btn.startAnimation();
                    String token = FirebaseInstanceId.getInstance().getToken();
                    System.out.println("token : " + token);
                    if (networkAvailable.isNetworkAvailable()) {
                        ApiService apiService = ApiClient.getClient().create(ApiService.class);
                        Call<LoginModel> call = apiService.login(email_ed.getText().toString(), pass_ed.getText().toString(), token);
                        call.enqueue(new Callback<LoginModel>() {
                            @Override
                            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                final LoginModel loginModel = response.body();
                                boolean success = loginModel.getSuccess();
                                if (success) {
                                    login_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                    // Save user data in shared preferences ...
                                    user_data_edito = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    user_data_edito.putString("user_id", loginModel.getID() + "");
                                    user_data_edito.putString("email", loginModel.getEmail());
                                    user_data_edito.putString("password", loginModel.getPassword());
                                    user_data_edito.putString("userName", loginModel.getName());
                                    user_data_edito.putString("address", loginModel.getAddress());
                                    user_data_edito.putString("phone", loginModel.getPhone());
                                    user_data_edito.putString("img", loginModel.getImg());
                                    user_data_edito.commit();
                                    user_data_edito.apply();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                                            intent.putExtra("customer_id", loginModel.getID() + "");
                                            intent.putExtra("email", loginModel.getEmail());
                                            intent.putExtra("Name", loginModel.getName());
                                            intent.putExtra("password", loginModel.getPassword());
                                            intent.putExtra("Phone", loginModel.getPhone());
                                            intent.putExtra("img", loginModel.getImg());
                                            intent.putExtra("Address", loginModel.getAddress());

                                            startActivity(intent);
                                            Toasty.success(LogIn.this, "تم الدخول بنجاح", 1500).show();
                                            finish();
                                        }
                                    }, 500);
                                } else {
                                    login_btn.revertAnimation();
                                    Toast.makeText(LogIn.this, "خطأ البريد الإلكترونى او كلمة المرور", Toast.LENGTH_LONG).show();
                                }
                                login_btn.setEnabled(true);
                            }


                            @Override
                            public void onFailure(Call<LoginModel> call, Throwable t) {
                                t.printStackTrace();
                                if (t instanceof TimeoutError) {
                                    Toasty.error(LogIn.this, getString(R.string.time_out), 1500).show();
                                } else if (t instanceof NoConnectionError)
                                    Toasty.error(LogIn.this, getString(R.string.no_connection), 1500).show();
                                else if (t instanceof ServerError)
                                    Toasty.error(LogIn.this, getString(R.string.server_error), 1500).show();
                                else if (t instanceof NetworkError)
                                    Toasty.error(LogIn.this, getString(R.string.no_connection), 1500).show();
                            }
                        });
                    } else {
                        Toasty.error(LogIn.this, getString(R.string.no_connection), 1500).show();
                    }
                }
                break;
            case R.id.sign_txt_id:
                startActivity(new Intent(LogIn.this, SignUp.class));
                finish();
                break;
            case R.id.forget_pass_txt_id:
                startActivity(new Intent(LogIn.this, ForgetPassword.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        editor.putString("email", email_ed.getText().toString().trim());
        editor.putString("password", pass_ed.getText().toString().trim());
        editor.commit();
        editor.apply();
    }
}
