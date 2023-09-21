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

public class MenCategoryHalfGritAdapter extends ArrayAdapter<CategoryGritModel> {
    private Context context;
    private int layoutResourceId;
    private List<CategoryGritModel> mencategritmodellist;


    public MenCategoryHalfGritAdapter(Context context, int layoutResourceId, List<CategoryGritModel> mencategritmodellist) {
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
            holder.txtTitle = (TextView) row.findViewById(R.id.hometitle_tx);
            //  holder.dis = (TextView) row.findViewById(R.id.hotdeal_dis);
            holder.imageItem = (ImageView) row.findViewById(R.id.hometitle_img);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        CategoryGritModel item = mencategritmodellist.get(position);
        holder.txtTitle.setText(item.getGrit_name());
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