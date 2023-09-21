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

public class ElectHomeAdapter extends RecyclerView.Adapter<ElectHomeAdapter.Holder> {

    private Context context;
    private List<HotdealModel> electrmodellist;
    private ElectHomeAdapter.CategoryAdapterCallback callback;

    public ElectHomeAdapter(Context context, List<HotdealModel> electrmodellist) {
        this.context = context;
        this.electrmodellist = electrmodellist;
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
    public ElectHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ElectHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private ElectHomeAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        ElectHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner2_home, viewGroup, false);
        viewHolder = new ElectHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ElectHomeAdapter.Holder holder, final int position) {

        final HotdealModel model = electrmodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());


    }

    @Override
    public int getItemCount() {
        return electrmodellist == null ? 0 : electrmodellist.size();
    }

    public void setCallback(ElectHomeAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


