package com.ZuWay.atapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ZuWay.R;

import com.ZuWay.model.Productmodel;

import java.util.List;

public class CatrgoryHomeAdapter extends RecyclerView.Adapter<CatrgoryHomeAdapter.Holder>{

    private Context context;
    private List<Productmodel> categoryModelList;
    private CategoryAdapterCallback callback;

    public CatrgoryHomeAdapter(Context context, List<Productmodel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private LinearLayout llParent;
        TextView tvMenuTitle;
        ImageView ivMenu;
        private final Context context;

        private Holder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            llParent = itemView.findViewById(R.id.ll_parent);
            tvMenuTitle = itemView.findViewById(R.id.tvMenuTitle);
            ivMenu = itemView.findViewById(R.id.ivMenu);


        }
    }


    @NonNull
    @Override
    public CatrgoryHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CatrgoryHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        CatrgoryHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_categorys_home, viewGroup, false);
        viewHolder = new CatrgoryHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final Productmodel model = categoryModelList.get(position);
        holder.tvMenuTitle.setText(model.getCate_name());


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
        });

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


