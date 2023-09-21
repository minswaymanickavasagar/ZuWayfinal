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

import com.ZuWay.R;
import com.ZuWay.model.CategoryGritModel;

import java.util.List;

public class WesternGritAdapter extends ArrayAdapter<CategoryGritModel> {
    private Context context;
    private int layoutResourceId;
    private List<CategoryGritModel> westgritmodellist;


    public WesternGritAdapter(Context context, int layoutResourceId, List<CategoryGritModel> westgritmodellist) {
        super(context, layoutResourceId, westgritmodellist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.westgritmodellist = westgritmodellist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.hometitle_tx);
            holder.dis = (TextView) row.findViewById(R.id.hotdeal_dis);
            holder.imageItem = (ImageView) row.findViewById(R.id.hometitle_img);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        CategoryGritModel item = westgritmodellist.get(position);
        holder.txtTitle.setText(item.getGrit_name());
        holder.dis.setText(item.getGrit_dis());
        holder.imageItem.setImageResource(item.getGrit_image());

        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        TextView dis;
        ImageView imageItem;
        LinearLayout llParent;
    }
}