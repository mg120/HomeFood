package com.homFood.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.activities.MainActivity;
import com.homFood.R;
import com.homFood.adapter.HomeAdapter;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class MainFragment extends Fragment {

    TextView no_data_txt;
    RecyclerView home_recycler;
    HomeAdapter adapter;
    ProgressBar progressBar;
    ImageView search_btn;
    EditText search_ed;

    final String[] address = new String[1];

    String current_url = Urls.current_url;
    ArrayList<HomeModel> home_data = new ArrayList<>();
    ArrayList<ProdImagesModel> images_list = new ArrayList<>();
    String key, selected_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("الرئيسية");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        no_data_txt = view.findViewById(R.id.no_data_txt_id);
        progressBar = view.findViewById(R.id.main_progress);
        home_recycler = view.findViewById(R.id.home_recyclerView);
        search_btn = view.findViewById(R.id.search_btn_id);
        search_ed = view.findViewById(R.id.search_ed_id);

        home_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            key = getArguments().getString("NAME");
            if (key != null) {
                horizontal_search_filter(key);
                return;
            }
            selected_item = getArguments().getString("selected_item");
            if (selected_item != null) {
                if (MainActivity.selected_item.equals("اسم الطبخة")) {
                    current_url = Urls.food_type_url;
                } else if (MainActivity.selected_item.equals("أ - ى")) {
                    current_url = Urls.name1_url;
                } else if (MainActivity.selected_item.equals("ى - أ")) {
                    current_url = Urls.name2_url;
                } else if (MainActivity.selected_item.equals("اسم الاسرة")) {
                    current_url = Urls.osra_name_url;
                } else if (MainActivity.selected_item.equals("اعلى سعر")) {
                    current_url = Urls.price1_url;
                } else if (MainActivity.selected_item.equals("اقل سعر")) {
                    current_url = Urls.price2_url;
                } else if (MainActivity.selected_item.equals("الاقرب اليك")) {
                    current_url = Urls.nearby_url;
                }
                loadhomeData();
                return;
            }
        }

//        search_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String txt = search_ed.getText().toString();
//                if (!TextUtils.isEmpty(txt)) {
//                    searchData(txt);
//                }
//            }
//        });
        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();

                ArrayList<HomeModel> filteredList = new ArrayList<>();
                ArrayList<ProdImagesModel> filtered_imageslist = new ArrayList<>();

                for (int i = 0; i < home_data.size(); i++) {
                    if (home_data.get(i).getFamily_name().toLowerCase().contains(s)) {

                        filteredList.add(home_data.get(i));
                        filtered_imageslist.add(images_list.get(i));
                    }
                }
                if (home_data.size() > 0) {
                    no_data_txt.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    home_recycler.setVisibility(View.VISIBLE);


                    adapter = new HomeAdapter(getActivity(), filteredList, filtered_imageslist);
                    home_recycler.setAdapter(adapter);
                } else {
                    no_data_txt.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    home_recycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadhomeData();
    }

    private void searchData(final String txt) {
        home_data.clear();
        images_list.clear();
        home_recycler.setAdapter(null);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest search_request = new StringRequest(Request.Method.POST, Urls.search_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            String product_id = jsonObject.getString("Product_ID");
                            String Name = jsonObject.getString("Name");
                            String img = jsonObject.getString("img");
                            String img2 = jsonObject.getString("img2");
                            String img3 = jsonObject.getString("img3");
                            String img4 = jsonObject.getString("img4");
                            String img5 = jsonObject.getString("img5");
                            String food_type = jsonObject.getString("FoodType");
                            String time = jsonObject.getString("Timeee");
                            String price = jsonObject.getString("Price");
                            String desc = jsonObject.getString("Description");
                            String Customer_id = jsonObject.getString("Customer_id");
                            String customer_name = jsonObject.getString("customer_name");
                            String customer_mail = jsonObject.getString("customer_mail");
                            String rate = jsonObject.getString("rate");
                            images_list.add(new ProdImagesModel(img, img2, img3, img4, img5));
                            home_data.add(new HomeModel(img, product_id, Name, food_type, rate, price, desc, time, Customer_id, customer_name, customer_mail));
                        }
                    }

                    if (home_data.size() > 0) {
                        no_data_txt.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        home_recycler.setVisibility(View.VISIBLE);
                        home_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        home_recycler.setHasFixedSize(true);
                        adapter = new HomeAdapter(getActivity(), home_data, images_list);
                        home_recycler.setAdapter(adapter);
                    } else {
                        no_data_txt.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        home_recycler.setVisibility(View.GONE);
                    }

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
                params.put("gsearch", txt);
                return params;
            }
        };
        requestQueue.add(search_request);
    }

    private void loadhomeData() {
        home_data.clear();
        images_list.clear();
        final StringRequest home_request = new StringRequest(Request.Method.GET, current_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final String product_id = jsonObject.getString("Product_ID");
                        final String name = jsonObject.getString("Name");
                        final String Price = jsonObject.getString("Price");
                        final String rate = jsonObject.getString("star");
                        final String desc = jsonObject.getString("Description");
                        final String time = jsonObject.getString("Timeee");
                        final String FoodType = jsonObject.getString("FoodType");
                        final String img1 = jsonObject.getString("img");
                        final String img2 = jsonObject.getString("img2");
                        final String img3 = jsonObject.getString("img3");
                        final String img4 = jsonObject.getString("img4");
                        final String img5 = jsonObject.getString("img5");
                        final String seller_id = jsonObject.getString("Customer_id");
                        final String seller_name = jsonObject.getString("customer_name");
                        final String seller_mail = jsonObject.getString("customer_mail");
                        StringRequest family_data_request = new StringRequest(Request.Method.POST, Urls.family_Data_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println("main_response: " + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        address[0] = jsonObject.getString("Address");
                                    }
                                    if (!MainActivity.customer_id.equals(seller_id)) {
                                        images_list.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                                        home_data.add(new HomeModel(img1, product_id, name, FoodType, rate, Price, desc, time, seller_id, seller_name, seller_mail, address[0]));
                                    }
                                    if (home_data.size() > 0) {
                                        no_data_txt.setVisibility(View.GONE);
                                        home_recycler.setVisibility(View.VISIBLE);

                                        adapter = new HomeAdapter(getActivity(), home_data, images_list);
                                        home_recycler.setAdapter(adapter);
                                    } else {
                                        no_data_txt.setVisibility(View.VISIBLE);
                                        home_recycler.setVisibility(View.GONE);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (error instanceof TimeoutError) {
                                    Toasty.error(getActivity(), getString(R.string.time_out), 1500).show();
                                } else if (error instanceof NoConnectionError)
                                    Toasty.error(getActivity(), getString(R.string.no_connection), 1500).show();
                                else if (error instanceof ServerError)
                                    Toasty.error(getActivity(), getString(R.string.server_error), 1500).show();
                                else if (error instanceof NetworkError)
                                    Toasty.error(getActivity(), getString(R.string.no_connection), 1500).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("ID", seller_id);
                                return params;
                            }
                        };
                        Volley.newRequestQueue(getActivity()).add(family_data_request);
                    }
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(getActivity()).add(home_request);
    }

    public void horizontal_search_filter(final String meal_name_filter) {
        home_data.clear();
        images_list.clear();
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest home_request = new StringRequest(Request.Method.POST, Urls.horizontal_search_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        final boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            final String product_id = jsonObject.getString("Product_ID");
                            final String name = jsonObject.getString("Name");
                            final String Price = jsonObject.getString("Price");
                            final String rate = jsonObject.getString("star");
                            final String desc = jsonObject.getString("Description");
                            final String time = jsonObject.getString("Timeee");
                            final String img1 = jsonObject.getString("img");
                            final String img2 = jsonObject.getString("img2");
                            final String img3 = jsonObject.getString("img3");
                            final String img4 = jsonObject.getString("img4");
                            final String img5 = jsonObject.getString("img5");
                            final String FoodType = jsonObject.getString("FoodType");
                            String ready = jsonObject.getString("ready");
                            final String seller_id = jsonObject.getString("Customer_id");
                            final String seller_name = jsonObject.getString("customer_name");
                            final String seller_mail = jsonObject.getString("customer_mail");

                            StringRequest family_data_request = new StringRequest(Request.Method.POST, Urls.family_Data_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            address[0] = jsonObject.getString("Address");
                                        }
                                        if (!MainActivity.customer_id.equals(seller_id)) {
                                            images_list.add(new ProdImagesModel(img1, img2, img3, img4, img5));
                                            home_data.add(new HomeModel(img1, product_id, name, FoodType, rate, Price, desc, time, seller_id, seller_name, seller_mail, address[0]));
                                        }
                                        if (home_data.size() > 0) {
                                            no_data_txt.setVisibility(View.GONE);
                                            progressBar.setVisibility(View.GONE);
                                            home_recycler.setVisibility(View.VISIBLE);

                                            adapter = new HomeAdapter(getActivity(), home_data, images_list);
                                            home_recycler.setAdapter(adapter);
                                        }

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
                                    params.put("ID", seller_id);
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(getActivity()).add(family_data_request);

                        }
                    }
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
                params.put("gsearch", meal_name_filter);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(home_request);
    }
}
