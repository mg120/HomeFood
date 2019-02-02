package com.homFood.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.homFood.R;
import com.homFood.model.SignupModel;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.NetworkAvailable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText name_ed, email_ed, phone_ed, pass_ed, confirm_pass_ed;
    CircularProgressButton signUp_btn;
    TextView login_txt, detect_location_txt, terms_conditions;
    ImageView imageView;
    CheckBox checkBox;
    FrameLayout add_image_layout;
    NetworkAvailable networkAvailable;
    // put data to shared preferences ...
    SharedPreferences.Editor user_data_edito;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    final int PICK_IMAGE = 100;
    int PLACE_PICKER_REQUEST = 1;
    double latitude, longtitude;
    String address, cityName, streetName, locality;
    String imageString = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        networkAvailable = new NetworkAvailable(this);
        name_ed = findViewById(R.id.sign_name_ed_id);
        email_ed = findViewById(R.id.sign_email_ed_id);
        phone_ed = findViewById(R.id.sign_phone_ed_id);
        pass_ed = findViewById(R.id.sign_pass_ed_id);
        detect_location_txt = findViewById(R.id.sign_location_txt_id);
        confirm_pass_ed = findViewById(R.id.login_con_pass_ed_id);
        signUp_btn = findViewById(R.id.signUp_btn_id);
        login_txt = findViewById(R.id.sign_txt_id);
        imageView = findViewById(R.id.sign_img_id);
        checkBox = findViewById(R.id.ch_box);
        terms_conditions = findViewById(R.id.terms_txt_id);
        add_image_layout = findViewById(R.id.image_layout_id);

        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            }
        });

        terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, TermsAndConditions.class));
            }
        });

        add_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        detect_location_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SignUp.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkAvailable.isNetworkAvailable()) {
                    register();
                } else {
                    Toasty.error(SignUp.this, getString(R.string.no_connection), 1500).show();
                }
            }
        });
    }

    private void register() {
        String userName = name_ed.getText().toString().trim();
        String email = email_ed.getText().toString().trim();
        String password = pass_ed.getText().toString().trim();
        String phone = phone_ed.getText().toString().trim();


        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(email_ed, getString(R.string.required))
                && FUtilsValidation.isValidEmail(email_ed, getString(R.string.enter_valid_email))
                && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(pass_ed, getString(R.string.required))
                ) {
            if (TextUtils.isEmpty(address)) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUp.this);
                builder.setMessage(getString(R.string.location_required))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create()
                        .show();
                return;
            }
            if (!checkBox.isChecked()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setMessage("برجاء الموافقة على الشروط والأحكام!")
                        .setCancelable(false)
                        .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create()
                        .show();
                return;
            }

//            RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userName);
//            RequestBody Email_Part = RequestBody.create(MultipartBody.FORM, email);
//            RequestBody phone_Part = RequestBody.create(MultipartBody.FORM, phone);
//            RequestBody pass_Part = RequestBody.create(MultipartBody.FORM, password);
//            RequestBody lat_Part = RequestBody.create(MultipartBody.FORM, latitude + "");
//            RequestBody lng_Part = RequestBody.create(MultipartBody.FORM, longtitude + "");
//            RequestBody address_Part = RequestBody.create(MultipartBody.FORM, address);
//            RequestBody type_part = RequestBody.create(MultipartBody.FORM, "0");

//            final SpotsDialog dialog = new SpotsDialog(SignUp.this, getString(R.string.signning), R.style.Custom);
//            dialog.show();
            signUp_btn.startAnimation();

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<List<SignupModel>> call = apiService.signUp(userName, email, password, phone, latitude + "", longtitude + "", locality, "0", imageString);
            call.enqueue(new Callback<List<SignupModel>>() {
                @Override
                public void onResponse(Call<List<SignupModel>> call, Response<List<SignupModel>> response) {
                    signUp_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                    SignupModel signupModel = response.body().get(0);
                    int flag = signupModel.getFlag();
                    if (flag == 1) {

                        // Save user data in shared preferences ...
                        user_data_edito = getSharedPreferences(LogIn.MY_PREFS_NAME, MODE_PRIVATE).edit();
                        user_data_edito.putString("user_id", signupModel.getId() + "");
                        user_data_edito.putString("email", signupModel.getEmail());
                        user_data_edito.putString("userName", signupModel.getName());
                        user_data_edito.putString("address", signupModel.getAddress());
                        user_data_edito.putString("phone", signupModel.getPhone());
                        user_data_edito.putString("img", signupModel.getImg());
                        user_data_edito.commit();
                        user_data_edito.apply();


                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        intent.putExtra("customer_id", signupModel.getId() + "");
                        intent.putExtra("email", signupModel.getEmail());
                        intent.putExtra("Name", signupModel.getName());
                        intent.putExtra("Phone", signupModel.getPhone());
                        intent.putExtra("img", signupModel.getImg());
                        intent.putExtra("Address", signupModel.getAddress());
                        startActivity(intent);

                        Toasty.success(SignUp.this, "تم التسجيل بنجاح", 1500).show();
                        finish();
                    } else if (flag == 0) {
                        signUp_btn.revertAnimation();
                        Toasty.error(SignUp.this, "هذا الايميل او الاسم مسجل مسبقا!", 1500).show();
                    }
//                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<SignupModel>> call, Throwable t) {
                    t.printStackTrace();
                    if (t instanceof TimeoutError) {
                        Toasty.error(SignUp.this, getString(R.string.time_out), 1500).show();
                    } else if (t instanceof NoConnectionError)
                        Toasty.error(SignUp.this, getString(R.string.no_connection), 1500).show();
                    else if (t instanceof ServerError)
                        Toasty.error(SignUp.this, getString(R.string.server_error), 1500).show();
                    else if (t instanceof NetworkError)
                        Toasty.error(SignUp.this, getString(R.string.no_connection), 1500).show();
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
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
                    if (addresses.size() > 0 && addresses != null) {
                        cityName = addresses.get(0).getSubAdminArea();
                        int maxAddressLine = addresses.get(0).getMaxAddressLineIndex();
                        streetName = addresses.get(0).getFeatureName();
                        locality = addresses.get(0).getLocality();
                        Log.e("cityName: ", addresses.get(0).getAddressLine(0));
                        detect_location_txt.setText(address);
                        Log.e("stret1: ", addresses.get(0).getCountryName());
                        Log.e("stret2: ", addresses.get(0).getLocality());
                        Log.e("stret3: ", addresses.get(0).getAdminArea());
                        Log.e("stret4: ", addresses.get(0).getAddressLine(maxAddressLine));
                        Log.e("stret5: ", addresses.get(0).getFeatureName());
                        Log.e("stret7: ", addresses.get(0).getSubAdminArea());

                    } else {
                        // do your stuff
                        detect_location_txt.setText("لم يتم تحديد الموقع بعد!, حاول مرة اخرى");
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                    InputStream is = getContentResolver().openInputStream(data.getData());

                    toBase64(bitmap);
//                    createMultiPartFile(getBytes(is));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                pickImage();
            }
        }
    }

    private void toBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }
}