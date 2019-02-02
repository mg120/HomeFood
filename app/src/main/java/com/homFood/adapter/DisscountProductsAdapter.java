package com.homFood.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.homFood.R;
import com.homFood.activities.CreateCouponActivity;
import com.homFood.activities.DiscountCoupons;
import com.homFood.model.HomeModel;
import com.homFood.model.ProdImagesModel;
import com.homFood.networking.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisscountProductsAdapter extends RecyclerView.Adapter<DisscountProductsAdapter.ViewHolder> {

    Context context;
    ArrayList<HomeModel> list;
    ArrayList<ProdImagesModel> images_list;
    private RadioButton lastCheckedRB = null;


    public DisscountProductsAdapter(Context context, ArrayList<HomeModel> list, ArrayList<ProdImagesModel> images_list) {
        this.context = context;
        this.list = list;
        this.images_list = images_list;
    }

    @NonNull
    @Override
    public DisscountProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_product_layout_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DisscountProductsAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(Urls.base_Images_Url + list.get(i).getMeal_image()).into(viewHolder.item_image);
        viewHolder.item_name.setText(list.get(i).getFamily_name());
        viewHolder.item_category.setText(list.get(i).getFood_type());

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked_rb = v.findViewById(R.id.dis_item_radioB_id);
//                Toast.makeText(context, "" + list.get(i).getFamily_id(), Toast.LENGTH_SHORT).show();
                CreateCouponActivity.seleceted_item_id = list.get(i).getFamily_id();
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                }
                //store the clicked radiobutton
                lastCheckedRB = checked_rb;
            }
        });

//        viewHolder.checkBox.setOnClickListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                RadioButton checked_rb = (RadioButton) buttonView.findViewById(R.id.dis_item_radioB_id);
//                Toast.makeText(context, "" + list.get(i).getFamily_id(), Toast.LENGTH_SHORT).show();
//                if (lastCheckedRB != null) {
//                    lastCheckedRB.setChecked(false);
//                }
//                //store the clicked radiobutton
//                lastCheckedRB = checked_rb;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name, item_category;
        RadioButton checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.dis_product_item_image);
            item_name = itemView.findViewById(R.id.dis_prod_name_txt_id);
            item_category = itemView.findViewById(R.id.dis_prod_category_txt_id);
            checkBox = itemView.findViewById(R.id.dis_item_radioB_id);
        }
    }
}