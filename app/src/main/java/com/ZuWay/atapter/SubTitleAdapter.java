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
import com.ZuWay.activity.ItemActivity;
import com.ZuWay.activity.ItemListActivity;
import com.ZuWay.model.ExpandableHeightGridView;
import com.ZuWay.model.Productmodel;
import com.ZuWay.model.SubTitleGridModel;
import com.ZuWay.model.SubTitleModel;

import java.util.ArrayList;
import java.util.List;

public class SubTitleAdapter extends RecyclerView.Adapter<SubTitleAdapter.MailViewHolder> {

    private Context mContext;
    List<SubTitleModel> SubTitleModelList;
    String  scid,cid,title;
    Integer i;
    ArrayAdapter<String> arrayAdapter;
    Productmodel subTiGritModel;
    SubTitleGritAdapter subTitleGritAdapter;


    public SubTitleAdapter(Context mContext, List<SubTitleModel> SubTitleModelList) {
        this.mContext = mContext;
        this.SubTitleModelList = SubTitleModelList;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title_home, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MailViewHolder holder, final int position) {

        final SubTitleModel model = SubTitleModelList.get(position);
        title = SubTitleModelList.get(position).getHome_title();

        holder.tvMenuTitle.setText(SubTitleModelList.get(position).getHome_title());

        ArrayList<String> gridtitlearray = new ArrayList<String>();
        ArrayList<String> gridimgarray = new ArrayList<String>();
        List<Productmodel>sublist = new ArrayList<>();
        final List<SubTitleGridModel> subgritModelList = SubTitleModelList.get(position).getHomegrid_title();
        for (SubTitleGridModel subTitleGridModel : subgritModelList) {

            subTiGritModel=new Productmodel();
            subTiGritModel.setCate_name(subTitleGridModel.getWeb_title());
            subTiGritModel.setCate_img(subTitleGridModel.getWeb_image());
            subTiGritModel.setCate_id(subTitleGridModel.getCid());
            subTiGritModel.setSubcate_id(subTitleGridModel.getScid());
            subTiGritModel.setPid(subTitleGridModel.getPid());
            sublist.add(subTiGritModel);
        }

        subTitleGritAdapter = new SubTitleGritAdapter(mContext, R.layout.layout_hotdeals_home, sublist);
        holder.hometitlegirt.setAdapter(subTitleGritAdapter);
        holder.hometitlegirt.setNestedScrollingEnabled(false);
        holder.hometitlegirt.setExpanded(true);

      /*  holder.hometitlegirt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cid=subTiGritModel.getCate_id();
                Productmodel model1 = sublist.get(position);
                ItemActivity.productmodel = model1;
                cid=subTiGritModel.getCate_id();
                scid=subTiGritModel.getSubcate_id();
                String pid = subTiGritModel.getPid();
                Intent intent = new Intent(mContext, ItemActivity.class);
                intent.putExtra("scid", scid);
                intent.putExtra("cid", cid);
                intent.putExtra("pid", pid);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });*/

        holder.tvviewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cid=subTiGritModel.getCate_id();

                    Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    holder.tvviewmore.startAnimation(animFadein);
                    scid = SubTitleModelList.get(position).getHome_title_scid();
                    cid=subTiGritModel.getCate_id();
                    scid=subTiGritModel.getSubcate_id();
                    Intent intent = new Intent(mContext, ItemListActivity.class);
                    intent.putExtra("scid", scid);
                    intent.putExtra("cid", cid);
                    intent.putExtra("keyword", "");

               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return SubTitleModelList.size();
    }

    public class MailViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenuTitle, tvviewmore;
        ExpandableHeightGridView hometitlegirt;

        public MailViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMenuTitle   = itemView.findViewById(R.id.home_title);
            tvviewmore    = itemView.findViewById(R.id.home_view);
            hometitlegirt = itemView.findViewById(R.id.home_grid);

        }
    }
}
