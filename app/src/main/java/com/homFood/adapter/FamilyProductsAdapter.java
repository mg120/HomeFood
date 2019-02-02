package com.homFood.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.HomeModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 4/12/2018.
 */

public class FamilyProductsAdapter extends BaseAdapter {

    Context mcontect;
    ArrayList<HomeModel> list;

    public FamilyProductsAdapter(Context mcontect, ArrayList<HomeModel> list) {
        this.mcontect = mcontect;
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
        view = LayoutInflater.from(mcontect).inflate(R.layout.family_prod_list_item, viewGroup, false);
        Typeface custom_font = Typeface.createFromAsset(mcontect.getAssets(), "fonts/font.ttf");
        ImageView imageView = view.findViewById(R.id.product_img);
        TextView title = view.findViewById(R.id.product_title);
        title.setTypeface(custom_font);
        TextView description = view.findViewById(R.id.product_desc);
        description.setTypeface(custom_font);
        TextView price = view.findViewById(R.id.product_price);
        price.setTypeface(custom_font);
        RatingBar ratingBar = view.findViewById(R.id.product_rate);

        //  set Data to items ....
        if (list != null) {
            Picasso.with(mcontect).load(Urls.base_Images_Url + list.get(i).getMeal_image()).into(imageView);
            title.setText(list.get(i).getFamily_name());
            description.setText(list.get(i).getDesc());
            if (list.get(i).getFamily_rate().equals("0") || list.get(i).getFamily_rate().equals("")) {
                ratingBar.setRating(0);
            } else {
                ratingBar.setRating(Float.parseFloat(list.get(i).getFamily_rate()));
            }

            price.setText(list.get(i).getPrice());
        }
        return view;
    }
}
