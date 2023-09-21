package com.ZuWay.atapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.activity.ItemActivity;
import com.ZuWay.activity.MyOrderDetailsActivity;
import com.ZuWay.activity.ShippingandRetrun;
import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.Productmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderDeatilAdapter extends RecyclerView.Adapter<OrderDeatilAdapter.Holder> {

    private Context context;
    private List<Productmodel> categoryModelList;
    private OrderDeatilAdapter.CategoryAdapterCallback callback;
    String status,id;
    String date;

    public OrderDeatilAdapter(Context context, List<Productmodel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;


    }

    @NonNull
    @Override
    public OrderDeatilAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        OrderDeatilAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private OrderDeatilAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        OrderDeatilAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_myorder, viewGroup, false);
        viewHolder = new OrderDeatilAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDeatilAdapter.Holder holder, final int position) {

        final Productmodel model = categoryModelList.get(position);

        holder.item_name.setText(model.getProduct_name());
        holder.item_disprice.setText("₹ " + model.getProduct_disprice() + ".00");
        holder.item_oldprice.setPaintFlags(holder.item_oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.item_oldprice.setText("₹ " + model.getProduct_oldprice() + ".00");
        holder.item_saveprice.setText("₹ " + model.getProduct_saveprice() + ".00");
        holder.item_return.setText(model.getProduct_return());
        holder.item_size.setText(model.getProduct_size());
        holder.item_color.setText(model.getProduct_color());
        holder.item_qut.setText(model.getProduct_qty());



        // CURRENT DATE
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        String current_date = s.format(currentTime);
        System.out.println("---current_date"+current_date);
//


         //  GET AFTER 7 DAY DATE

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date myDate = null;
        String datee=model.getArraving_date();
        if(datee.equalsIgnoreCase("")){

        }else {
            try {
                myDate = dateFormat.parse(datee);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(myDate);
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate = calendar.getTime();
                date = dateFormat.format(newDate);
                System.out.println("---date"+date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        //

        if(model.getStatus().equalsIgnoreCase("4")){
            if(date.equalsIgnoreCase(current_date)){
                holder.return_btn.setVisibility(View.GONE);
            }else {
                holder.return_btn.setVisibility(View.VISIBLE);
            }
            System.out.println("---status"+status);
        }

        holder.return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ShippingandRetrun.class);
                intent.putExtra("order_id",model.getOrder_id());
                intent.putExtra("product_name",model.getProduct_name());
                System.out.println("---"+model.getProduct_name());
                context.startActivity(intent);
            }
        });
        String url = model.getProduct_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.list_gray)
                .into(holder.item_image);
        holder.item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Productmodel model = categoryModelList.get(position);
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("pid", model.getPid());
                intent.putExtra("cid", model.getCate_id());
                intent.putExtra("scid", model.getSubcate_id());
                //ItemActivity.productmodel = model;
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }
  /*  public static String getCalculatedDate(String date, String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        try {
            return s.format(new Date(s.parse(date).getTime()));
            String date = dateFormat.format(newDate);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("TAG", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }*/
    @Override
    public int getItemCount() {
        return categoryModelList == null ? 0 : categoryModelList.size();
    }

    public void setCallback(OrderDeatilAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView item_name, item_size, item_color, item_return, item_disprice, item_oldprice, item_saveprice, item_qut;
        ImageView item_image;
        CardView cardView;
        TextView return_btn;

        private Holder(View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.mc_product_name);
            item_size = itemView.findViewById(R.id.mc_product_size);
            item_color = itemView.findViewById(R.id.mc_product_color);
            item_return = itemView.findViewById(R.id.item_rp);
            item_disprice = itemView.findViewById(R.id.mc_product_price);
            item_oldprice = itemView.findViewById(R.id.mc_product_priceorgi);
            item_saveprice = itemView.findViewById(R.id.mc_product_save);
            item_image = itemView.findViewById(R.id.mc_product_image);
            item_qut = itemView.findViewById(R.id.item_qut);
            cardView = itemView.findViewById(R.id.cv_card);
            return_btn=itemView.findViewById(R.id.return_btn);
        }
    }

}
