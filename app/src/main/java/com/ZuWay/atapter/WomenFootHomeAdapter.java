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

public class WomenFootHomeAdapter extends RecyclerView.Adapter<WomenFootHomeAdapter.Holder>{

    private Context context;
    private List<HotdealModel> womenfootmodellist;
    private WomenFootHomeAdapter.CategoryAdapterCallback callback;

    public WomenFootHomeAdapter(Context context, List<HotdealModel> womenfootmodellist) {
        this.context = context;
        this.womenfootmodellist = womenfootmodellist;
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
    public WomenFootHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        WomenFootHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private WomenFootHomeAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        WomenFootHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner_home, viewGroup, false);
        viewHolder = new WomenFootHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WomenFootHomeAdapter.Holder holder, final int position) {

        final HotdealModel model = womenfootmodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());


    }

    @Override
    public int getItemCount() {
        return womenfootmodellist == null ? 0 : womenfootmodellist.size();
    }

    public void setCallback(WomenFootHomeAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


