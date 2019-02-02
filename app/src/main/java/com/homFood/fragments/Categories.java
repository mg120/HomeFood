package com.homFood.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.activities.MainActivity;
import com.homFood.interfaces.FragmentCommunication;
import com.homFood.R;
import com.homFood.adapter.CategoriesAdapter;
import com.homFood.model.CategoriesModel;
import com.homFood.networking.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class Categories extends Fragment {

    public static List<CategoriesModel> categories_list = new ArrayList<>();
    RecyclerView categories_recycler;
    ProgressBar progressBar;
    TextView no_data_txt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(getString(R.string.categories));
        MainActivity.filter_icon.setVisible(false);
        MainActivity.cart_icon.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        categories_recycler = view.findViewById(R.id.categories_recyclerView);
        progressBar = view.findViewById(R.id.categories_progress);
        no_data_txt = view.findViewById(R.id.no_categories_data_txt_id);

        categories_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categories_recycler.setHasFixedSize(true);

        // get Categories Data ...
        categoriesData();

        return view;
    }

    FragmentCommunication communication = new FragmentCommunication() {
        @Override
        public void respond(int position, String name) {
            MainFragment mainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("NAME", name);
            mainFragment.setArguments(bundle);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_frame, mainFragment).commit();
        }
    };

    public void categoriesData() {
        categories_list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.categories_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
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
                    if (categories_list.size() > 0) {
                        no_data_txt.setVisibility(View.GONE);
                        categories_recycler.setVisibility(View.VISIBLE);
                        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), categories_list, communication);
                        categories_recycler.setAdapter(adapter);
                    } else {
                        categories_recycler.setVisibility(View.GONE);
                        no_data_txt.setVisibility(View.VISIBLE);
                    }
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
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
