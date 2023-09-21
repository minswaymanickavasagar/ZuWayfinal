package com.ZuWay.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ZuWay.R;

import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyWishActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Productmodel> wishModelList;
    MyWishListAdapter myWishListAdapter;
    LinearLayoutManager linearLayoutManager;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish);

        toolbar();

        recyclerView = findViewById(R.id.rvWishList);

        loadorder();

    }


    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // onBackPressed();
                onBackPressed();
            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);

        activitytitle.setText("My WishList");


    }

    @Override
    public void onRestart()
    {
        super.onRestart();
       // startActivity(getIntent());
    }


    private void loadorder() {


        final Map<String, String> params = new HashMap<>();

        wishModelList = new ArrayList<>();

        String para_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());


        String baseUrl = ProductConfig.wishlistuser + para_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    Productmodel model = new Productmodel();
                                    model.setWish_id(jsonObject.getString("wish_id"));
                                    model.setCate_id(jsonObject.getString("cid"));
                                    model.setSubcate_id(jsonObject.getString("scid"));
                                    model.setPid(jsonObject.getString("pid"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_disprice(jsonObject.getString("discountprize"));
                                    model.setProduct_saveprice(jsonObject.getString("saveprize"));
                                    model.setProduct_category(jsonObject.getString("product_category"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    model.setProduct_ratting(Float.parseFloat(jsonObject.getString("rating")));
                                    wishModelList.add(model);

                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    myWishListAdapter = new MyWishListAdapter(MyWishActivity.this, wishModelList);
                                    recyclerView.setAdapter(myWishListAdapter);

                                }


                            } else {
                                FrameLayout noitem=findViewById(R.id.noitem);
                                noitem.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);

    }

    public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.Holder> {

        String wish_id, pid, cid, scid;
        private Context context;
        private List<Productmodel> wishModelList;


        public MyWishListAdapter(Context context, List<Productmodel> wishModelList) {
            this.context = context;
            this.wishModelList = wishModelList;
        }

        @NonNull
        @Override
        public MyWishListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            MyWishListAdapter.Holder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            viewHolder = getViewHolder(viewGroup, inflater);
            return viewHolder;
        }

        private MyWishListAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
            MyWishListAdapter.Holder viewHolder;
            View v1 = inflater.inflate(R.layout.mywish_layout, viewGroup, false);
            viewHolder = new MyWishListAdapter.Holder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyWishListAdapter.Holder holder, final int position) {

            final Productmodel model = wishModelList.get(position);

            holder.item_name.setText(model.getProduct_name());

            holder.item_price.setText("₹ " + model.getProduct_disprice() + ".00");
            holder.item_origiprice.setText("₹ " + model.getProduct_oldprice() + ".00");
            holder.item_save.setText("save ₹ " + model.getProduct_saveprice() + ".00");
            holder.ratingBar.setRating(model.getProduct_ratting());


            String url = model.getProduct_img();
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.list_gray)
                    .into(holder.item_image);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    holder.cardView.startAnimation(animFadein);
                    Productmodel model = wishModelList.get(position);
                    ItemActivity.productmodel = model;

                    pid = wishModelList.get(position).getPid();

                    scid = wishModelList.get(position).getSubcate_id();
                    cid = wishModelList.get(position).getCate_id();


                    Intent i = new Intent(context, ItemActivity.class);
                    i.putExtra("scid", scid);
                    i.putExtra("cid", cid);
                    i.putExtra("pid", pid);
                   // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                }
            });


            holder.delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animFadein = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    holder.delet.startAnimation(animFadein);

                    wish_id = wishModelList.get(position).getWish_id();

                    final Map<String, String> params = new HashMap<>();

                    String para_str3 = "&user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
                    String para_str4 = "?wish_id=" + wish_id;

                    String baseUrl = ProductConfig.wishlistremove + para_str4 + para_str3;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                    loadorder();

                                } else {
                                    Toast.makeText(context, "Failed to remove", Toast.LENGTH_SHORT).show();
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
                }
            });


        }

        @Override
        public int getItemCount() {
            return wishModelList == null ? 0 : wishModelList.size();
        }


        public  class Holder extends RecyclerView.ViewHolder {

            public TextView item_name, item_price, item_origiprice, item_save;
            ImageView item_image;
            RatingBar ratingBar;
            CardView cardView;
            ImageView delet;

            private Holder(View itemView) {
                super(itemView);

                item_name       = itemView.findViewById(R.id.mc_product_name);
                item_image      = itemView.findViewById(R.id.mw_product_image);
                item_price      = itemView.findViewById(R.id.mw_product_price);
                item_origiprice = itemView.findViewById(R.id.mw_product_priceorgi);
                ratingBar       = itemView.findViewById(R.id.ratingBar);
                item_save       = itemView.findViewById(R.id.mw_product_save);
                delet           = itemView.findViewById(R.id.mc_product_delet);
                cardView        = itemView.findViewById(R.id.cv_mwcard);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MyWishActivity.this,HomeActivity.class);
        startActivity(intent);
    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        //mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {

            Intent intent=new Intent(MyWishActivity.this,MyWishActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MyWishActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }

}
