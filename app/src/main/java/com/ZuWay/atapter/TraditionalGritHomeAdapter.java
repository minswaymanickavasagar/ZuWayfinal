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

import java.util.ArrayList;
import java.util.List;

public class TraditionalGritHomeAdapter extends ArrayAdapter<Productmodel> {
    private Context context;
    private int layoutResourceId;
    private List<Productmodel> tradigritmodellist;


    public TraditionalGritHomeAdapter(Context context, int layoutResourceId, List<Productmodel> tradigritmodellist) {
        super(context,layoutResourceId,tradigritmodellist);
        this.context = context;
        this.layoutResourceId=layoutResourceId;
        this.tradigritmodellist=tradigritmodellist;

    }




    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HotdealGritHomeAdapter.RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotdealGritHomeAdapter.RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.hometitle_tx);
            holder.imageItem = (ImageView) row.findViewById(R.id.hometitle_img);
            row.setTag(holder);
        } else
        {
            holder = (HotdealGritHomeAdapter.RecordHolder) row.getTag();
        }
        Productmodel item = tradigritmodellist.get(position);
        holder.txtTitle.setText(item.getCate_name());
        String url = item.getCate_img();
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.dummy_logo)
                .into(holder.imageItem);
        return row;
    }
    static class RecordHolder
    {
        TextView txtTitle;
        ImageView imageItem;
         LinearLayout llParent;
    }
}