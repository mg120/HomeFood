package com.homFood.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.homFood.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        try {
//            Badges.removeBadge(this);
//            // Alternative way
//            Badges.setBadge(this, 0);
//        } catch (BadgesNotSupportedException badgesNotSupportedException) {
//            Log.d(TAG, badgesNotSupportedException.getMessage());
//        }

        ImageView imageView = findViewById(R.id.splash_logo_img_id);
        ProgressBar progressBar = findViewById(R.id.progress_id);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        imageView.setAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE);
                final String user_id = prefs.getString("user_id", "");
                final String email = prefs.getString("email", "");
                final String password = prefs.getString("password", "");
                final String userName = prefs.getString("userName", "");
                final String address = prefs.getString("address", "");
                final int available = prefs.getInt("available", 0);
                final String phone = prefs.getString("phone", "");
                final String image = prefs.getString("img", "");
                final String type = prefs.getString("type", -1 + "");

                if (!userName.equals("")) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    intent.putExtra("customer_id", user_id);
                    intent.putExtra("email", email);
                    intent.putExtra("Name", userName);
                    intent.putExtra("password", password);
                    intent.putExtra("Phone", phone);
                    intent.putExtra("img", image);
                    intent.putExtra("Address", address);
                    intent.putExtra("available", available);
                    intent.putExtra("type", Integer.parseInt(type));
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, LogIn.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 4000);
    }
}
