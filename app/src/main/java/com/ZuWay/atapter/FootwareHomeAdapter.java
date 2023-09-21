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

public class FootwareHomeAdapter extends RecyclerView.Adapter<FootwareHomeAdapter.Holder> {

    private Context context;
    private List<HotdealModel> footwaremodellist;
    private FootwareHomeAdapter.CategoryAdapterCallback callback;

    public FootwareHomeAdapter(Context context, List<HotdealModel> footwaremodellist) {
        this.context = context;
        this.footwaremodellist = footwaremodellist;
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
    public FootwareHomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FootwareHomeAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private FootwareHomeAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        FootwareHomeAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_tradibanner_home, viewGroup, false);
        viewHolder = new FootwareHomeAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FootwareHomeAdapter.Holder holder, final int position) {

        final HotdealModel model = footwaremodellist.get(position);

        holder.ivMenu.setImageResource(model.getHotdeal_image());


    }

    @Override
    public int getItemCount() {
        return footwaremodellist == null ? 0 : footwaremodellist.size();
    }

    public void setCallback(FootwareHomeAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


