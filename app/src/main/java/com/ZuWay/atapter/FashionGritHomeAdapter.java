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
import com.ZuWay.model.CategoryModel;

import java.util.List;

public class FashionGritHomeAdapter extends ArrayAdapter<CategoryModel> {
    private Context context;
    private int layoutResourceId;
    private List<CategoryModel> fashiongritmodellist;


    public FashionGritHomeAdapter(Context context, int layoutResourceId, List<CategoryModel> fashiongritmodellist) {
        super(context, layoutResourceId, fashiongritmodellist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.fashiongritmodellist = fashiongritmodellist;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HotdealGritHomeAdapter.RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HotdealGritHomeAdapter.RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.hometitle_tx);
            holder.imageItem = (ImageView) row.findViewById(R.id.hometitle_tx);
            row.setTag(holder);
        } else {
            holder = (HotdealGritHomeAdapter.RecordHolder) row.getTag();
        }
        CategoryModel item = fashiongritmodellist.get(position);
        holder.txtTitle.setText(item.getCategory_name());

        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
        LinearLayout llParent;
    }
}