package com.ZuWay.atapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.ZuWay.R;
import com.ZuWay.model.Productmodel;


import java.util.ArrayList;
import java.util.List;


public class FruitAdapter extends ArrayAdapter<Productmodel> {
    private Context context;
    private int resourceId;
    private List<Productmodel> items, tempItems, suggestions;

    public FruitAdapter(@NonNull Context context, int resourceId, ArrayList<Productmodel> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            final Productmodel fruit = getItem(position);
            TextView pname = (TextView) view.findViewById(R.id.pname);
            TextView pnamenk = (TextView) view.findViewById(R.id.pnamenk);

           //  pname.setText(fruit.getKeywords());
             pnamenk.setText(fruit.getProduct_name());
            System.out.println("productname="+pname);
            System.out.println("productname="+pnamenk);
            TextView pid = (TextView) view.findViewById(R.id.pid);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public Productmodel getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }


    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Productmodel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(tempItems);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Productmodel item : tempItems) {
                    if (item.getKeywords().toLowerCase().contains(filterPattern)||item.getProduct_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}


















