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

public class BlingHomeAdapter extends RecyclerView.Adapter<BlingHomeAdapter.Holder>{

    private Context context;
    private List<HotdealModel> blingmodellist;
    private BlingHomeAdapter.CategoryAdapterCallback callback;

    public BlingHomeAdapter(Context context, List<HotdealModel> blingmodellist) {
        this.context = context;
        this.blingmodellist = blingmodellist;
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
    public BlingHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        BlingHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private BlingHomeAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        BlingHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner_home, viewGroup, false);
        viewHolder = new BlingHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BlingHomeAdapter.Holder holder, final int position) {

        final HotdealModel model = blingmodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());


    }

    @Override
    public int getItemCount() {
        return blingmodellist == null ? 0 : blingmodellist.size();
    }

    public void setCallback(BlingHomeAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


