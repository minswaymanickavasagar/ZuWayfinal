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

public class HotdealGritHomeAdapter extends ArrayAdapter<CategoryModel> {

    private Context context;
    int layoutResourceId;
    private List<CategoryModel> hotdealgritModelList;


    public HotdealGritHomeAdapter(Context context, int layoutResourceId, List<CategoryModel> hotdealgritModelList) {
        super(context, layoutResourceId, hotdealgritModelList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.hotdealgritModelList = hotdealgritModelList;
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
            holder.imageItem = (ImageView) row.findViewById(R.id.hometitle_img);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        CategoryModel item = hotdealgritModelList.get(position);
        holder.txtTitle.setText(item.getCategory_name());
        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
    }
}





