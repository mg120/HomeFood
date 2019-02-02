package com.homFood.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.homFood.interfaces.FragmentCommunication;
import com.homFood.R;
import com.homFood.model.CategoriesModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context context;
    List<CategoriesModel> list;
    FragmentCommunication fragmentCommunication;

    public CategoriesAdapter(Context context, List<CategoriesModel> list, FragmentCommunication communication) {
        this.context = context;
        this.list = list;
        this.fragmentCommunication = communication;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.categories_item, viewGroup, false);
        return new ViewHolder(view, fragmentCommunication);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.ViewHolder viewHolder, int i) {
        // set Data to Views ...
        Picasso.with(context).load(Urls.base_Images_Url + list.get(i).getImg()).into(viewHolder.item_image);
        viewHolder.item_name.setText(list.get(i).getName());
        viewHolder.card_item.setTag(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView card_item;
        private ImageView item_image;
        private TextView item_name;
        private RatingBar item_rating;
        FragmentCommunication mcommunicator;

        public ViewHolder(@NonNull View itemView, FragmentCommunication Communicator) {
            super(itemView);
            card_item = itemView.findViewById(R.id.category_item_card_id);
            item_image = itemView.findViewById(R.id.category_item_image);
            item_name = itemView.findViewById(R.id.category_item_name_id);
            item_rating = itemView.findViewById(R.id.category_item_rating);
            mcommunicator = Communicator;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            Bundle bundle = new Bundle();
            bundle.putString("key_word", list.get(pos).getName());
            mcommunicator.respond(pos, list.get(pos).getName());
        }
    }
}
