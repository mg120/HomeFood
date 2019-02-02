package com.homFood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.homFood.R;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;


public class Details extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    TextView back, card, prod_name2, prod_category, prod_desc, prod_price, prod_time, rate_txt;
    SliderLayout sliderLayout;

    ArrayList<String> layouts = new ArrayList<>();
    RatingBar ratingBar, item_rate;
    Button order_prod_btn;
    TextView publish_rate, comments, family_name;
    EditText comment_ed;
    ProdImagesModel images_list;
    HomeModel product_data;
    final int min = 20;
    final int max = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (getIntent().getExtras() != null) {
            images_list = getIntent().getExtras().getParcelable("Imageslist");
            product_data = getIntent().getExtras().getParcelable("product_data");
        }
        sliderLayout = findViewById(R.id.banner_slider);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");

        if (!images_list.getProd_img1().equals("0")) {
            layouts.add(Urls.base_Images_Url + images_list.getProd_img1());
        }
        if (!images_list.getProd_img2().equals("0")) {
            layouts.add(Urls.base_Images_Url + images_list.getProd_img2());
        }
        if (!images_list.getProd_img3().equals("0")) {
            layouts.add(Urls.base_Images_Url + images_list.getProd_img3());
        }

        back = findViewById(R.id.details_back);
        prod_name2 = findViewById(R.id.prod_title2);
        prod_name2.setTypeface(custom_font);
        prod_category = findViewById(R.id.prod_category);
        prod_category.setTypeface(custom_font);
        prod_desc = findViewById(R.id.prod_desc);
        prod_desc.setTypeface(custom_font);
        prod_price = findViewById(R.id.prod_price);
        prod_price.setTypeface(custom_font);
        prod_time = findViewById(R.id.prod_item_time);
        item_rate = findViewById(R.id.prod_item_rate);
        card = findViewById(R.id.card);
        rate_txt = findViewById(R.id.rate_txtV_id);
        order_prod_btn = findViewById(R.id.order_now_btn_id);
        family_name = findViewById(R.id.family_name_txt_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        for (String name : layouts) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView.image(name)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);
        // set data to views ................
        prod_name2.setText(product_data.getFamily_name());
        prod_category.setText(product_data.getFood_type());
        prod_desc.setText(product_data.getDesc());
        prod_price.setText(product_data.getPrice());
        family_name.setText(product_data.getSeller_name());
        if (!product_data.getFamily_rate().equals("")) {
            float d = (Float.parseFloat(product_data.getFamily_rate()) * 1) / 5;
            item_rate.setRating(d);
        }
        if (product_data.getTime().equals("0") || product_data.getTime().equals("")) {
            prod_time.setText("جاهزة");
        } else {
            prod_time.setText(product_data.getTime());
        }

        rate_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, Rates.class);
                intent.putExtra("product_id", product_data.getFamily_id());
                startActivity(intent);
            }
        });

        order_prod_btn.setOnClickListener(this);
        family_name.setOnClickListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.order_now_btn_id:
                if (MainActivity.Name.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Details.this);
                    builder.setMessage("قم بتسجيل الدخول اولا")
                            .setCancelable(false)
                            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            }).create().show();
                } else if (MainActivity.customer_id.equals(product_data.getSeller_id())) {
                    Toast.makeText(Details.this, "لا يمكن شراء المنتج!", Toast.LENGTH_SHORT).show();
                } else {
                    int random = new Random().nextInt((max - min) + 1) + min;
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println(response);
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    Toasty.success(Details.this, "تم اضافة المنتج الى السلة", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    OrderRequest orderRequest = new OrderRequest(product_data.getFamily_name(), product_data.getMeal_image(), product_data.getFood_type(), "1", random + "", product_data.getPrice(), product_data.getSeller_id(), product_data.getSeller_name(), product_data.getSeller_email(), MainActivity.customer_id, MainActivity.Name, MainActivity.email, MainActivity.phone, listener);
                    RequestQueue queue = Volley.newRequestQueue(Details.this);
                    queue.add(orderRequest);
                }
                break;
            case R.id.family_name_txt_id:
                Intent intent = new Intent(Details.this, FamilyProducts.class);
                intent.putExtra("family_id", product_data.getSeller_id());
                startActivity(intent);
                break;
        }
    }
}