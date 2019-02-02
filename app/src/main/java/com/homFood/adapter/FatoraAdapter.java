package com.homFood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.FatoraModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 5/13/2018.
 */

public class FatoraAdapter extends RecyclerView.Adapter<FatoraAdapter.ViewHolder> {

    Context context;
    ArrayList<FatoraModel> list;

    public FatoraAdapter(Context context, ArrayList<FatoraModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public FatoraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.fatora_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FatoraAdapter.ViewHolder holder, int position) {

        holder.prod_name.setText(list.get(position).getProduct_name());
        holder.prod_quantity.setText(list.get(position).getQuantity());
        holder.total_price.setText("" + Float.parseFloat(list.get(position).getPrice()) * Integer.parseInt(list.get(position).getQuantity()) + " ريال ");
        holder.foodType.setText(list.get(position).getFood_type());
        Picasso.with(context).load(Urls.base_Images_Url + list.get(position).getProduct_img()).into(holder.prod_image);
//        holder.prod_price.setText( " ريال ");
        holder.prod_price.setText(list.get(position).getPrice() + " ريال ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView prod_name, prod_quantity, prod_price, foodType, total_price;
        private ImageView prod_image;

        public ViewHolder(View itemView) {
            super(itemView);

            prod_name = itemView.findViewById(R.id.fatora_prod_name);
            prod_quantity = itemView.findViewById(R.id.fatora_item_quantity);
            prod_price = itemView.findViewById(R.id.fatora_item_price);
            foodType = itemView.findViewById(R.id.fatora_prod_category);
            total_price = itemView.findViewById(R.id.fatora_item_totalprice);
            prod_image = itemView.findViewById(R.id.fatora_prod_img);
        }
    }
}
