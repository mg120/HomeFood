package com.homFood.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.OrdersModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ma7MouD on 8/15/2018.
 */

public class DealsAdapter extends BaseAdapter {

    Context context;
    List<OrdersModel> list;

    public DealsAdapter(Context context, List<OrdersModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.last_deals_item, viewGroup, false);
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
        ImageView imageView = view.findViewById(R.id.deals_item_img);
        TextView title = view.findViewById(R.id.deals_item_name);
        TextView deals_item_totalprice = view.findViewById(R.id.deals_item_totalprice);
        TextView price = view.findViewById(R.id.deals_item_price);
        TextView quantity = view.findViewById(R.id.deals_item_quantity);
        //  set Data to items ....
        if (list != null) {
            Picasso.with(context).load(Urls.base_Images_Url + list.get(i).getOrder_img()).into(imageView);
            title.setText(list.get(i).getOrder_name());
            deals_item_totalprice.setText(String.valueOf(Float.parseFloat(list.get(i).getOrder_price()) * Integer.parseInt(list.get(i).getOrder_quantity()) + " ريال "));
            quantity.setText(list.get(i).getOrder_quantity());
            price.setText(list.get(i).getOrder_price());
        }
        return view;
    }
}
