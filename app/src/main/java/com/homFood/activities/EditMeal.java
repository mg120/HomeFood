package com.homFood.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import com.homFood.ProgressRequestBody;
import com.homFood.R;
import com.homFood.model.CategoriesModel;
import com.homFood.model.EditProductModel;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.ApiClient;
import com.homFood.networking.ApiService;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import es.dmoral.toasty.Toasty;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMeal extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {

    TextView back;
    EditText name_ed, price_ed, description_ed;
    TextView time_txt;
    Spinner categories_spinner, meal_state_spinner;
    ImageView image_1, image_2, image_3;
    Button edit_imgs_btn;
    CircularProgressButton edit_btn;

    List<CategoriesModel> categories_list = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    List<String> meal_states = new ArrayList<>();
    ArrayList<Uri> images = new ArrayList<>();

    String time, selected_category;
    MultipartBody.Part mPartImage_1, mPartImage_2, mPartImage_3;
    ProdImagesModel list;
    HomeModel product_data;
    int selected_index;
    int time_state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);

        // get Previos Images
        if (getIntent().getExtras() != null) {
            list = getIntent().getExtras().getParcelable("product_images");
            product_data = getIntent().getExtras().getParcelable("product_data");
        }

        back = findViewById(R.id.edit_meal_back);
        name_ed = findViewById(R.id.edit_meal_name_ed_id);
        price_ed = findViewById(R.id.edit_meal_price_ed_id);
        time_txt = findViewById(R.id.edit_meal_time_txt_id);
        description_ed = findViewById(R.id.edit_meal_description_ed_id);
        categories_spinner = findViewById(R.id.edit_meal_categories_spinner_id);
        meal_state_spinner = findViewById(R.id.edit_meal_state_spinner_id);
        image_1 = findViewById(R.id.edit_meal_img1);
        image_2 = findViewById(R.id.edit_meal_img2);
        image_3 = findViewById(R.id.edit_meal_img3);
        edit_imgs_btn = findViewById(R.id.edit_images_btn);
        edit_btn = findViewById(R.id.edit_btn_id);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // ------------------ product images -------------------
        Picasso.with(EditMeal.this).load(Urls.base_Images_Url + list.getProd_img1()).into(image_1);
        Picasso.with(EditMeal.this).load(Urls.base_Images_Url + list.getProd_img2()).into(image_2);
        Picasso.with(EditMeal.this).load(Urls.base_Images_Url + list.getProd_img3()).into(image_3);

        /// ----------------------------------------------------
        name_ed.setText(product_data.getFamily_name());
        description_ed.setText(product_data.getDesc());
        if (!product_data.getTime().equals("0")) {
            time_txt.setVisibility(View.GONE);
            time_state = 0;
        } else {
            time_state = 1;
            time_txt.setVisibility(View.VISIBLE);
            time_txt.setText(product_data.getTime());
        }
        price_ed.setText(product_data.getPrice());

        categoriesData();

        // -- For state Spinner ......................
        meal_states.add("جاهزة");
        meal_states.add("عند الطلب");
        final ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(EditMeal.this, android.R.layout.simple_dropdown_item_1line, meal_states);
        meal_state_spinner.setAdapter(state_adapter);
        meal_state_spinner.setSelection(time_state);
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

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProductInfo();
            }
        });
        edit_imgs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMultiImages();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_meal_time_txt_id:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditMeal.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time_txt.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("مدة التجهيز :");
                mTimePicker.show();
                break;
        }
    }

    void pickMultiImages() {
        TedBottomPicker bottomSheetDialogFragment = new TedBottomPicker.Builder(EditMeal.this)
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
    }

    private void editProductInfo() {
        if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(price_ed, getString(R.string.required))
                && !FUtilsValidation.isEmpty(description_ed, getString(R.string.required))) {

            RequestBody product_id_part = RequestBody.create(MultipartBody.FORM, product_data.getFamily_id());
            RequestBody NamePart = RequestBody.create(MultipartBody.FORM, name_ed.getText().toString().trim());
            RequestBody foodtype_Part = RequestBody.create(MultipartBody.FORM, selected_category);
            RequestBody time_Part = RequestBody.create(MultipartBody.FORM, time_txt.getText().toString().trim());
            RequestBody price_Part = RequestBody.create(MultipartBody.FORM, price_ed.getText().toString().trim());
            RequestBody description_Part = RequestBody.create(MultipartBody.FORM, description_ed.getText().toString().trim());

            if (time_txt.getVisibility() == View.VISIBLE && TextUtils.isEmpty(time_txt.getText().toString().trim())) {
                Toasty.error(EditMeal.this, "برجاء تحديد وقت التجهيز", 1500).show();
                return;
            }

            edit_btn.setEnabled(false);
            edit_btn.startAnimation();
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<EditProductModel> call = apiService.edit_Product(product_id_part, NamePart, foodtype_Part, time_Part, price_Part, description_Part, mPartImage_1, mPartImage_2, mPartImage_3);
            call.enqueue(new Callback<EditProductModel>() {
                @Override
                public void onResponse(Call<EditProductModel> call, Response<EditProductModel> response) {
                    EditProductModel editProductModel = response.body();
                    int success = editProductModel.getSuccess();
                    edit_btn.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                    if (success == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditMeal.this);
                        builder.setMessage("تم تعديل المنتج بنجاح")
                                .setCancelable(false)
                                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                }).create().show();
                    }
                }

                @Override
                public void onFailure(Call<EditProductModel> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
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
                        if (product_data.getFood_type().equals(categories_list.get(i).getName()))
                            selected_index = i;
                    }
                    final ArrayAdapter<String> category_adapter = new ArrayAdapter<String>(EditMeal.this, android.R.layout.simple_dropdown_item_1line, stringList);
                    categories_spinner.setAdapter(category_adapter);
                    categories_spinner.setSelection(selected_index);
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
                    Toasty.error(EditMeal.this, getString(R.string.time_out), 1500).show();
                } else if (error instanceof NoConnectionError)
                    Toasty.error(EditMeal.this, getString(R.string.no_connection), 1500).show();
                else if (error instanceof ServerError)
                    Toasty.error(EditMeal.this, getString(R.string.server_error), 1500).show();
                else if (error instanceof NetworkError)
                    Toasty.error(EditMeal.this, getString(R.string.no_connection), 1500).show();
            }
        });
        Volley.newRequestQueue(EditMeal.this).add(stringRequest);
    }
}
