package com.homFood.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ma7MouD on 4/25/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    Context context;
    List<OrdersModel> list;

    public OrdersAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrdersAdapter.ViewHolder holder, final int position) {

        Picasso.with(context).load(Urls.base_Images_Url + list.get(position).getOrder_img()).into(holder.imageView);
        holder.order_item_name.setText(list.get(position).getOrder_name());
        try {
            float one_item_price = Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity());
            holder.order_item_price.setText(one_item_price + " ريال ");
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        holder.order_item_price.setText(list.get(position).getOrder_price() + " ريال ");
        holder.order_item_quantity.setText(list.get(position).getOrder_quantity());
        holder.order_item_trader_name.setText(list.get(position).getSeller_name());

//        if (list.get(position).getState_type().equals("0")) {
//            holder.order_state.setText("قيد الانتظار");
//        } else if (list.get(position).getState_type().equals("1")) {
//            holder.order_state.setText("تم قبول طلبك");
//        } else if (list.get(position).getState_type().equals("2")) {
//            holder.order_state.setText("تم تجهيز طلبك");
//        } else if (list.get(position).getState_type().equals("3")) {
//            holder.order_state.setText("تم انهاء طلبك");
//        } else if (list.get(position).getState_type().equals("4")) {
//            holder.order_state.setText("تم رفض طلبك");
//        }
        holder.order_item_totalprice.setText("" + Float.parseFloat(list.get(position).getOrder_price()) * Integer.parseInt(list.get(position).getOrder_quantity()) + " ريال ");
//        holder.order_item_totalprice.setText(list.get(position).getOrder_price()+ " ريال ");

//        holder.order_item_another_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StringRequest again_order_request = new StringRequest(Request.Method.POST, order_url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) {
//                                Toast.makeText(context, "تم اضافة المنتج الى السلة", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//
//                        Map<String, String> params = new HashMap<>();
//                        params.put("Product_Name", list.get(position).getOrder_name());
//                        params.put("Product_img", list.get(position).getOrder_img());
//                        params.put("FoodType", list.get(position).getOrder_foodtype());
//                        params.put("Quantity", list.get(position).getOrder_quantity());
//                        params.put("code", MainActivity.customer_id + random_num);
//                        params.put("Price", list.get(position).getOrder_price());
//                        params.put("Seller_id", list.get(position).getSeller_id());
//                        params.put("Seller_name", list.get(position).getSeller_name());
//                        params.put("Seller_mail", list.get(position).getSeller_mail());
//                        params.put("Customer_id", list.get(position).getCustomer_id());
//                        params.put("Customer_name", list.get(position).getCustomer_name());
//                        params.put("Customer_mail", list.get(position).getCustomer_mail());
//                        params.put("Customer_phone", list.get(position).getCustomer_phone());
//                        return params;
//                    }
//                };
//                Volley.newRequestQueue(context).add(again_order_request);
//            }
//        });
        holder.linearLayout.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView order_item_name, order_item_price, order_item_quantity, order_item_totalprice, order_state, item_quantity, order_item_trader_name;
        LinearLayout linearLayout;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.order_item_layout_id);
            imageView = itemView.findViewById(R.id.order_item_image);
            order_item_name = itemView.findViewById(R.id.order_item_Name);
            order_item_price = itemView.findViewById(R.id.order_item_price);
            order_item_quantity = itemView.findViewById(R.id.order_item_quantity);
            order_item_totalprice = itemView.findViewById(R.id.order_item_price);
            item_quantity = itemView.findViewById(R.id.order_item_quantity);
            order_item_trader_name = itemView.findViewById(R.id.order_item_trader_name);

            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();

            final Dialog pass_Dialog = new Dialog(context);
            pass_Dialog.setContentView(R.layout.order_detail_popup);
            TextView close_txt = pass_Dialog.findViewById(R.id.txt_close);
            LinearLayout user_order_detail_layout = pass_Dialog.findViewById(R.id.usr_order_detail_layout_id);
            LinearLayout dealer_order_detail_layout = pass_Dialog.findViewById(R.id.dealer_order_detail_layout_id);
            TextView order_accepted_txt = pass_Dialog.findViewById(R.id.order_accept_txt_id);
            TextView order_accepted_circle = pass_Dialog.findViewById(R.id.order_accept_circle_id);
            TextView order_rejected_txt = pass_Dialog.findViewById(R.id.order_rejected_txt_id);
            TextView order_rejected_circle = pass_Dialog.findViewById(R.id.order_rejected_circle_id);
            TextView order_complete_txt = pass_Dialog.findViewById(R.id.order_complete_txt_id);
            TextView order_complete_circle = pass_Dialog.findViewById(R.id.order_complete_circle_id);
            TextView order_way_in_txt = pass_Dialog.findViewById(R.id.order_inWay_txt_id);
            TextView order_way_in_circle = pass_Dialog.findViewById(R.id.order_inWay_circle_id);
            TextView order_delivered_success_txt = pass_Dialog.findViewById(R.id.order_delivered_success_txt_id);
            TextView order_delivered_success_circle = pass_Dialog.findViewById(R.id.order_delivered_success_circle_id);
            final Button received_btn = pass_Dialog.findViewById(R.id.order_received_btn_id);

            pass_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            pass_Dialog.show();
            pass_Dialog.setCancelable(false);
            close_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pass_Dialog.dismiss();
                }
            });

            if (list.get(position).getState_type().equals("0")) {
                order_accepted_circle.setBackgroundResource(R.drawable.fill_primary_circle);
                order_rejected_circle.setBackgroundResource(R.drawable.white_circle);
                order_complete_circle.setBackgroundResource(R.drawable.white_circle);
                order_way_in_circle.setBackgroundResource(R.drawable.white_circle);
                order_delivered_success_circle.setBackgroundResource(R.drawable.white_circle);
                order_accepted_txt.setTextColor(Color.parseColor("#EF582F"));
                order_rejected_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_complete_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_way_in_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_delivered_success_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                received_btn.setEnabled(false);
            } else if (list.get(position).getState_type().equals("1")) {
                order_accepted_circle.setBackgroundResource(R.drawable.white_circle);
                order_rejected_circle.setBackgroundResource(R.drawable.white_circle);
                order_complete_circle.setBackgroundResource(R.drawable.fill_primary_circle);
                order_way_in_circle.setBackgroundResource(R.drawable.white_circle);
                order_delivered_success_circle.setBackgroundResource(R.drawable.white_circle);
                order_accepted_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_rejected_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_complete_txt.setTextColor(Color.parseColor("#EF582F"));
                order_way_in_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_delivered_success_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                received_btn.setEnabled(false);
            } else if (list.get(position).getState_type().equals("2")) {
                order_accepted_circle.setBackgroundResource(R.drawable.white_circle);
                order_rejected_circle.setBackgroundResource(R.drawable.white_circle);
                order_complete_circle.setBackgroundResource(R.drawable.white_circle);
                order_delivered_success_circle.setBackgroundResource(R.drawable.white_circle);
                order_way_in_circle.setBackgroundResource(R.drawable.fill_primary_circle);
                order_accepted_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_rejected_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_complete_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_delivered_success_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_way_in_txt.setTextColor(Color.parseColor("#EF582F"));
                received_btn.setEnabled(true);
            } else if (list.get(position).getState_type().equals("3")) {
                order_accepted_circle.setBackgroundResource(R.drawable.white_circle);
                order_rejected_circle.setBackgroundResource(R.drawable.white_circle);
                order_complete_circle.setBackgroundResource(R.drawable.white_circle);
                order_way_in_circle.setBackgroundResource(R.drawable.white_circle);
                order_delivered_success_circle.setBackgroundResource(R.drawable.green_circle);
                order_accepted_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_rejected_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_complete_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_way_in_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_delivered_success_txt.setTextColor(Color.parseColor("#0AB29B"));
                received_btn.setEnabled(true);
            } else if (list.get(position).getState_type().equals("4")) {
                order_accepted_circle.setBackgroundResource(R.drawable.white_circle);
                order_rejected_circle.setBackgroundResource(R.drawable.fill_primary_circle);
                order_complete_circle.setBackgroundResource(R.drawable.white_circle);
                order_way_in_circle.setBackgroundResource(R.drawable.white_circle);
                order_delivered_success_circle.setBackgroundResource(R.drawable.white_circle);
                order_accepted_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_rejected_txt.setTextColor(Color.parseColor("#EF582F"));
                order_complete_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_way_in_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                order_delivered_success_txt.setTextColor(context.getResources().getColor(R.color.gray_color));
                received_btn.setEnabled(true);
            }

//            received_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Handle click Here ....
//                    order_status = 3;
//                    pass_Dialog.dismiss();
//                    change_process(order_status + "", Home.user_hash, list.get(pos).getId() + "");
//                    received_btn.setEnabled(false);
//                }
//            });
//            order_completed_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    order_status = 1;
//                    pass_Dialog.dismiss();
//                    change_process(order_status + "", Home.user_hash, list.get(pos).getId() + "");
//                    order_completed_btn.setEnabled(false);
//                    order_onWay_btn.setEnabled(true);
//                }
//            });
//            order_onWay_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    order_status = 2;
//                    pass_Dialog.dismiss();
//                    change_process(order_status + "", Home.user_hash, list.get(pos).getId() + "");
//                    order_completed_btn.setEnabled(false);
//                    order_onWay_btn.setEnabled(false);
//                }
//            });
        }
    }
}