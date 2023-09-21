package com.ZuWay.atapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.R;
import com.ZuWay.activity.ItemListActivity;
import com.ZuWay.activity.SubCategoryActivity;
import com.ZuWay.model.ExpandableHeightGridView;
import com.ZuWay.model.HomeTitleGridModel;
import com.ZuWay.model.HomeTitleModel;
import com.ZuWay.model.Productmodel;

import java.util.ArrayList;
import java.util.List;

public class HomeTitleAdapter extends RecyclerView.Adapter<HomeTitleAdapter.MailViewHolder> {

    private Context mContext;
    List<HomeTitleModel> HomeTitleModelList;
    String scid, cid;
    Integer i;
    String title;
    TraditionalGritHomeAdapter traditionalGritHomeAdapter;


    public HomeTitleAdapter(Context mContext, List<HomeTitleModel> HomeTitleModelList) {
        this.mContext = mContext;
        this.HomeTitleModelList = HomeTitleModelList;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title_home, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MailViewHolder holder, final int position) {

        final HomeTitleModel model = HomeTitleModelList.get(position);
        title = HomeTitleModelList.get(position).getHome_title();
        cid = HomeTitleModelList.get(position).getHome_title_id();

        if (model.getHome_title().equalsIgnoreCase("Today Best Offers")) {
            holder.tvviewmore.setVisibility(View.GONE);
        }

        holder.tvMenuTitle.setText(HomeTitleModelList.get(position).getHome_title());

        ArrayList<String> gridtitlearray = new ArrayList<String>();
        ArrayList<String> gridimgarray = new ArrayList<String>();
        final List<Productmodel> homelist = new ArrayList<>();
        final List<HomeTitleGridModel> homegritModelList = HomeTitleModelList.get(position).getHomegrid_title();
        for (HomeTitleGridModel homeTitleGridModel : homegritModelList) {

            Productmodel categoryModel = new Productmodel();

            categoryModel.setCate_name(homeTitleGridModel.getWeb_title());
            categoryModel.setCate_img(homeTitleGridModel.getWeb_image());
            categoryModel.setCate_id(homeTitleGridModel.getCid());
            categoryModel.setSubcate_id(homeTitleGridModel.getScid());

            homelist.add(categoryModel);
        }

        traditionalGritHomeAdapter = new TraditionalGritHomeAdapter(mContext, R.layout.layout_hotdeals_home, homelist);
        holder.hometitlegirt.setAdapter(traditionalGritHomeAdapter);
        holder.hometitlegirt.setNestedScrollingEnabled(false);
        holder.hometitlegirt.setExpanded(true);

        holder.hometitlegirt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Productmodel model = homelist.get(position);
                scid = model.getSubcate_id();
                cid = model.getCate_id();
                ItemListActivity.productmodel = model;
                Intent intent = new Intent(mContext, ItemListActivity.class);
                intent.putExtra("scid", scid);
                intent.putExtra("cid", cid);
                intent.putExtra("keyword", "");
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);


            }
        });

        holder.tvviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                holder.tvviewmore.startAnimation(animFadein);

                cid = HomeTitleModelList.get(position).getHome_title_id();
                Intent intent = new Intent(mContext, SubCategoryActivity.class);
                intent.putExtra("cid", cid);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return HomeTitleModelList.size();
    }

    public class MailViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuTitle, tvviewmore;
        ExpandableHeightGridView hometitlegirt;


        public MailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMenuTitle = itemView.findViewById(R.id.home_title);
            tvviewmore = itemView.findViewById(R.id.home_view);
            hometitlegirt = itemView.findViewById(R.id.home_grid);

        }
    }
}
