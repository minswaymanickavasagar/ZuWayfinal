package com.ZuWay.atapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.HotdealModel;

import java.util.List;

public class FashionHomeAdapter extends RecyclerView.Adapter<FashionHomeAdapter.Holder> {

    private Context context;
    private List<HotdealModel> fashionmodellist;
    private CategoryAdapterCallback callback;

    public FashionHomeAdapter(Context context, List<HotdealModel> fashionmodellist) {
        this.context = context;
        this.fashionmodellist = fashionmodellist;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private CardView tradicv;
        public ImageView ivMenu;

        private Holder(View itemView) {
            super(itemView);
            tradicv = itemView.findViewById(R.id.tradicv);
            ivMenu = itemView.findViewById(R.id.banner_im);
        }
    }

    @NonNull
    @Override
    public FashionHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FashionHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        FashionHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner_home, viewGroup, false);
        viewHolder = new FashionHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final HotdealModel model = fashionmodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());


    }

    @Override
    public int getItemCount() {
        return fashionmodellist == null ? 0 : fashionmodellist.size();
    }

    public void setCallback(CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


