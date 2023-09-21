package com.ZuWay.atapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ZuWay.activity.ItemActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ZuWay.R;
import com.ZuWay.model.Productmodel;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGritHomeAdapter extends ArrayAdapter<Productmodel> {
    String likeaction = "", pid, status = "2", pidd,cid,scid;
    int cout = 1;
    private Context context;
    private int layoutResourceId;
    private List<Productmodel> itemgritmodellist;


    public ItemGritHomeAdapter(Context context, int layoutResourceId, List<Productmodel> itemgritmodellist) {
        super(context, layoutResourceId, itemgritmodellist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.itemgritmodellist = itemgritmodellist;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RecordHolder();
            holder.llParent = row.findViewById(R.id.item_parent);
            holder.wishll = row.findViewById(R.id.wishll);
            holder.ratingBar = row.findViewById(R.id.ratingBar);
            holder.wishlist_count = (TextView) row.findViewById(R.id.wishlist_count);
            holder.item_name = (TextView) row.findViewById(R.id.item_name);
            holder.item_disprice = (TextView) row.findViewById(R.id.item_disprice);
            holder.item_oldprice = (TextView) row.findViewById(R.id.item_oldprice);
            holder.item_saveprice = (TextView) row.findViewById(R.id.item_saveprice);

            holder.item_image = (ImageView) row.findViewById(R.id.item_img);
            holder.wishlidt_img = (ImageView) row.findViewById(R.id.wish_img);
            holder.wishlidt_img = (ImageView) row.findViewById(R.id.wish_img);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        final Productmodel item = itemgritmodellist.get(position);

        holder.wishlist_count.setText(item.getProduct_withlistcount());
        holder.item_name.setText(item.getProduct_name());
        holder.item_disprice.setText(" ₹ " + item.getProduct_disprice() + ".00");

        holder.item_oldprice.setText(" ₹ " + item.getProduct_oldprice() + ".00");
        holder.item_saveprice.setText(" save ₹ " + item.getProduct_saveprice() + ".00");
        holder.ratingBar.setRating(item.getProduct_ratting());

    /*    holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Productmodel model1 = itemgritmodellist.get(position);
                pid = itemgritmodellist.get(position).getPid();
                cid = itemgritmodellist.get(position).getCate_id();
                scid = itemgritmodellist.get(position).getSubcate_id();

                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("cid",cid );
                intent.putExtra("scid",scid );
                intent.putExtra("pid",pid );
                ItemActivity.productmodel = model1;
                context.startActivity(intent);
            }
        });*/


        String img = item.getProduct_img();
        Glide.with(context)
                .load(img)
                .placeholder(R.drawable.list_gray)
                .into(holder.item_image);

        if (item.getProduct_withlistcount_status().equalsIgnoreCase("2")) {
            holder.wishlidt_img.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.wishlidt_img.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.wishlist_count.setText(item.getProduct_withlistcount());


        final RecordHolder finalHolder = holder;
        final RecordHolder finalHolder1 = holder;
        final RecordHolder finalHolder2 = holder;
        holder.wishll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                finalHolder2.wishll.startAnimation(animFadein);


                if (status.equals("2")) {
                    likeaction = "2";
                    pid = itemgritmodellist.get(position).getPid();

                    final String customer_id = BSession.getInstance().getUser_id(getContext());
                    final User user = new User();

                    final Map<String, String> params = new HashMap<>();

                    String par_str = "?user_id=" + customer_id;
                    String par_str1 = "&product_id=" + pid;
                    String par_str2 = "&status=" + likeaction;


                    String baseUrl = ProductConfig.wishlist + par_str + par_str1 + par_str2;
                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.
                            Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    ProductConfig.UserDetails = new User(jsonResponse);

                                    final Productmodel model = new Productmodel();
                                    finalHolder1.wishlist_count.setText(jsonResponse.getString("count"));
                                    pidd = jsonResponse.getString("product_id");
                                    status = jsonResponse.getString("status");

                                    String mess = jsonResponse.getString("message");
                                    if (mess.equals("Successfully liked whislist")) {
                                        finalHolder.wishlidt_img.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(context, " Thanks for your like", Toast.LENGTH_SHORT).show();
                                    } else {
                                        finalHolder.wishlidt_img.setImageResource(R.drawable.ic_favorite_border);
                                        Toast.makeText(context, " Remove like", Toast.LENGTH_SHORT).show();

                                    }


                                } else {
                                    //Toast.makeText(getActivity(), " Try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                            Log.e("Error", error.toString());

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(jsObjRequest);

                    cout = 2;
                } else {
                    likeaction = "1";

                    final String customer_id = BSession.getInstance().getUser_id(getContext());
                    final User user = new User();

                    final Map<String, String> params = new HashMap<>();

                    String par_str = "?user_id=" + customer_id;
                    String par_str1 = "&product_id=" + pid;
                    String par_str2 = "&status=" + likeaction;

                    String baseUrl = ProductConfig.wishlist + par_str + par_str1 + par_str2;
                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.
                            Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                    ProductConfig.UserDetails = new User(jsonResponse);

                                    final Productmodel model = new Productmodel();
                                    finalHolder1.wishlist_count.setText(jsonResponse.getString("count"));

                                    status = jsonResponse.getString("status");
                                    String mess = jsonResponse.getString("message");
                                    if (mess.equals("Successfully liked whislist")) {
                                        finalHolder.wishlidt_img.setImageResource(R.drawable.ic_favorite);

                                        Toast.makeText(context, " Thanks for your like", Toast.LENGTH_SHORT).show();
                                    } else {
                                        finalHolder.wishlidt_img.setImageResource(R.drawable.ic_favorite_border);
                                        Toast.makeText(context, " Remove like", Toast.LENGTH_SHORT).show();

                                    }


                                } else {
                                    //Toast.makeText(getActivity(), " Try again.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                            Log.e("Error", error.toString());

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(jsObjRequest);

                    cout = 1;
                }

            }
        });


        return row;
    }
    private void setAnimation(View viewToAnimate, int position)
    {          int lastPosition=0;

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    static class RecordHolder {


        TextView item_name, item_disprice, item_oldprice, item_saveprice, wishlist_count;
        ImageView item_image, wishlidt_img;
        RatingBar ratingBar;
        RelativeLayout wishll,item_parent;
        private RelativeLayout llParent;
    }
}





