package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ZuWay.R;
import com.ZuWay.atapter.MyOrderListAdapter;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;
import com.ZuWay.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends AppCompatActivity {

    MyOrderListAdapter myOrderListAdapter;
    LinearLayoutManager linearLayoutManager;
    String activity;
    private RecyclerView recyclerView;
    private List<Productmodel> categoryModelList;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            activity = extras.getString("activity");
        }


        recyclerView = findViewById(R.id.rvOrderList);

        loadorder();
        toolbar();
    }


    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);

            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);

        activitytitle.setText("My Orders");


    }


    private void loadorder() {


        final Map<String, String> params = new HashMap<>();

        categoryModelList = new ArrayList<>();

        String para_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());


        String baseUrl = ProductConfig.myorder + para_str;
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


                                    model.setOrder_id(jsonObject.getString("order_id"));
                                    model.setPayment_mode(jsonObject.getString("payment_mode"));
                                    model.setProduct_total_price(jsonObject.getString("grandtotal"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    model.setOrder_date(jsonObject.getString("order_date"));
                                    model.setOrder_time(jsonObject.getString("order_time"));
                                    categoryModelList.add(model);

                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    myOrderListAdapter = new MyOrderListAdapter(MyOrderActivity.this, categoryModelList);
                                    recyclerView.setAdapter(myOrderListAdapter);
                                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MyOrderActivity.this, R.anim.layout_animation);recyclerView.setLayoutAnimation(controller);


                                    recyclerView.addOnItemTouchListener(
                                            new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.ClickListener() {

                                                @Override
                                                public void onItemClick(View view, final int position) {

                                                    Productmodel model = categoryModelList.get(position);
                                                    String id = model.getOrder_id();
                                                    String date = model.getOrder_date();
                                                    String time = model.getOrder_time();
                                                    Intent intent = new Intent(MyOrderActivity.this, MyOrderDetailsActivity.class);
                                                    intent.putExtra("orderid", id);
                                                    intent.putExtra("order_date", date);
                                                    intent.putExtra("order_time", time);
                                                    startActivity(intent);

                                                }

                                                @Override
                                                public void onLongItemClick(View view, int position) {

                                                }
                                            })
                                    );

                                }


                            } else {

                                FrameLayout noitem = findViewById(R.id.noitem);
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

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MyOrderActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();

    }

    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MyOrderActivity.this, R.anim.layout_animation);recyclerView.setLayoutAnimation(controller);
        super.onResume();
    }
}
