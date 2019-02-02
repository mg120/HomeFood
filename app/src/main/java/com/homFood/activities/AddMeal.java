package com.homFood.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fourhcode.forhutils.FUtilsValidation;
import com.homFood.PermissionUtils;
import com.homFood.ProgressRequestBody;
import com.homFood.R;
import com.homFood.model.CategoriesModel;
import com.homFood.model.PicModel;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.Urls;
import com.karan.churi.PermissionManager.PermissionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddMeal extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    TextView back;
    EditText name_ed, price_ed, description_ed;
    Spinner categories_spinner, meal_state_spinner;
    ImageView image_1, image_2, image_3;
    Button add_imgs_btn;
    TextView time_txt;
    CircularProgressButton add_btn;

    List<CategoriesModel> categories_list = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    List<String> meal_states = new ArrayList<>();
    ArrayList<Uri> images = new ArrayList<>();

    String time, selected_category;
    MultipartBody.Part mPartImage_1, mPartImage_2, mPartImage_3;
    SpotsDialog dialog;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        back = findViewById(R.id.contact_us_back);
        name_ed = findViewById(R.id.meal_name_ed_id);
        price_ed = findViewById(R.id.meal_price_ed_id);
        time_txt = findViewById(R.id.meal_time_txt_id);
        description_ed = findViewById(R.id.meal_description_ed_id);
        categories_spinner = findViewById(R.id.meal_categories_spinner_id);
        meal_state_spinner = findViewById(R.id.meal_state_spinner_id);
        image_1 = findViewById(R.id.add_meal_img1);
        image_2 = findViewById(R.id.add_meal_img2);
        image_3 = findViewById(R.id.add_meal_img3);
        add_imgs_btn = findViewById(R.id.add_images_btn);
        add_btn = findViewById(R.id.add_btn_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // -- For Categories Spinner .........................
        categoriesData();

        // -- For state Spinner ......................
        meal_states.add("جاهزة");
        meal_states.add("عند الطلب");
        final ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(AddMeal.this, android.R.layout.simple_dropdown_item_1line, meal_states);
        meal_state_spinner.setAdapter(state_adapter);
        meal_state_spinner.setSelection(0);
        meal_state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_state = adapterView.getSelectedItem().toString();
                if (selected_state.equals("عند الطلب")) {
                    time_txt.setVisibility(View.VISIBLE);
                } else if (selected_state.equals("جاهزة")) {
                    time_txt.setVisibility(View.GONE);
                    time = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add_btn.setOnClickListener(this);
        add_imgs_btn.setOnClickListener(this);
        time_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.meal_time_txt_id:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddMeal.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time_txt.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("مدة التجهيز :");
                mTimePicker.show();
                break;
            case R.id.add_images_btn:
                getPickImageWithPermission();
                break;

            case R.id.add_btn_id:
                addProductInfo();
                break;
        }
    }

    private void addProductInfo() {

        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(price_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(description_ed, getString(R.string.required))
                ) {
            if (time_txt.getVisibility() == View.VISIBLE && TextUtils.isEmpty(time_txt.getText().toString().trim())){
                Toasty.error(AddMeal.this, "برجاء تحديد وقت التجهيز", 1500).show();
                return;
            }
            if (mPartImage_1 == null) {
                Toasty.error(AddMeal.this, "برجاء اختيار صور المنتج اولا!", 1500).show();
                return;
            }
            RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name_ed.getText().toString().trim());
            RequestBody foodtype_Part = RequestBody.create(MultipartBody.FORM, selected_category);
            RequestBody time_Part = RequestBody.create(MultipartBody.FORM, time_txt.getText().toString().trim());
            RequestBody price_Part = RequestBody.create(MultipartBody.FORM, price_ed.getText().toString().trim());
            RequestBody description_Part = RequestBody.create(MultipartBody.FORM, description_ed.getText().toString().trim());
            RequestBody cus_id_part = RequestBody.create(MultipartBody.FORM, MainActivity.customer_id);
            RequestBody cus_name_part = RequestBody.create(MultipartBody.FORM, MainActivity.Name);
            RequestBody cus_email_part = RequestBody.create(MultipartBody.FORM, MainActivity.email);
            add_btn.setEnabled(false);
            add_btn.startAnimation();
            ApiClient.getClient().create(ApiService.class)
                    .uploadImage(NamePart, foodtype_Part, time_Part, price_Part,
                            description_Part, cus_id_part, cus_name_part, cus_email_part, mPartImage_1, mPartImage_2, mPartImage_3)
                    .enqueue(new Callback<PicModel>() {
                        @Override
                        public void onResponse(Call<PicModel> call, retrofit2.Response<PicModel> response) {
                            System.out.println("res: " + response);
                            Log.e("response: ", response + "");
                            PicModel picModel = response.body();
                            add_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddMeal.this);
                            builder.setMessage("تم اضافة المنتج بنجاح")
                                    .setCancelable(false)
                                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    }).create().show();
                        }

                        @Override
                        public void onFailure(Call<PicModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    public void getPickImageWithPermission() {
        if (PermissionUtils.canMakeSmores(Build.VERSION_CODES.LOLLIPOP_MR1)) {
            if (!PermissionUtils.hasPermissions(AddMeal.this, PermissionUtils.IMAGE_PERMISSIONS)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PermissionUtils.IMAGE_PERMISSIONS, 100);
                }
            } else {
                pickMultiImages();
            }
        } else {
            pickMultiImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //To get Granted Permission and Denied Permission
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied = permissionManager.getStatus().get(0).denied;
    }


    void pickMultiImages() {
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(AddMeal.this)
                .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(ArrayList<Uri> uriList) {

                        images.clear();
                        images.addAll(uriList);
                        if (images.size() > 0) {
                            createMultiPartFile();
                        }
                    }
                })
                .setTitle("اخترالصور")
                .setSelectMaxCount(3)
                .setSelectMinCount(1)
                .setPeekHeight(2600)
                .showTitle(false)
                .setCompleteButtonText(R.string.done)
                .create();
        bottomSheetDialogFragment.show(getSupportFragmentManager());
    }

    private void createMultiPartFile() {

        image_1.setImageURI(null);
        image_2.setImageURI(null);
        image_3.setImageURI(null);

        if (images.size() == 1) {
            image_1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImage_1 = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);
        } else if (images.size() == 2) {
            image_1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImage_1 = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            image_2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImage_2 = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);

        } else if (images.size() == 3) {
            image_1.setImageURI(images.get(0));
            File ImageFile = new File(images.get(0).getPath());
            final ProgressRequestBody fileBody = new ProgressRequestBody(ImageFile, this);
            mPartImage_1 = MultipartBody.Part.createFormData("img", ImageFile.getName(), fileBody);

            image_2.setImageURI(images.get(1));
            File ImageFileTwo = new File(images.get(1).getPath());
            final ProgressRequestBody fileBodyTwo = new ProgressRequestBody(ImageFileTwo, this);
            mPartImage_2 = MultipartBody.Part.createFormData("img2", ImageFileTwo.getName(), fileBodyTwo);

            image_3.setImageURI(images.get(2));
            File ImageFileThree = new File(images.get(2).getPath());
            final ProgressRequestBody fileBodyThree = new ProgressRequestBody(ImageFileThree, this);
            mPartImage_3 = MultipartBody.Part.createFormData("img3", ImageFileThree.getName(), fileBodyThree);
        }
        System.out.println("partone : " + mPartImage_1);
        System.out.println("parttwo : " + mPartImage_2);
    }

    public void categoriesData() {
        categories_list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.categories_url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int x = 0; x < jsonArray.length(); x++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(x);
                        String id = jsonObject.getString("id");
                        String type = jsonObject.getString("name");
                        String img = jsonObject.getString("img");

                        Log.e("type: ", type);
                        categories_list.add(new CategoriesModel(id, type, img));
                    }
                    // -- For Categories Spinner .........................
                    for (int i = 0; i < categories_list.size(); i++) {
                        stringList.add(categories_list.get(i).getName());
                    }
                    final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(AddMeal.this, android.R.layout.simple_dropdown_item_1line, stringList);
                    categories_spinner.setAdapter(category_adapter);
                    categories_spinner.setSelection(0);
                    categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selected_category = adapterView.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof TimeoutError) {
                    Toasty.error(AddMeal.this, getString(R.string.time_out), 1500).show();
                } else if (error instanceof NoConnectionError)
                    Toasty.error(AddMeal.this, getString(R.string.no_connection), 1500).show();
                else if (error instanceof ServerError)
                    Toasty.error(AddMeal.this, getString(R.string.server_error), 1500).show();
                else if (error instanceof NetworkError)
                    Toasty.error(AddMeal.this, getString(R.string.no_connection), 1500).show();
            }
        });
        Volley.newRequestQueue(AddMeal.this).add(stringRequest);
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }


    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}
