package com.ZuWay.atapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.Productmodel;

import java.util.List;

public class SubcateGritAdapter extends ArrayAdapter<Productmodel> {
    private Context context;
    private int layoutResourceId;
    private List<Productmodel> mencategritmodellist;


    public SubcateGritAdapter(Context context, int layoutResourceId, List<Productmodel> mencategritmodellist) {
        super(context, layoutResourceId, mencategritmodellist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.mencategritmodellist = mencategritmodellist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.subcate_name);
            holder.imageItem =  row.findViewById(R.id.subcate_img);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        Productmodel item = mencategritmodellist.get(position);
        holder.txtTitle.setText(item.getSubcate_name());

        String url = item.getSubcate_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.dummy_logo)
                .into(holder.imageItem);



        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        TextView dis;
        ImageView imageItem;
        LinearLayout llParent;
    }
}