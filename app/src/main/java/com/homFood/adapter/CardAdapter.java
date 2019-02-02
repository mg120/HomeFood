package com.homFood.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.homFood.R;
import com.homFood.activities.Cart;
import com.homFood.activities.Details;
import com.homFood.model.CardModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ma7MouD on 4/15/2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    Context context;
    public static List<CardModel> cart_list;

    public CardAdapter(Context context, List<CardModel> cart_list) {
        this.context = context;
        CardAdapter.cart_list = cart_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final int[] count = {Integer.parseInt(cart_list.get(position).getOrder_pro_quantity())};
        holder.cart_item_name.setText(cart_list.get(position).getOrder_pro_name());
        Picasso.with(context).load(Urls.base_Images_Url + cart_list.get(position).getOrder_pro_img()).into(holder.imageView);
        holder.count_num.setText(cart_list.get(position).getOrder_pro_quantity());
        final int[] total_price = {Integer.parseInt(cart_list.get(position).getOrder_pro_quantity()) * Integer.parseInt(cart_list.get(position).getOrder_pro_price())};
        holder.cart_item_total.setText(total_price[0] + "");
        holder.increase_count_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0] = count[0] + 1;
                cart_list.get(position).setOrder_pro_quantity(String.valueOf(count[0]));
                Log.e("count:", "clicked ...");
                Log.e("count:", count[0] + "");
                Log.e("total:", Cart.total + "");
                Log.e("total2:", String.valueOf(Cart.total + cart_list.get(position).getOrder_pro_price()));
                holder.count_num.setText(String.valueOf(count[0]));
                Cart.total = Cart.total + Integer.parseInt(cart_list.get(position).getOrder_pro_price());
                holder.cart_item_total.setText(String.valueOf(count[0] * Integer.parseInt(cart_list.get(position).getOrder_pro_price())));
                Cart.total_txtV.setText(String.valueOf(Cart.total));
                Log.e("final_total: ", Cart.total + "");
            }
        });
        holder.decrease_count_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count[0] > 1) {
                    count[0] = count[0] - 1;
                    cart_list.get(position).setOrder_pro_quantity(String.valueOf(count[0]));
                    holder.count_num.setText(String.valueOf(count[0]));
                    holder.cart_item_total.setText(String.valueOf(count[0] * Integer.parseInt(cart_list.get(position).getOrder_pro_price())));
                    Cart.total = Cart.total - Integer.parseInt(cart_list.get(position).getOrder_pro_price());
                    Cart.total_txtV.setText(String.valueOf(Cart.total));
                    Log.e("final_total: ", Cart.total + "");
                }
            }
        });
        holder.item_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("item_id", cart_list.get(position).getOrder_pro_id() + "");
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("item_id", cart_list.get(position).getOrder_pro_id());
                context.startActivity(intent);
            }
        });

        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final float one_piece_price = Float.parseFloat(cart_list.get(position).getOrder_pro_price()) / Integer.parseInt(cart_list.get(position).getOrder_pro_quantity());
                final StringRequest delete_request = new StringRequest(Request.Method.POST, Urls.delete_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                cart_list.remove(position);
                                Cart.total -= count[0] * one_piece_price;
                                CardAdapter.this.notifyDataSetChanged();
                                Toast.makeText(context, "تم حذف المنتج", Toast.LENGTH_SHORT).show();
                                if (cart_list.size() == 0) {
                                    Cart.cart_layout.setVisibility(View.GONE);
                                    Cart.empty_layout.setVisibility(View.VISIBLE);
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
                        params.put("Order_id", cart_list.get(position).getOrder_pro_id());
                        System.out.println("params:: " + params);
                        return params;
                    }
                };
                Volley.newRequestQueue(context).add(delete_request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView count_num;
        ImageView imageView, increase_count_btn, decrease_count_btn;
        TextView cart_item_price, cart_item_name, cart_item_total;
        Button item_details, item_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cart_item_img);
            cart_item_name = itemView.findViewById(R.id.item_name_txt_id);
            increase_count_btn = itemView.findViewById(R.id.cart_increase_img);
            decrease_count_btn = itemView.findViewById(R.id.cart_decrease_img);
            count_num = itemView.findViewById(R.id.cart_prod_num);
            cart_item_total = itemView.findViewById(R.id.cart_item_total);
            item_details = itemView.findViewById(R.id.cart_item_details);
            item_delete = itemView.findViewById(R.id.cart_item_delete);
        }
    }
}