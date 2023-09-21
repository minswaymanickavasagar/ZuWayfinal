package com.ZuWay.atapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.R;
import com.ZuWay.model.CategoryModel;
import com.ZuWay.model.Productmodel;

import java.util.List;

public class ColorProductAdapter extends RecyclerView.Adapter<ColorProductAdapter.Holder> {

    private Context context;
    private List<Productmodel> electrmodellist;
    private ColorProductAdapter.CategoryAdapterCallback callback;

    int row_index;

    public ColorProductAdapter(Context context, List<Productmodel> electrmodellist) {
        this.context = context;
        this.electrmodellist = electrmodellist;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView size_tx;
        LinearLayout select_ll;

        private Holder(View itemView) {
            super(itemView);

            size_tx = itemView.findViewById(R.id.size_tx);
            select_ll = itemView.findViewById(R.id.select);
        }
    }

    @NonNull
    @Override
    public ColorProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ColorProductAdapter.Holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        viewHolder = getViewHolder(viewGroup, inflater);
        return viewHolder;
    }

    private ColorProductAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
        ColorProductAdapter.Holder viewHolder;
        View v1 = inflater.inflate(R.layout.layout_size, viewGroup, false);
        viewHolder = new ColorProductAdapter.Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColorProductAdapter.Holder holder, final int position) {

        final Productmodel model = electrmodellist.get(position);

        holder.size_tx.setText(model.getProduct_color());

        holder.select_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Productmodel model = electrmodellist.get(position);

                row_index = position;
                notifyDataSetChanged();
            }
        });

        if (row_index == position) {
            holder.select_ll.setBackgroundResource(R.drawable.border_sizebg);

        } else {
            holder.select_ll.setBackgroundColor(Color.parseColor("#ffffff"));

        }


    }

    @Override
    public int getItemCount() {
        return electrmodellist == null ? 0 : electrmodellist.size();
    }

    public void setCallback(ColorProductAdapter.CategoryAdapterCallback callback) {

        this.callback = callback;
    }

    public interface CategoryAdapterCallback {
        public void categoryItem(int position, CategoryModel model);
    }
}


