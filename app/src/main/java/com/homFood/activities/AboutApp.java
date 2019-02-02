package com.homFood.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.AboutModel;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.NetworkAvailable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutApp extends AppCompatActivity {

    private TextView back, title_txt, desc_txt, alsalil_txt;
    private String about_url = "http://cookehome.com/CookApp/ShowData/Users/ShowAbout.php";

    NetworkAvailable networkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        networkAvailable = new NetworkAvailable(this);
        title_txt = findViewById(R.id.name_txt);
        desc_txt = findViewById(R.id.desc_txt);
        alsalil_txt = findViewById(R.id.alsalil_site);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        alsalil_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.alsalil.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        if (networkAvailable.isNetworkAvailable()) {
            Call<AboutModel> call = ApiClient.getClient().create(ApiService.class).aboutApp();
            call.enqueue(new Callback<AboutModel>() {
                @Override
                public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                    AboutModel aboutModel = response.body();
                    String name = aboutModel.getName();
                    title_txt.setText(name);
                    desc_txt.setText(aboutModel.getDescribtion());
                }

                @Override
                public void onFailure(Call<AboutModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
