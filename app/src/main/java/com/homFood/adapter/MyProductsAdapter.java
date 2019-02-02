package com.homFood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.activities.EditMeal;
import com.homFood.activities.MyProducts;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ViewHolder> {

    Context context;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;

    public MyProductsAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @NonNull
    @Override
    public MyProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_products_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(Urls.base_Images_Url + list.get(i).getMeal_image()).into(viewHolder.imageView);
        viewHolder.item_name.setText(list.get(i).getFamily_name());
        viewHolder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, Urls.delete_product_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                removeAt(i);
//                                        MyProductsAdapter.this.notifyDataSetChanged();
                                Toasty.success(context, "تم حذف المنتج بنجاح", Toast.LENGTH_SHORT).show();
                                if (list.isEmpty()) {
                                    MyProducts.products_recycler.setVisibility(View.GONE);
                                    MyProducts.no_products_data.setVisibility(View.VISIBLE);
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
                        params.put("Product_ID", list.get(i).getFamily_id());
                        System.out.println(params);
                        return params;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        });

        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditMeal.class);
                intent.putExtra("product_images", images_list.get(i));
                intent.putExtra("product_data", list.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView item_name;
        Button edit_btn, delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_item_image);
            item_name = itemView.findViewById(R.id.prod_name_txt_id);
            edit_btn = itemView.findViewById(R.id.edit_product_btn_id);
            delete_btn = itemView.findViewById(R.id.delete_product_btn_id);
        }
    }
}
