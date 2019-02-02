package com.homFood.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.homFood.R;
import com.homFood.model.CouponsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {

    Context context;
    ArrayList<CouponsModel> list;

    public CouponsAdapter(Context context, ArrayList<CouponsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coupons_layout_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.code.setText(list.get(i).getCode());
        viewHolder.discount_val.setText(list.get(i).getDiscont_val());
        viewHolder.expire_date.setText(list.get(i).getExpire_date());

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());
//        System.out.println("date1 : " + currentDateandTime);
//        System.out.println("date2 : " + list.get(i).getExpire_date());
//
//        if (currentDateandTime.compareTo(list.get(i).getExpire_date()) > 0) {
//            System.out.println("Date1 is after Date2");
//            Toast.makeText(context, "Date1 is after Date2", Toast.LENGTH_SHORT).show();
//        } else if (currentDateandTime.compareTo(list.get(i).getExpire_date()) < 0) {
//            System.out.println("Date1 is before Date2");
//            Toast.makeText(context, "Date1 is before Date2", Toast.LENGTH_SHORT).show();
//        } else if (currentDateandTime.compareTo(list.get(i).getExpire_date()) == 0) {
//            System.out.println("Date1 is equal to Date2");
//            Toast.makeText(context, "Date1 is equal to Date2", Toast.LENGTH_SHORT).show();
//        } else {
//            System.out.println("How to get here?");
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView code, discount_val, expire_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code_txt_id);
            discount_val = itemView.findViewById(R.id.discount_val_txt_id);
            expire_date = itemView.findViewById(R.id.discount_expire_date_txt_id);
        }
    }
}
