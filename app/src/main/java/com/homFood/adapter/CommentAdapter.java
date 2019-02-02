package com.homFood.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.homFood.R;
import com.homFood.model.CommentsModel;

import java.util.ArrayList;
/**
 * Created by Ma7MouD on 7/11/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<CommentsModel> comments_list;

    public CommentAdapter(Context context, ArrayList comments_list) {
        this.context = context;
        this.comments_list = comments_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(comments_list.get(position).getUsername());
        holder.comment_txt.setText(comments_list.get(position).getComment_txt());
        float d = Float.parseFloat(comments_list.get(position).getRate());
        holder.comment_rating.setRating(d);
    }

    @Override
    public int getItemCount() {
        return comments_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, comment_txt;
        RatingBar comment_rating;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.comment_usrname_txt);
            comment_txt = itemView.findViewById(R.id.user_comment_txt);
            comment_rating = itemView.findViewById(R.id.user_comment_rate);
        }
    }
}
