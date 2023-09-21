package com.ZuWay.atapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ZuWay.activity.ItemActivity;
import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.Productmodel;

import java.util.ArrayList;
import java.util.List;

public class SubTitleGritAdapter extends ArrayAdapter<Productmodel> {
    private Context context;
    private int layoutResourceId;
    private List<Productmodel> tradigritmodellist;


    SubTitleGritAdapter(Context context, int layoutResourceId, List<Productmodel> tradigritmodellist) {
        super(context,layoutResourceId,tradigritmodellist);
        this.context = context;
        this.layoutResourceId=layoutResourceId;
        this.tradigritmodellist=tradigritmodellist;

    }

    @Override public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("scid", tradigritmodellist.get(position).getSubcate_id());
                intent.putExtra("cid",  tradigritmodellist.get(position).getCate_id());
                intent.putExtra("pid",  tradigritmodellist.get(position).getPid());
               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.list_gray)
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