package com.ZuWay.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class MyCartActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    MyCartListAdapter myCartListAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView total_price;
    Button pay_btn, pay_cont;
    LinearLayout prill;
    String scid, cid;
    private RecyclerView recyclerView;
    private List<Productmodel> cartModelList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomSheetDialog mBottomSheetDialog;
    boolean mFocusDuringOnPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        toolbar();
        bundle();
        init();

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = BSession.getInstance().getUser_id(MyCartActivity.this);
                System.out.println("user_id---"+user_id);
                if (user_id.equalsIgnoreCase("")){
                    Intent intent=new Intent(MyCartActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {


                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    pay_btn.startAnimation(animFadein);
                    loadplacecheckout();
                }
            }
        });


        pay_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                pay_btn.startAnimation(animFadein);
               // onBackPressed();


            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        loadcartitem();

                                    }
                                }
        );


    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scid = extras.getString("scid");
            cid = extras.getString("cid");
        }
    }

    public void init(){
        total_price  = findViewById(R.id.total_price);
        pay_btn      = findViewById(R.id.pay_btn);
        pay_cont     = findViewById(R.id.pay_con);
        prill        = findViewById(R.id.prill);
        recyclerView = findViewById(R.id.recy_cart);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadcartitem();
    }

    private void loadplacecheckout() {
        final Map<String, String> params = new HashMap<>();

        String para_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String baseUrl  = ProductConfig.placeorder + para_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);


                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                Intent intent = new Intent(MyCartActivity.this, OrderPlacedActivity.class);

                                Bundle extras = getIntent().getExtras();
                                intent.putExtra("order_id", jsonResponse.getString("order_id"));
                                intent.putExtra("total", jsonResponse.getString("total"));
                                intent.putExtra("product_count", jsonResponse.getString("product_count"));
                                intent.putExtra("grandtotal", jsonResponse.getString("grandtotal"));
                                intent.putExtra("deliveryamount", jsonResponse.getString("deliveryamount"));
                                intent.putExtra("cid", cid);
                                intent.putExtra("scid", scid);
                                startActivity(intent);

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

    private void loadcartitem() {

        swipeRefreshLayout.setRefreshing(true);
        final Map<String, String> params = new HashMap<>();

        cartModelList   = new ArrayList<>();

        String para_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String baseUrl  = ProductConfig.getcart + para_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {


                                total_price.setText(jsonResponse.getString("totalamount"));

                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    Productmodel model = new Productmodel();
                                    model.setCate_id(jsonObject.getString("cid"));
                                    model.setSubcate_id(jsonObject.getString("scid"));
                                    model.setPid(jsonObject.getString("product_id"));
                                    model.setCart_id(jsonObject.getString("cart_id"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_dis(jsonObject.getString("product_discription"));
                                    model.setProduct_disprice(jsonObject.getString("discountprize"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_saveprice(jsonObject.getString("product_saveprice"));
                                    model.setProduct_stack(jsonObject.getString("product_stockcount"));
                                    model.setProduct_delivery_Charge(jsonObject.getString("deliveryamount"));
                                    model.setProduct_color(jsonObject.getString("colour"));
                                    model.setProduct_size(jsonObject.getString("size"));
                                    model.setProduct_total_price(jsonObject.getString("total"));
                                    model.setProduct_qty(jsonObject.getString("quantity"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    cartModelList.add(model);

                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    myCartListAdapter = new MyCartListAdapter(MyCartActivity.this, cartModelList);
                                    recyclerView.setAdapter(myCartListAdapter);

                                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MyCartActivity.this, R.anim.layout_animation);recyclerView.setLayoutAnimation(controller);

                                }

                            } else {

                                FrameLayout noitem = findViewById(R.id.noitem);
                                noitem.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                pay_btn.setVisibility(View.GONE);
                                pay_cont.setVisibility(View.VISIBLE);
                                total_price.setText("0");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
                //onBackPressed();
            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);
        activitytitle.setText("My Cart");


    }

    public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.Holder> {

        Integer i;
        String customer_id, pid, cart_id,size,color;
        String editqut, type;
        String para_str2;
        private Context context;
        private List<Productmodel> cartModelList;


        public MyCartListAdapter(Context context, List<Productmodel> cartModelList) {
            this.context = context;
            this.cartModelList = cartModelList;
        }

        @NonNull
        @Override
        public MyCartListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            MyCartListAdapter.Holder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            viewHolder = getViewHolder(viewGroup, inflater);
            return viewHolder;
        }

        private MyCartListAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
            MyCartListAdapter.Holder viewHolder;
            View v1 = inflater.inflate(R.layout.mycart_item_layout, viewGroup, false);
            viewHolder = new MyCartListAdapter.Holder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyCartListAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {

            final Productmodel model = cartModelList.get(position);

            holder.item_name.setText(model.getProduct_name());
            holder.item_price.setText(model.getProduct_total_price());
            System.out.println("model"+model.getProduct_total_price());
            holder.item_origiprice.setText(" ₹ " + model.getProduct_oldprice() + ".00");
            holder.item_save.setText(" ₹ " + model.getProduct_saveprice() + ".00");

            size=model.getProduct_size();
            color=model.getProduct_color();
            if(size.equalsIgnoreCase("null")&&color.equalsIgnoreCase("null")){
                holder.llay_rating.setVisibility(View.GONE);

            }else if (size.equalsIgnoreCase("null")&&!color.equalsIgnoreCase("null")){
                holder.llay_rating.setVisibility(View.VISIBLE);
                holder.item_size.setVisibility(View.GONE);
                holder.sizee.setVisibility(View.GONE);
                holder.slash.setVisibility(View.GONE);
                holder.item_color.setText(model.getProduct_color());

            }else if(color.equalsIgnoreCase("null")&&!size.equalsIgnoreCase("null")){
                holder.llay_rating.setVisibility(View.VISIBLE);
                holder.item_color.setVisibility(View.GONE);
                holder.colorr.setVisibility(View.GONE);
                holder.slash.setVisibility(View.GONE);
                holder.item_size.setText(model.getProduct_size());
            }
            else{
                holder.llay_rating.setVisibility(View.VISIBLE);
                holder.item_size.setText(model.getProduct_size());
                holder.item_color.setText(model.getProduct_color());
            }

            holder.item_dis.setText( Html.fromHtml(String.valueOf( Html.fromHtml(model.getProduct_dis()))));

            if (model.getProduct_stack().equalsIgnoreCase("0")) {
                holder.item_stock.setText("Out of Stock");
            } else {
                holder.item_stock.setText("In Stock");
            }

            holder.tv_qty.setText(model.getProduct_qty());
            holder.qty.setText("Quantity : "+model.getProduct_qty());

            String url = model.getProduct_img();
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.list_gray)
                    .into(holder.item_image);
            holder.item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Productmodel model = cartModelList.get(position);
                    Intent intent=new Intent(MyCartActivity.this,ItemActivity.class);
                    intent.putExtra("pid",model.getPid() );
                    intent.putExtra("cid", model.getCate_id());
                    intent.putExtra("scid", model.getSubcate_id());
                    startActivity(intent);
                    ItemActivity.productmodel = model;
                    context.startActivity(intent);

                }
            });

            customer_id = BSession.getInstance().getUser_id(MyCartActivity.this);

            holder.delet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    holder.delet.startAnimation(animFadein);
                    cart_id = cartModelList.get(position).getCart_id();


                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCartActivity.this, R.style.AlertDialogTheme);
                    builder.setMessage(getApplicationContext().getResources().getString(R.string.alert_want_cancell))
                            .setCancelable(false)
                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            final Map<String, String> params = new HashMap<>();

                                            String para_str3           = "?user_id=" + customer_id;
                                            String para_str4           = "&cart_id=" + cart_id;
                                            String baseUrl             = ProductConfig.removecart + para_str3 + para_str4;
                                            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Log.e("Response", response.toString());
                                                    try {
                                                        JSONObject jsonResponse = new JSONObject(response);

                                                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                                            removeItem(position);
                                                            loadcartitem();
                                                            loadtotalamount();

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


            holder.tv_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    holder.delet.startAnimation(animFadein);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) {
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

                    final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.layout_editqut, null);

                    final EditText qyutt = bottomSheetLayout.findViewById(R.id.et_qut);
                    final TextView typee = bottomSheetLayout.findViewById(R.id.type);

                    qyutt.setFocusable(true);
                    qyutt.requestFocus();
                    qyutt.requestFocusFromTouch();

                    qyutt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());
                                pid = cartModelList.get(position).getPid();
                                editqut = qyutt.getText().toString();
                                typee.setText(editqut);
                                type = typee.getText().toString();


                                final Map<String, String> params = new HashMap<>();

                                String para_str3 = "?user_id=" + customer_id;
                                String para_str1 = "&product_id=" + pid;
                                para_str2        = "&quantity=" + type;
                                cart_id          = cartModelList.get(position).getCart_id();
                                String para_str4 = "&cart_id=" + cart_id;
                                String baseUrl   = ProductConfig.editcart + para_str3 + para_str1 + para_str2 + para_str4;

                                StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("Response", response.toString());
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);

                                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                                holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                                holder.item_price.setText(jsonResponse.getString("total"));
                                                if(editqut!=null){
                                                    hideKeyboard(MyCartActivity.this);
                                                }
                                                loadcartitem();

                                            } else {
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


                                mBottomSheetDialog.dismiss();
                            }
                            return false;
                        }
                    });

                    (bottomSheetLayout.findViewById(R.id.sub_btn)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            pid = cartModelList.get(position).getPid();
                            editqut = qyutt.getText().toString();
                            typee.setText(editqut);
                            type = typee.getText().toString();


                            final Map<String, String> params = new HashMap<>();

                            String para_str3 = "?user_id=" + customer_id;
                            String para_str1 = "&product_id=" + pid;
                            para_str2        = "&quantity=" + type;
                            cart_id          = cartModelList.get(position).getCart_id();
                            String para_str4 = "&cart_id=" + cart_id;
                            String baseUrl   = ProductConfig.editcart + para_str3 + para_str1 + para_str2 + para_str4;

                            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e("Response", response.toString());
                                    try {
                                        JSONObject jsonResponse = new JSONObject(response);

                                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                            holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                            holder.item_price.setText(jsonResponse.getString("total"));

                                            loadcartitem();
                                        } else {
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


                            mBottomSheetDialog.dismiss();
                        }
                    });
                    (bottomSheetLayout.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetDialog.dismiss();
                        }
                    });

                    mBottomSheetDialog = new BottomSheetDialog(MyCartActivity.this);
                    mBottomSheetDialog.setContentView(bottomSheetLayout);
                    mBottomSheetDialog.show();


                }
            });

            holder.tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    holder.tv_plus.startAnimation(animFadein);

                    pid = cartModelList.get(position).getPid();
                    final String _stringVal;
                    Log.d("src", "Increasing value...");
                    i = Integer.valueOf(cartModelList.get(position).getProduct_qty());
                    i = i + 1;
                    _stringVal = String.valueOf(i);

                    final Map<String, String> params = new HashMap<>();

                    String para_str3 = "?user_id=" + customer_id;
                    String para_str1 = "&product_id=" + pid;
                    para_str2        = "&quantity=" + "1";
                    cart_id          = cartModelList.get(position).getCart_id();
                    String para_str4 = "&cart_id=" + cart_id;
                    String baseUrl   = ProductConfig.incart + para_str3 + para_str1 + para_str2 + para_str4;

                    StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                    holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                    holder.item_price.setText(jsonResponse.getString("total"));

                                    loadtotalamount();

                                    loadcartitem();
                                } else {
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

            holder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    holder.tv_minus.startAnimation(animFadein);
                    pid = cartModelList.get(position).getPid();
                    String _stringVal;
                    Log.d("src", "Decreasing value...");
                    i = Integer.valueOf(cartModelList.get(position).getProduct_qty());
                    if (i > 0) {
                        i = i - 1;
                        _stringVal = String.valueOf(i);

                        final Map<String, String> params = new HashMap<>();


                        String para_str3 = "?user_id=" + customer_id;
                        String para_str1 = "&product_id=" + pid;
                        String para_str2 = "&quantity=" + "1";
                        cart_id          = cartModelList.get(position).getCart_id();
                        String para_str4 = "&cart_id=" + cart_id;
                        String baseUrl   = ProductConfig.subcart + para_str3 + para_str1 + para_str2 + para_str4;

                        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response", response.toString());
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);

                                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                                        holder.tv_qty.setText(jsonResponse.getString("quantity"));
                                        holder.item_price.setText(jsonResponse.getString("total"));
                                        loadcartitem();
                                        loadtotalamount();
                                        int qut = Integer.parseInt(jsonResponse.getString("quantity"));
                                        if (qut <= 0) {
                                            removeItem(position);
                                        }


                                    } else {
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


                    } else {
                        Log.d("src", "Value can't be less than 0");
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return cartModelList == null ? 0 : cartModelList.size();
        }


        private void loadtotalamount() {

            final Map<String, String> params = new HashMap<>();

            String para_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
            String baseUrl  = ProductConfig.getcart + para_str;
            final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                    total_price.setText(jsonResponse.getString("totalamount"));

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

        public void removeItem(int position) {
            cartModelList.remove(position);
            notifyItemRemoved(position);
        }

        public class Holder extends RecyclerView.ViewHolder {

            public TextView item_name,slash,sizee,colorr, item_dis, item_stock, item_price, item_origiprice, item_save, item_size, item_color, item_dd, item_dch, tv_plus, tv_qty, tv_add, tv_minus;
            ImageView item_image;
            CardView cardView;
            ImageView delet;
            ImageButton editqut;
            TextView qty;
            LinearLayout llay_rating;

            private Holder(View itemView) {
                super(itemView);

                item_name       = itemView.findViewById(R.id.mc_product_name);
                item_image      = itemView.findViewById(R.id.mc_product_image);
                item_price      = itemView.findViewById(R.id.mc_product_price);
                item_origiprice = itemView.findViewById(R.id.mc_product_priceorgi);
                item_size       = itemView.findViewById(R.id.mc_product_size);
                item_color      = itemView.findViewById(R.id.mc_product_color);
                item_stock      = itemView.findViewById(R.id.mc_product_stock);
                item_dis        = itemView.findViewById(R.id.mc_product_dis);
                item_save       = itemView.findViewById(R.id.mc_product_save);
                delet           = itemView.findViewById(R.id.mc_product_delet);
                cardView        = itemView.findViewById(R.id.cv_mccard);
                item_dis        = itemView.findViewById(R.id.mc_product_dis);
                item_stock      = itemView.findViewById(R.id.mc_product_stock);
                editqut         = itemView.findViewById(R.id.qutity_edit);
                tv_qty          = itemView.findViewById(R.id.tv_qty);
                tv_plus         = itemView.findViewById(R.id.tv_plus);
                tv_add          = itemView.findViewById(R.id.tv_add);
                tv_minus        = itemView.findViewById(R.id.tv_minus);
                qty             = itemView.findViewById(R.id.qty);
                llay_rating     = itemView.findViewById(R.id.llay_rating);
                sizee           =itemView.findViewById(R.id.sizee);
                colorr          =itemView.findViewById(R.id.colorr);
                slash           =itemView.findViewById(R.id.slash);
            }
        }
    }


    public static void hideKeyboard(Activity activity) {
        if (isKeyboardVisible(activity)) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static boolean isKeyboardVisible(Activity activity) {
        Rect r = new Rect();
        View contentView = activity.findViewById(android.R.id.content);
        contentView.getWindowVisibleDisplayFrame(r);
        int screenHeight = contentView.getRootView().getHeight();

        int keypadHeight = screenHeight - r.bottom;

        return
                (keypadHeight > screenHeight * 0.15);
    }
   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(scid.equalsIgnoreCase("0")) {
            Intent intent = new Intent(MyCartActivity.this, HomeActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(MyCartActivity.this, ItemListActivity.class);
            intent.putExtra("scid",scid);
            intent.putExtra("cid",cid);
            intent.putExtra("keyword", "");
            startActivity(intent);
        }
    }*/
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        //mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

       /* if(mFocusDuringOnPause) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                scid = extras.getString("scid");
                cid = extras.getString("cid");
            }
            Intent intent=new Intent(MyCartActivity.this,MyCartActivity.class);
            intent.putExtra("cid",cid);
            intent.putExtra("scid",scid);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MyCartActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }

    @Override
    protected void onResume() {
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MyCartActivity.this, R.anim.layout_animation);recyclerView.setLayoutAnimation(controller);
        super.onResume();
    }
}
