package com.homFood.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.activities.Details;
import com.homFood.adapter.FamilyProductsAdapter;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FamilyProductsFrag extends Fragment {

    TextView check_txt;
    ListView products_listV;
    ProgressBar progressBar;
    FamilyProductsAdapter adapter;
    String family_id;
    private ArrayList<HomeModel> prod_data_list = new ArrayList<>();
    private ArrayList<ProdImagesModel> prod_imgs_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_family_products, container, false);
        check_txt = view.findViewById(R.id.check_products_txt);
        products_listV = view.findViewById(R.id.family_products_list);
        progressBar = view.findViewById(R.id.load_prods_progress_id);
        if (getArguments() != null) {
            family_id = getArguments().getString("family_id");
            loadFamilyproducts(family_id);
        }
        return view;
    }

    private void loadFamilyproducts(final String family_id) {
        StringRequest products_request = new StringRequest(Request.Method.POST, Urls.products_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("prod_list: " + response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data_obj = jsonArray.getJSONObject(i);
                        String Product_ID = data_obj.getString("Product_ID");
                        String Product_Name = data_obj.getString("Name");
                        String Product_img = data_obj.getString("img");
                        String Product_img2 = data_obj.getString("img2");
                        String Product_img3 = data_obj.getString("img3");
                        String Product_img4 = data_obj.getString("img4");
                        String Product_img5 = data_obj.getString("img5");
                        String foodType = data_obj.getString("FoodType");
                        String time = data_obj.getString("Timeee");
                        String Product_desc = data_obj.getString("Description");
                        String Product_rate = data_obj.getString("rate");
                        String Product_price = data_obj.getString("Price");
                        String seller_id = data_obj.getString("Customer_id");
                        String seller_name = data_obj.getString("customer_name");
                        String seller_email = data_obj.getString("customer_mail");

                        prod_imgs_list.add(new ProdImagesModel(Product_img, Product_img2, Product_img3, Product_img4, Product_img5));
                        prod_data_list.add(new HomeModel(Product_img, Product_ID, Product_Name, foodType, Product_rate, Product_price, Product_desc, time, seller_id, seller_name, seller_email));
                    }

                    if (prod_data_list.size() > 0) {
                        progressBar.setVisibility(View.GONE);
                        check_txt.setVisibility(View.GONE);
                        products_listV.setVisibility(View.VISIBLE);

                        adapter = new FamilyProductsAdapter(getActivity(), prod_data_list);
                        products_listV.setAdapter(adapter);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        check_txt.setVisibility(View.VISIBLE);
                        products_listV.setVisibility(View.GONE);
                    }
                    products_listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent1 = new Intent(getActivity(), Details.class);
                            intent1.putExtra("Imageslist", prod_imgs_list.get(i));
                            intent1.putExtra("product_data", prod_data_list.get(i));
                            startActivity(intent1);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Customer_id", family_id);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(products_request);
    }
}