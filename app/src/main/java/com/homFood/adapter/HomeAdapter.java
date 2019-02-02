package com.homFood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.activities.Details;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 4/9/2018.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    Context context;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;

    public HomeAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder holder, final int position) {
        Picasso.with(context).load(Urls.base_Images_Url + list.get(position).getMeal_image()).into(holder.item_image);
        holder.item_name.setText(list.get(position).getFamily_name());
        holder.market_name.setText(list.get(position).getSeller_name());
        holder.item_city.setText(list.get(position).getAddress());

        try {
            if (!list.get(position).getFamily_rate().equals("")) {
                float d = Float.parseFloat(list.get(position).getFamily_rate()) * 5 / 100;
                holder.item_rate.setRating(d);
            }
        } catch (NumberFormatException ex) { // handle your exception
            ex.printStackTrace();
        }

        holder.home_cardview.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView item_image;
        private TextView item_name, market_name, item_city;
        private RatingBar item_rate;
        private CardView home_cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
            home_cardview = itemView.findViewById(R.id.home_item_card_id);
            item_image = itemView.findViewById(R.id.home_item_image);
            item_name = itemView.findViewById(R.id.home_prod_name_txt_id);
            market_name = itemView.findViewById(R.id.home_market_name_id);
            item_rate = itemView.findViewById(R.id.home_item_rating);
            item_city = itemView.findViewById(R.id.home_item_location_txt_id);

            home_cardview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            Intent intent = new Intent(context, Details.class);
            intent.putExtra("Imageslist", images_list.get(pos));
            intent.putExtra("product_data", list.get(pos));
            context.startActivity(intent);

        }
    }
}
