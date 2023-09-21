package com.ZuWay.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ZuWay.R;
import com.ZuWay.atapter.OrderDeatilAdapter;
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

public class MyOrderDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    TextView order_cancel_tx,order_status_tx,symbol_item_dc,order_process,review2, item_payment_status,orederid, delivery_status, user_name, user_add, user_mn, user_email, item_price, item_dc, item_totprice, item_payment, reatll;
    String review_product, id, rating1, review1, rat, rev, rtv, status = "1", commt,orderdate,ordertime;
    CardView cv_track;
    TextView tracker;
    CheckBox trk1, trk2, trk3, trk4;
    EditText Review;
    RatingBar ratingBar, ratingBar2;
    AlertDialog dialog;
    RecyclerView oreder_itemry;
    OrderDeatilAdapter orderDeatilAdapter;
    LinearLayout ratevaluell, referbefore;
    Button can_btn,returntx;
    private List<Productmodel> orderModelList;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean mFocusDuringOnPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_order_details);
        bundle();
        init();


        returntx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrderDetailsActivity.this,ShippingandRetrun.class));
            }
        });

        can_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderDetailsActivity.this, R.style.AlertDialogTheme);
                builder.setMessage(getApplicationContext().getResources().getString(R.string.alert_want_cancel))
                        .setCancelable(false)
                        .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        loadcancelbtn();

                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getApplicationContext().getResources().getString(R.string.alert_no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();
                                    }
                                });
                final AlertDialog alert = builder.create();
                alert.show();

            }
        });



        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tracker.startAnimation(animFadein);

                tracker.setVisibility(View.GONE);
                loadtracker();
            }
        });

        String time="Order on " + orderdate + " / " +ordertime;
        delivery_status.setText(time);

        reatll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                reatll.startAnimation(animFadein);

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.review_layout, null);
                Review = alertLayout.findViewById(R.id.review);
                ratingBar = alertLayout.findViewById(R.id.ratingBar);
                final Button sumit = alertLayout.findViewById(R.id.submitRateBtn);

                AlertDialog.Builder alert = new AlertDialog.Builder(MyOrderDetailsActivity.this);
                alert.setView(alertLayout);
               // alert.setCancelable(false);
               // MyOrderDetailsActivity.this.setFinishOnTouchOutside(true);
                dialog = alert.create();


                sumit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        sumit.startAnimation(animFadein);

                        if (ratingBar.getRating() == 0) {
                            Toast.makeText(getApplicationContext(), "Please Rating", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            rating1 = String.valueOf(ratingBar.getRating());
                            review1 = Review.getText().toString();
                            loadratting();

                        }


                    }
                });

                dialog.show();

            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        loadtracker();

                                    }
                                }
        );

        toolbar();
        loadorderview();
    }


    public void bundle(){
        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            id        = extras.getString("orderid");
            orderdate = extras.getString("order_date");
            ordertime = extras.getString("order_time");

        }
    }

    public void init(){

        ratevaluell         = findViewById(R.id.ratevaluell);
        referbefore         = findViewById(R.id.referbefore);
        review2             = findViewById(R.id.review);
        ratingBar2          = findViewById(R.id.ratingBar2);
        reatll              = findViewById(R.id.ratell);
        oreder_itemry       = findViewById(R.id.item_orderry);
        can_btn             = findViewById(R.id.can_btn);
        returntx            = findViewById(R.id.return_tx);
        order_cancel_tx     = findViewById(R.id.order_cancel_tx);
        order_status_tx     = findViewById(R.id.order_status_tx);
        order_process       = findViewById( R.id.order_process );
        symbol_item_dc      = findViewById( R.id.symbol_item_dc );
        orederid            = findViewById(R.id.orderid);
        delivery_status     = findViewById(R.id.delivery_status);
        user_name           = findViewById(R.id.user_name);
        user_add            = findViewById(R.id.user_add);
        user_mn             = findViewById(R.id.user_mn);
        user_email          = findViewById(R.id.user_em);
        item_price          = findViewById(R.id.item_price);
        item_dc             = findViewById(R.id.item_dc);
        item_totprice       = findViewById(R.id.item_tolprice);
        item_payment        = findViewById(R.id.item_payment);
        item_payment_status = findViewById(R.id.item_payment_status);
        tracker             = findViewById(R.id.tracker);
        cv_track            = findViewById(R.id.cv_track);
        trk1                = findViewById(R.id.trk1);
        trk2                = findViewById(R.id.trk2);
        trk3                = findViewById(R.id.trk3);
        trk4                = findViewById(R.id.trk4);

    }

    @Override
    public void onRefresh() {
        loadtracker();

    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    private void loadtracker() {


        final Map<String, String> params = new HashMap<>();

        String para_str = "?order_id=" + id;

        String BaseUrl  = ProductConfig.orderdelivery + para_str;

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, BaseUrl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                        Productmodel productModel = new Productmodel();

                        status = jsonResponse.getString("status").toString();
                        commt  = jsonResponse.getString("comment").toString();

                        if (status.equalsIgnoreCase("1")) {

                            trk1.setVisibility(View.VISIBLE);
                            trk1.setChecked(true);
                            trk2.setVisibility(View.VISIBLE);
                            trk3.setVisibility(View.VISIBLE);
                            trk4.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Order has been placed ", Toast.LENGTH_SHORT).show();

                        }

                        else if (status.equalsIgnoreCase("2")) {
                            trk1.setVisibility(View.VISIBLE);
                            trk1.setChecked(true);
                            trk2.setVisibility(View.VISIBLE);
                            trk3.setVisibility(View.VISIBLE);
                            trk4.setText(commt);
                            trk4.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Arriving date", Toast.LENGTH_SHORT).show();

                        }
                        else if (status.equalsIgnoreCase("3")) {

                            trk1.setVisibility(View.VISIBLE);
                            trk1.setChecked(true);
                            trk2.setVisibility(View.VISIBLE);
                            trk2.setChecked(true);
                            trk3.setVisibility(View.VISIBLE);
                            trk4.setText(commt);
                            trk4.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Package has shipped", Toast.LENGTH_SHORT).show();

                        }
                       else if (status.equalsIgnoreCase("4")) {

                            trk1.setVisibility(View.VISIBLE);
                            trk1.setChecked(true);
                            trk2.setVisibility(View.VISIBLE);
                            trk2.setChecked(true);
                            trk3.setVisibility(View.VISIBLE);
                            trk3.setChecked(true);
                            trk4.setText(commt);
                            trk4.setVisibility(View.VISIBLE);
                            trk4.setChecked( true);
                            can_btn.setVisibility( View.GONE );
                            order_process.setVisibility( View.GONE );
                          //  returntx.setVisibility( View.VISIBLE );
                            order_process.setVisibility( View.  VISIBLE );
                            order_process.setText("Order Delivered");
                            item_payment_status.setText("Paid");
                            Toast.makeText(getApplicationContext(), "Delivered Process complete", Toast.LENGTH_SHORT).show();

                        } else if (status.equalsIgnoreCase("5")) {

                            trk1.setVisibility(View.VISIBLE);
                            trk1.setChecked(true);
                            trk2.setVisibility(View.VISIBLE);
                            trk2.setChecked(true);
                            trk3.setVisibility(View.VISIBLE);
                            trk3.setChecked(true);
                            trk4.setText(commt);
                            trk4.setVisibility(View.VISIBLE);
                            trk4.setChecked(true);

                    }
                        else if (status.equalsIgnoreCase("6")) {

                            cv_track.setVisibility(View.GONE);
                            order_cancel_tx.setVisibility(View.VISIBLE);
                            order_status_tx.setText("Order Cancelled");
                            order_status_tx.setTextColor(Color.RED);
                            order_status_tx.setVisibility(View.VISIBLE);
                            can_btn.setVisibility(View.GONE);
                            order_process.setVisibility(View.GONE);
                            ratevaluell.setVisibility(View.GONE);
                            reatll.setVisibility(View.GONE);
                        }else {
                    }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
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

    private void loadratting() {

        final Map<String, String> params = new HashMap<>();

        if (rating1.equalsIgnoreCase(String.valueOf(1.0))) {
            rtv = "1";
        } else if (rating1.equalsIgnoreCase(String.valueOf(2.0))) {
            rtv = "2";
        } else if (rating1.equalsIgnoreCase(String.valueOf(3.0))) {
            rtv = "3";
        } else if (rating1.equalsIgnoreCase(String.valueOf(4.0))) {
            rtv = "4";
        } else if (rating1.equalsIgnoreCase(String.valueOf(5.0))) {
            rtv = "5";
        } else {
            rtv = "0";
        }

        String par_str  = "?order_id=" + id;
        String par_str1 = "&review_status=" + rtv;
        String par_str2 = "&review_comment=" + review1;

        String baseUrl  = ProductConfig.revieworder + par_str + par_str1 + par_str2;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {


                        review_product = Review.getText().toString().trim();
                        review2.setText(review_product);
                        ratingBar2.setRating(Float.parseFloat(rating1));
                        dialog.cancel();
                        dialog.dismiss();

                        loadorderview();
                        Toast.makeText(MyOrderDetailsActivity.this, "Thank You for Your Rating", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(MyOrderDetailsActivity.this, "Failed..!", Toast.LENGTH_SHORT).show();


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
        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderDetailsActivity.this);
        requestQueue.add(jsObjRequest);


    }

    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);

        activitytitle.setText("My Orders");
    }


    private void loadorderview() {
        final Map<String, String> params = new HashMap<>();
        String par_str = "?order_id=" + id;
        orderModelList = new ArrayList<>();

        String baseUrl = ProductConfig.myorderlist + par_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                                orederid.setText(jsonResponse.getString("order_id"));
                                user_name.setText(jsonResponse.getString("username"));
                                user_add.setText(jsonResponse.getString("address"));
                                user_email.setText(jsonResponse.getString("email"));
                                user_mn.setText(jsonResponse.getString("phone"));
                                item_payment.setText(jsonResponse.getString("payment_type"));
                                item_price.setText(jsonResponse.getString("total"));
                                item_payment_status.setText(jsonResponse.getString("payment_mode"));
                                item_totprice.setText(jsonResponse.getString("grandtotal"));

                                int tot = Integer.parseInt(item_totprice.getText().toString());

                                if(jsonResponse.getString("status").equalsIgnoreCase("6")){
                                    cv_track.setVisibility(View.GONE);
                                    order_cancel_tx.setVisibility(View.VISIBLE);
                                    order_status_tx.setText("Order Cancelled");
                                    order_status_tx.setTextColor(Color.RED);
                                    order_status_tx.setVisibility(View.VISIBLE);
                                    can_btn.setVisibility(View.GONE);
                                    order_process.setVisibility(View.GONE);
                                    status=(jsonResponse.getString("status"));
                                    Toast.makeText(getApplicationContext(), "Order Cancelled", Toast.LENGTH_SHORT).show();

                                }else if (jsonResponse.getString("status").equalsIgnoreCase("4")) {

                                    trk1.setVisibility(View.VISIBLE);
                                    trk1.setChecked(true);
                                    trk2.setVisibility(View.VISIBLE);
                                    trk2.setChecked(true);
                                    trk3.setVisibility(View.VISIBLE);
                                    trk4.setText(commt);
                                    trk4.setVisibility(View.VISIBLE);
                                    can_btn.setVisibility( View.GONE );
                                    order_process.setVisibility( View.GONE );
                                  //  returntx.setVisibility( View.VISIBLE );
                                    order_process.setVisibility( View.  VISIBLE );
                                    order_process.setText("Order Delivered");
                                    item_payment_status.setText("Paid");

                                } else {
                                    order_cancel_tx.setVisibility(View.GONE);
                                    can_btn.setVisibility(View.VISIBLE);
                                    cv_track.setVisibility(View.VISIBLE);
                                    trk1.setVisibility(View.VISIBLE);
                                    trk2.setVisibility(View.VISIBLE);
                                    trk3.setVisibility(View.VISIBLE);
                                    trk4.setVisibility(View.GONE);
                                    order_status_tx.setVisibility(View.GONE);

                                    loadtracker();
                                }

                                if (tot >= 500) {
                                    item_dc.setText("Free");
                                    symbol_item_dc.setVisibility( View.GONE );
                                } else {
                                    item_dc.setText(jsonResponse.getString("deliveryamount"));
                                }

                                rat = jsonResponse.getString("ratting_value");
                                rev = jsonResponse.getString("review_comment");


                                if (rat.equalsIgnoreCase("0")) {
                                    referbefore.setVisibility(View.VISIBLE);
                                    ratevaluell.setVisibility(View.GONE);

                                } else {
                                    referbefore.setVisibility(View.GONE);
                                    ratevaluell.setVisibility(View.VISIBLE);
                                    if (rev.equalsIgnoreCase("0")) {
                                        review2.setText(" ");
                                    } else {
                                        review2.setText(rev);
                                    }
                                    ratingBar2.setRating(Float.parseFloat(rat));
                                }


                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    Productmodel model = new Productmodel();
                                    model.setPid(jsonObject.getString("product_id"));
                                    model.setCate_id(jsonObject.getString("cid"));
                                    model.setSubcate_id(jsonObject.getString("scid"));
                                    model.setProduct_size(jsonObject.getString("size"));
                                    model.setProduct_color(jsonObject.getString("colour"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_qty(jsonObject.getString("quantity"));
                                    model.setProduct_disprice(jsonObject.getString("total"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_saveprice(jsonObject.getString("saveprize"));
                                    model.setProduct_code(jsonObject.getString("product_code"));
                                    model.setProduct_brand(jsonObject.getString("product_brand"));
                                    model.setProduct_sold(jsonObject.getString("product_soldby"));
                                    model.setProduct_return(jsonObject.getString("product_returnpolicy"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    model.setProduct_category(jsonObject.getString("product_category"));
                                    model.setStatus(jsonObject.getString("status"));
                                    model.setOrder_id(jsonObject.getString("order_id"));
                                    model.setArraving_date(jsonObject.getString("arraving_date"));
                                    orderModelList.add(model);

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    oreder_itemry.setLayoutManager(linearLayoutManager);
                                    orderDeatilAdapter = new OrderDeatilAdapter(MyOrderDetailsActivity.this, orderModelList);
                                    oreder_itemry.setAdapter(orderDeatilAdapter);

                                }

                            } else {

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


    private void loadcancelbtn() {

        final Map<String, String> params = new HashMap<>();

        String para_str1 = "&customer_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String para_str  = "?order_id=" + id;


        String baseUrl   = ProductConfig.cancelorder + para_str + para_str1;

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);


                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                can_btn.setVisibility(View.GONE);
                                order_process.setVisibility(View.GONE);
                                order_cancel_tx.setVisibility(View.VISIBLE);
                                reatll.setVisibility(View.GONE);

                            } else {
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
  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MyOrderActivity.class);
        startActivity(intent);
    }*/

    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

       // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();
/*
        if(mFocusDuringOnPause) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {

                id        = extras.getString("orderid");
                orderdate = extras.getString("order_date");
                ordertime = extras.getString("order_time");

            }
            Intent intent=new Intent(MyOrderDetailsActivity.this,MyOrderDetailsActivity.class);
            intent.putExtra("orderid",id);
            intent.putExtra("order_date",orderdate);
            intent.putExtra("order_time",ordertime);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MyOrderDetailsActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }

}
