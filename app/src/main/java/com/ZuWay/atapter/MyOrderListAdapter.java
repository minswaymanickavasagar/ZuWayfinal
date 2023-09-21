package com.ZuWay.atapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.Productmodel;

import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.Holder> {

    private Context context;
    private List<Productmodel> categoryModelList;
    private MyOrderListAdapter.CategoryAdapterCallback callback;


    public MyOrderListAdapter(Context context, List<Productmodel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView item_name, item_deleverydate, order_id;
        ImageView item_image;
        RatingBar item_rating;
        CardView cardView;

        private Holder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.mrl_product_price);
            item_deleverydate = itemView.findViewById(R.id.orderdate);
            order_id = itemView.findViewById(R.id.orderid);
            item_image = itemView.findViewById(R.id.improduct);
            item_rating = itemView.findViewById(R.id.ratingBar);
            cardView = itemView.findViewById(R.id.cv_card);
        }
    }

    @NonNull
    @Override
    public MyOrderListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyOrderListAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private MyOrderListAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        MyOrderListAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.myorder_layout, viewGroup, false);
        viewHolder = new MyOrderListAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyOrderListAdapter.Holder holder, final int position) {

        final Productmodel model = categoryModelList.get(position);

        holder.item_name.setText("₹ " + model.getProduct_total_price() + ".00");
        holder.order_id.setText(model.getOrder_id());
        holder.item_deleverydate.setText("Order on " + model.getOrder_date() + " / " + model.getOrder_time());
       /* holder.item_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                String answerValue = String.valueOf(ratingBar.getRating());
                if(ratingBar.getRating()!=0){
                    Toast.makeText(context, "Thanks for your Rating", Toast.LENGTH_SHORT).show();
                    holder.item_rating.setVisibility(View.GONE);

                }
            }
        });
*/
     /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MyOrderDetailsActivity.class);

                context.startActivity(intent);
            }
        });*/

        String url = model.getProduct_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.cate_gray)
                .into(holder.item_image);


    }

    @Override
    public int getItemCount() {
        return categoryModelList == null ? 0 : categoryModelList.size();
    }

    public void setCallback(MyOrderListAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


