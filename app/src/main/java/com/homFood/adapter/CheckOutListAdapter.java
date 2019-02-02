package com.homFood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.CardModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ma7MouD on 5/10/2018.
 */

public class CheckOutListAdapter extends BaseAdapter {

    Context context;
    List<CardModel> list;

    public CheckOutListAdapter(Context context, List<CardModel> list) {
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

        view = LayoutInflater.from(context).inflate(R.layout.checkout_list_item, viewGroup, false);

        final TextView item_name = view.findViewById(R.id.checkout_item_name);
        final TextView item_category = view.findViewById(R.id.checkout_item_category);
        final ImageView item_image = view.findViewById(R.id.checkout_item_img);
        final TextView item_price = view.findViewById(R.id.checkout_item_price);
        final TextView item_quantity = view.findViewById(R.id.checkout_item_quantity);
        final TextView item_total_price = view.findViewById(R.id.checkout_item_totalprice);

        final CardModel cardModel = list.get(i);
        item_name.setText(cardModel.getOrder_pro_name());
        Picasso.with(context).load(Urls.base_Images_Url + cardModel.getOrder_pro_img()).into(item_image);
        float one_item_price = Float.parseFloat(cardModel.getOrder_pro_price()) * Integer.parseInt(cardModel.getOrder_pro_quantity());
//        item_time.setText(String.valueOf( one_item_price + " ريال "));
        item_price.setText(cardModel.getOrder_pro_price());
        item_quantity.setText(cardModel.getOrder_pro_quantity());
        item_total_price.setText(String.valueOf(one_item_price));

        return view;
    }
}