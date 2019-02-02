package com.homFood.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.homFood.activities.MainActivity;
import com.homFood.activities.Messages;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Created by Ma7MouD on 8/8/2018.
 */

public class ActiveAdapter extends RecyclerView.Adapter<ActiveAdapter.ViewHolder> {

    Context context;
    List<OrdersModel> active_list;

    private int order_type = 0;

    public ActiveAdapter(Context context, List<OrdersModel> active_list) {
        this.context = context;
        this.active_list = active_list;
    }

    @Override
    public ActiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.from_mordered_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActiveAdapter.ViewHolder holder, int position) {

        Picasso.with(context).load(Urls.base_Images_Url + active_list.get(position).getOrder_img()).into(holder.item_image);
        holder.item_name.setText(active_list.get(position).getOrder_name());
        holder.item_price.setText(active_list.get(position).getOrder_price());
        holder.item_Quantity.setText(active_list.get(position).getOrder_quantity());
        holder.item_total_price.setText("" + Float.parseFloat(active_list.get(position).getOrder_price()) * Integer.parseInt(active_list.get(position).getOrder_quantity()) + " ريال ");

        if (active_list.get(position).getState_type().equals("0")) {
            holder.item_state.setText("قيد الانتظار");
        } else if (active_list.get(position).getState_type().equals("1")) {
            holder.item_state.setText("تم قبول طلبك");
        } else if (active_list.get(position).getState_type().equals("2")) {
            holder.item_state.setText("تم تجهيز طلبك");
        } else if (active_list.get(position).getState_type().equals("3")) {
            holder.item_state.setText("تم انهاء طلبك");
        } else if (active_list.get(position).getState_type().equals("4")) {
            holder.item_state.setText("تم رفض طلبك");
        }
        holder.linearLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return active_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayout;
        TextView item_name, item_price, item_Quantity, item_total_price, item_state;
        ImageView item_image;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.main_layout);
            item_name = itemView.findViewById(R.id.order_item_name);
            item_price = itemView.findViewById(R.id.order_item_price);
            item_Quantity = itemView.findViewById(R.id.order_item_quantity);
            item_total_price = itemView.findViewById(R.id.order_item_totalprice);
            item_state = itemView.findViewById(R.id.order_state);
            item_image = itemView.findViewById(R.id.order_item_img);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            final OrdersModel ordersModel = active_list.get(position);
            final Dialog dialog = new Dialog(context);
            TextView txt_close, order_user_name, order_user_email, order_user_phone;
            TextView order_item_name, order_itm_price, user_order_state;
            ImageView order_item_img;
            final Button sure_add_order, reject_order, order_completed, cancel_order, family_chat;
            LinearLayout user_state_layout, osra_state_layout, user_info_layout;
            dialog.setContentView(R.layout.order_state_popup);
            txt_close = dialog.findViewById(R.id.txt_close);
            order_user_name = dialog.findViewById(R.id.order_user_name);
            order_user_email = dialog.findViewById(R.id.order_user_email);
            order_user_phone = dialog.findViewById(R.id.order_user_phone);
            txt_close = dialog.findViewById(R.id.txt_close);
            order_item_name = dialog.findViewById(R.id.order_item_name);
            user_order_state = dialog.findViewById(R.id.user_order_state);
            order_itm_price = dialog.findViewById(R.id.order_item_price);
            order_item_img = dialog.findViewById(R.id.order_item_imgage);
            user_state_layout = dialog.findViewById(R.id.user_state_layout);
            osra_state_layout = dialog.findViewById(R.id.osra_state_layout);
            user_info_layout = dialog.findViewById(R.id.user_info);
            family_chat = dialog.findViewById(R.id.family_chat);
            sure_add_order = dialog.findViewById(R.id.accept_order);
            reject_order = dialog.findViewById(R.id.reject_order);
            order_completed = dialog.findViewById(R.id.complete_order);
            cancel_order = dialog.findViewById(R.id.cancel_order);

            txt_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            user_state_layout.setVisibility(View.GONE);
            osra_state_layout.setVisibility(View.VISIBLE);
            user_info_layout.setVisibility(View.VISIBLE);
            family_chat.setVisibility(View.VISIBLE);


            Picasso.with(context).load(Urls.base_Images_Url + ordersModel.getOrder_img()).into(order_item_img);
            order_item_name.setText(ordersModel.getOrder_name());
            order_user_name.setText(ordersModel.getCustomer_name());
            order_user_email.setText(ordersModel.getCustomer_mail());
            order_user_phone.setText(ordersModel.getCustomer_phone());
            order_itm_price.setText(ordersModel.getOrder_price() + " ريال ");

            if (ordersModel.getState_type().equals("0")) {
                user_order_state.setText("قيد الانتظار");
            } else if (ordersModel.getState_type().equals("1")) {
                user_order_state.setText("تم قبول طلبك");
            } else if (ordersModel.getState_type().equals("2")) {
                user_order_state.setText("تم تجهيز طلبك");
            } else if (ordersModel.getState_type().equals("3")) {
                user_order_state.setText("تم انهاء طلبك");
            } else if (ordersModel.getState_type().equals("4")) {
                user_order_state.setText("تم رفض طلبك");
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.show();
            //////////////////////////////////////////////////////
            family_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!MainActivity.customer_id.equals("")) {
                                dialog.dismiss();
                                Intent intent1 = new Intent(context, Messages.class);
                                intent1.putExtra("sender_id", MainActivity.customer_id);
                                intent1.putExtra("receiver_id", ordersModel.getCustomer_id());
                                intent1.putExtra("sender_name", MainActivity.Name);
                                intent1.putExtra("receiver_name", ordersModel.getCustomer_name());
                                context.startActivity(intent1);
                            } else {
                                Toast.makeText(context, "يجب تسجيل الدخول اولا", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000);
                }
            });
            sure_add_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    order_type = 1;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            reject_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 4;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            order_completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 2;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
            cancel_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    order_type = 3;
                    updateOrder_type(order_type, ordersModel.getOrder_id());
                    dialog.dismiss();
                }
            });
        }
    }

    public void updateOrder_type(final int type, final String order_id) {
        StringRequest update_order_request = new StringRequest(Request.Method.POST, Urls.update_order_type, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("response:response:: " + response);
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    boolean success = jsonObject1.getBoolean("success");
                    if (success)
                        Toasty.success(context, "تم تغييير حالة الطلب بنجاح", 1500).show();

                    ActiveAdapter.this.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Connection", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", type + "");
                params.put("Order_id", order_id);
                System.out.println("params: " + params);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(update_order_request);
    }
}
