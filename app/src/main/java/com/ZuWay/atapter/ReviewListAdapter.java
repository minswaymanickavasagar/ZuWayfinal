package com.ZuWay.atapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.R;
import com.ZuWay.model.Productmodel;
import com.bumptech.glide.Glide;

import java.util.List;


public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.Holder>{

    private Context context;
    private List<Productmodel> categoryModelList;
    private CategoryAdapterCallback callback;

    public ReviewListAdapter(Context context, List<Productmodel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private LinearLayout llParent;
        TextView name,date,review,name1;
        RatingBar ratingBar;
        private final Context context;

        private Holder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            review = itemView.findViewById(R.id.review);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            name1 = itemView.findViewById(R.id.name1);


        }
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        Holder viewHolder;
        View v1 = inflater.inflate(R.layout.review_list, viewGroup, false);
        viewHolder = new Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final Productmodel model = categoryModelList.get(position);
        holder.name.setText(model.getName());
        holder.date.setText(model.getCommand_date());
        holder.review.setText(model.getComment());
        String rating1= (model.getReview_status());
        holder.ratingBar.setRating(Float.parseFloat(rating1));
        String upperString = model.getName().substring(0, 1).toUpperCase() + model.getName().substring(1).toLowerCase();

        holder.name1.setText(upperString);

/*
        String url = model.getCate_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.cate_gray)
                .into(holder.ivMenu);

        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.categoryItem(position, model);
                }
            }
        });*/

    }



    @Override
    public int getItemCount() {
        return categoryModelList == null ? 0 : categoryModelList.size();
    }

    public void setCallback(CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, Productmodel model);
    }




}


