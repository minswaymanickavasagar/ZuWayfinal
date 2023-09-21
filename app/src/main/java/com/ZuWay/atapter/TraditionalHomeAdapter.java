package com.ZuWay.atapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.HotdealModel;

import java.util.List;

public class TraditionalHomeAdapter extends RecyclerView.Adapter<TraditionalHomeAdapter.Holder>{

    private Context context;
    private List<HotdealModel> tradimodellist;
    private CategoryAdapterCallback callback;

    public TraditionalHomeAdapter(Context context, List<HotdealModel> tradimodellist) {
        this.context = context;
        this.tradimodellist = tradimodellist;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private LinearLayout llParent;
        public ImageView ivMenu;

        private Holder(View itemView) {
            super(itemView);

            ivMenu = itemView.findViewById(R.id.banner_im);
        }
    }
    @NonNull
    @Override
    public TraditionalHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TraditionalHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }
    private Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        TraditionalHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner1_home, viewGroup, false);
        viewHolder = new TraditionalHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        final HotdealModel model = tradimodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());



    }

    @Override
    public int getItemCount() {
        return tradimodellist == null ? 0 : tradimodellist.size();
    }

    public void setCallback(CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


