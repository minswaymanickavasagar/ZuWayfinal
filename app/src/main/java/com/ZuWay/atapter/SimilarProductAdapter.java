package com.ZuWay.atapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.Productmodel;

import java.util.List;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.Holder>{

    private Context context;
    private List<Productmodel> similarmodellist;
    private CategoryAdapterCallback callback;


    public SimilarProductAdapter(Context context, List<Productmodel> similarmodellist) {
        this.context = context;
        this.similarmodellist = similarmodellist;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private RelativeLayout llParent;
        TextView item_name,item_disprice,item_oldprice,item_saveprice,wishlist_count;
        ImageView item_image,wishlidt_img;
        RatingBar ratingBar;
        Context context;
        LinearLayout ll_rel;

        private Holder(View itemView) {
            super(itemView);
            context        = itemView.getContext();
            ratingBar      = itemView.findViewById(R.id.ratingBar);
            ll_rel         = itemView.findViewById(R.id.ll_rel);
            item_name      = (TextView) itemView.findViewById(R.id.item_name);
            item_disprice  = (TextView) itemView.findViewById(R.id.item_disprice);
            item_oldprice  = (TextView) itemView.findViewById(R.id.item_oldprice);
            item_saveprice = (TextView) itemView.findViewById(R.id.item_saveprice);
            item_image     = (ImageView) itemView.findViewById(R.id.item_img);

        }
    }


    @NonNull
    @Override
    public SimilarProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SimilarProductAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        SimilarProductAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_similar_list, viewGroup, false);
        viewHolder = new SimilarProductAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final Productmodel item = similarmodellist.get(position);
        holder.item_name.setText(item.getProduct_name());
        holder.item_disprice.setText("₹ "+item.getProduct_disprice()+".00");
        holder.item_oldprice.setText("₹ "+item.getProduct_oldprice()+".00");
        holder.item_saveprice.setText("save ₹"+item.getProduct_saveprice()+".00");
        holder.ratingBar.setRating(item.getProduct_ratting());


        String img=item.getProduct_img();
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.list_gray)
                .into(holder.item_image);


    }



    @Override
    public int getItemCount() {
        return similarmodellist == null ? 0 : similarmodellist.size();
    }

    public void setCallback(CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }




}


