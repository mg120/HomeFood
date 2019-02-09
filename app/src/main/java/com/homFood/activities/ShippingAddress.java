package com.homFood.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.homFood.R;
import com.homFood.adapter.CardAdapter;
import com.homFood.model.CardModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ShippingAddress extends AppCompatActivity {

    private EditText shipping_name_ed, shipping_email_ed, shipping_address_ed, shipping_country_ed,
            shipping_zone_ed, shipping_city_ed, shipping_phone;
    private Button next;
    private TextView open_shipping_place;

    int PLACE_PICKER_REQUEST = 1;
    double latitude, longtitude;
    String address, cityName, countryName, state;
    private String shipping_address_url = "";
    private TextView back;

    LinearLayout location_info_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        final ArrayList<CardModel> list = getIntent().getExtras().getParcelableArrayList("card_list");

        // Reference to views ...
        shipping_name_ed = findViewById(R.id.shipping_name);
        shipping_email_ed = findViewById(R.id.shipping_email);
        shipping_address_ed = findViewById(R.id.shipping_address_direction);
        shipping_country_ed = findViewById(R.id.shipping_country);
        shipping_zone_ed = findViewById(R.id.shipping_zone);
        shipping_city_ed = findViewById(R.id.shipping_city);
        shipping_phone = findViewById(R.id.shipping_phone);
        next = findViewById(R.id.next_of_shipping);
        open_shipping_place = findViewById(R.id.detect_shipping_location);
        back = findViewById(R.id.back);
        location_info_layout = findViewById(R.id.location_info_layout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shipping_name_ed.setText(MainActivity.Name);
        shipping_email_ed.setText(MainActivity.email);
        shipping_country_ed.setText(MainActivity.Address);
        shipping_phone.setText(MainActivity.phone);

        open_shipping_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(ShippingAddress.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = shipping_name_ed.getText().toString();
                String email = shipping_email_ed.getText().toString();
                String country = shipping_country_ed.getText().toString();
                String phone = shipping_phone.getText().toString();
                String zone = shipping_zone_ed.getText().toString();
                String city = shipping_city_ed.getText().toString();
                String address = shipping_address_ed.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    shipping_name_ed.setError("required");
                    shipping_name_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    shipping_email_ed.setError("required");
                    shipping_email_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(country)) {
                    shipping_country_ed.setError("required");
                    shipping_country_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    shipping_phone.setError("required");
                    shipping_phone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(zone)) {
                    shipping_zone_ed.setError("required");
                    shipping_zone_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(city)) {
                    shipping_city_ed.setError("required");
                    shipping_city_ed.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    shipping_address_ed.setError("required");
                    shipping_address_ed.requestFocus();
                    return;
                }

                Intent intent = new Intent(ShippingAddress.this, CheckCoupon.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("country", country);
                bundle.putString("address", address);
                bundle.putString("city", city);
                bundle.putString("zone", zone);
                bundle.putString("phone", phone);
                bundle.putDouble("lat", latitude);
                bundle.putDouble("lan", longtitude);
                bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) CardAdapter.cart_list);
                intent.putExtras(bundle);
                startActivity(intent);

//                Intent intent = new Intent(ShippingAddress.this, CheckOut.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name", name);
//                bundle.putString("email", email);
//                bundle.putString("country", country);
//                bundle.putString("address", address);
//                bundle.putString("city", city);
//                bundle.putString("zone", zone);
//                bundle.putString("phone", phone);
//                bundle.putDouble("lat", latitude);
//                bundle.putDouble("lan", longtitude);
//                bundle.putParcelableArrayList("card_list", (ArrayList<? extends Parcelable>) CardAdapter.cart_list);
//                intent.putExtras(bundle);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;

                Log.e("lat", latitude + "");
                Log.e("lan", longtitude + "");
                Log.e("address", address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                    if (addresses != null) {
                        location_info_layout.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        if (addresses.size() != 0) {
                            cityName = addresses.get(0).getAddressLine(0);
                            countryName = addresses.get(0).getCountryName();
                            state = addresses.get(0).getAdminArea();
                            shipping_zone_ed.setText(state);
                            shipping_country_ed.setText(countryName);
                            shipping_address_ed.setText(cityName);
                            shipping_city_ed.setText(addresses.get(0).getLocality());

                        } else {
                            // do your stuff
                            Toasty.error(ShippingAddress.this, "لم يتم تحديد الموقع, برجاء المحاولة مرة أخرى!", 1500).show();
                            return;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
