package com.ZuWay.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ZuWay.utils.NotificationONOff;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;
import com.ZuWay.EndActivity;
import com.ZuWay.R;
import com.ZuWay.atapter.ColorProductAdapter;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.atapter.SizeProductAdapter;
import com.ZuWay.model.Productmodel;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;
import com.ZuWay.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleatedItemActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static Productmodel productmodel = new Productmodel();
    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    ImageView produ_img, fevorite, share;
    String offer, quntity,editqut, type,customer_id, pid, cid, likeaction = "",sizee="-", colorr="-", scid, wishstatus;
    TextView offertx,badge_notification, tv_plus, tv_qty, tv_add, tv_minus,item_name, item_dis, item_stock, item_oldprice, item_saveprice, item_disprice, item_sold, item_brand,
            item_category, item_returnpolicy, item_code,
            item_wishlistcount, item_deliverytime, home1_view;
    Button addcart, buynow, pre, next;
    SimilarProductAdapter similarProductAdapter;
    SizeProductAdapter sizeProductAdapter;
    ColorProductAdapter colorProductAdapter;
    RatingBar ratingBar;
    CarouselView customCarouselView;
    List<Productmodel> apiSliderList = new ArrayList<>();
    Productmodel sliderModel = new Productmodel();
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    RelativeLayout wishll;
    int cout = 1;
    AutoCompleteTextView auto_search;
    View actionView;
    Toolbar toolbar;
    Integer i;
    FrameLayout offer_ll;
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView itemrecy,color,size;
    private List<Productmodel> itemmodellist;
    private List<Productmodel> sizemodellist;
    private List<Productmodel> colormodellist;
    private FruitAdapter fruitAdapter;
    private BottomSheetDialog mBottomSheetDialog;
    TextView notifi;
    ToggleButton toggleButton;
    LinearLayout lian;
    ImageView image_toggle;
    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_customm, null);


            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);


            Productmodel model = apiSliderList.get(position);
            String url = model.getProduct_listimg();
            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.list_gray)
                    .into(fruitImageView);

            return customView;
        }
    };
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Add Cart.....");
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        lian =findViewById(R.id.lian);
        progressDialog.dismiss();

        bundle();
        init();


                final String user_id = BSession.getInstance().getUser_id(ReleatedItemActivity.this);
          System.out.println("user_id,,"+user_id);

               /* if(user_id.equalsIgnoreCase("")){
                    Intent intent=new Intent(ReleatedItemActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                }*/



        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id= BSession.getInstance().getUser_id(ReleatedItemActivity.this);
                System.out.println("user_id,,"+user_id);
                if(user_id.equalsIgnoreCase("")){
                    Toast.makeText(ReleatedItemActivity.this, "if,,,", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ReleatedItemActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ReleatedItemActivity.this, "else,,,", Toast.LENGTH_SHORT).show();

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                addcart.startAnimation(animFadein);


                if (colorr != null && colorr != " " && sizee != null && sizee != " ") {
                    loadaddcart();
                } else {
                    Toast.makeText(ReleatedItemActivity.this, "Please Select your Product Color & Size", Toast.LENGTH_SHORT).show();
                }
                }

            }
        });

        progressDialog.dismiss();
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id.equalsIgnoreCase("")){
                    Intent intent=new Intent(ReleatedItemActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    buynow.startAnimation(animFadein);
                    if (colorr != null && colorr != " " && sizee != null && sizee != " ") {
                        loadbuy();
                    } else {
                        Toast.makeText(ReleatedItemActivity.this, "Please Select your Product Color & Size", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.tool_tit);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        notifi = hView.findViewById(R.id.notifi);
        toggleButton=hView.findViewById(R.id.toggleButton);
        image_toggle=hView.findViewById(R.id.image_toggle);
        customer_id = BSession.getInstance().getUser_id(getApplicationContext());

        String notification = NotificationONOff.getInstance().getType(getApplicationContext());
        if(notification.equalsIgnoreCase("1")){
            notifi.setVisibility(View.VISIBLE);
            toggleButton.setVisibility(View.GONE);
            image_toggle.setVisibility(View.VISIBLE);
            image_toggle.setImageResource(R.drawable.ic_baseline_toggle_on_24);

        }else if(notification.equalsIgnoreCase("0")){
            notifi.setVisibility(View.VISIBLE);
            toggleButton.setVisibility(View.GONE);
            image_toggle.setVisibility(View.VISIBLE);
            image_toggle.setImageResource(R.drawable.ic_baseline_toggle_off_24);

        }else{
            notifi.setVisibility(View.VISIBLE);
            toggleButton.setVisibility(View.GONE);
            image_toggle.setVisibility(View.VISIBLE);
            image_toggle.setImageResource(R.drawable.ic_baseline_toggle_off_24);

        }

        tv_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_qty.startAnimation(animFadein);
                final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());


                final View bottomSheetLayout = getLayoutInflater().inflate(R.layout.layout_editqut, null);

                final EditText qyutt = bottomSheetLayout.findViewById(R.id.et_qut);
                final TextView typee = bottomSheetLayout.findViewById(R.id.type);

                (bottomSheetLayout.findViewById(R.id.sub_btn)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());
                        pid = productmodel.getPid();
                        editqut = qyutt.getText().toString();

                        typee.setText(editqut);

                        type = typee.getText().toString();
                        tv_qty.setText(editqut);

                        mBottomSheetDialog.dismiss();
                    }
                });
                (bottomSheetLayout.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                mBottomSheetDialog = new BottomSheetDialog(ReleatedItemActivity.this);
                mBottomSheetDialog.setContentView(bottomSheetLayout);
                mBottomSheetDialog.show();


            }
        });


        tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_plus.startAnimation(animFadein);

                pid = productmodel.getPid();
                final String _stringVal;
                Log.d("src", "Increasing value...");
                i = Integer.valueOf(tv_qty.getText().toString());
                i = i + 1;
                _stringVal = String.valueOf(i);
                tv_qty.setText(_stringVal);


            }
        });

        tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_minus.startAnimation(animFadein);
                pid = productmodel.getPid();
                String _stringVal;
                Log.d("src", "Decreasing value...");
                i = Integer.valueOf(tv_qty.getText().toString());
                if (i > 0) {
                    i = i - 1;
                    _stringVal = String.valueOf(i);
                    tv_qty.setText(_stringVal);
                }

            }

        });

        loadproductview();
        loadslider();
        toolbar();
        loadgetcartcount();
        loadserach();
        itemmodellist = new ArrayList<>();
        loadreleated();
        sizemodellist = new ArrayList<>();
        loadsize();
        colormodellist = new ArrayList<>();
        loadcolor();

    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scid = extras.getString("scid");
            cid = extras.getString("cid");
            pid = extras.getString("pid");
        }
    }

    public void init(){
        share              = findViewById(R.id.share);
        offer_ll           = findViewById(R.id.offer_ll);
        offertx            = findViewById(R.id.offertx);
        pre                = findViewById(R.id.privious);
        next               = findViewById(R.id.next);
        tv_qty             = findViewById(R.id.tv_qty);
        tv_plus            = findViewById(R.id.tv_plus);
        tv_add             = findViewById(R.id.tv_add);
        tv_minus           = findViewById(R.id.tv_minus);
        customCarouselView = findViewById(R.id.customCarouselView);
        addcart            = findViewById(R.id.addbag);
        buynow             = findViewById(R.id.buynow);
        produ_img          = findViewById(R.id.product_img);
        home1_view         = findViewById(R.id.home1_view);
        item_name          = findViewById(R.id.item_name);
        item_dis           = findViewById(R.id.item_dis);
        item_stock         = findViewById(R.id.item_stock);
        item_oldprice      = findViewById(R.id.item_oldprice);
        item_saveprice     = findViewById(R.id.item_saveprice);
        item_disprice      = findViewById(R.id.item_disprice);
        item_sold          = findViewById(R.id.item_sold);
        item_brand         = findViewById(R.id.item_brand);
        item_category      = findViewById(R.id.item_category);
        item_returnpolicy  = findViewById(R.id.item_returnpolicy);
        item_wishlistcount = findViewById(R.id.fevorite_count);
        item_deliverytime  = findViewById(R.id.delivery);
        ratingBar          = findViewById(R.id.ratingBar);
        item_code          = findViewById(R.id.item_code);
        wishll             = findViewById(R.id.wishll);
        fevorite           = findViewById(R.id.fevorite);
        itemrecy           = findViewById(R.id.similarList);
        size               = findViewById(R.id.size);
        color              = findViewById(R.id.color);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ReleatedItemActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(ReleatedItemActivity.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(ReleatedItemActivity.this, MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(ReleatedItemActivity.this, MyWishActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_shipping) {
            Intent intent = new Intent(ReleatedItemActivity.this, ShippingandRetrun.class);
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(ReleatedItemActivity.this, PrivacyPolicy.class);
            startActivity(intent);

        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(ReleatedItemActivity.this, TermsandContidion.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(ReleatedItemActivity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(ReleatedItemActivity.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            logoutAlert();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(getApplicationContext().getResources().getString(R.string.alert_want_logout))
                .setCancelable(false)
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                logout();

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

    private void logout() {

        BSession.getInstance().destroy(ReleatedItemActivity.this);
        Toast.makeText(ReleatedItemActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ReleatedItemActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
       // finish();
    }


    private void loadlike() {

        final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());

        final Map<String, String> params = new HashMap<>();

        String par_str  = "?user_id=" + customer_id;
        String par_str1 = "&product_id=" + productmodel.getPid();
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


                        if (likeaction.equals("2")) {

                            item_wishlistcount.setText(jsonResponse.getString("count"));

                            Toast.makeText(getApplicationContext(), " Thanks for your like", Toast.LENGTH_SHORT).show();
                        } else {
                            item_wishlistcount.setText(jsonResponse.getString("count"));
                            Toast.makeText(getApplicationContext(), " Remove like", Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);

    }

    private void loadreleated() {

        final Map<String, String> params = new HashMap<>();

        colormodellist = new ArrayList<>();

        String par_str  = "?scid=" + productmodel.getSubcate_id();
        String par_str1 = "&pid=" + pid;

        String baseUrl = ProductConfig.smilarproduct + par_str + par_str1;
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
                                    final Productmodel model = new Productmodel();
                                    model.setCate_id(jsonObject.getString("cid"));
                                    model.setSubcate_id(jsonObject.getString("scid"));
                                    model.setPid(jsonObject.getString("pid"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_disprice(jsonObject.getString("discountprize"));
                                    model.setProduct_saveprice(jsonObject.getString("saveprize"));
                                    model.setProduct_ratting(Float.parseFloat(jsonObject.getString("rating")));
                                    model.setProduct_withlistcount(jsonObject.getString("wishlist_count"));
                                    model.setProduct_img(jsonObject.getString("web_image"));

                                    itemmodellist.add(model);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    itemrecy.setLayoutManager(linearLayoutManager);
                                    similarProductAdapter = new SimilarProductAdapter(ReleatedItemActivity.this, itemmodellist);
                                    itemrecy.setAdapter(similarProductAdapter);

                                    pre.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            pre.startAnimation(animFadein);
                                            scid = model.getSubcate_id();
                                            pid = model.getPid();

                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("keyword", "");
                                            intent.putExtra("cid", cid);
                                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });

                                    next.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            next.startAnimation(animFadein);
                                            scid = model.getSubcate_id();
                                            pid = model.getPid();
                                            Intent intent = new Intent(getApplicationContext(), ReleatedItemActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });

                                }

                            } else {
                                // Toast.makeText(getApplicationContext(), "no list items", Toast.LENGTH_SHORT).show();
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

    private void loadproductview() {
        final Map<String, String> params = new HashMap<>();
        String userid   = BSession.getInstance().getUser_id(getApplicationContext());
        String par_str  = "?scid=" + productmodel.getSubcate_id();
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;
        String baseUrl  = ProductConfig.productview + par_str + par_str1 + par_str2;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                Productmodel model = new Productmodel();

                                model.setCate_id(jsonResponse.getString("cid"));
                                model.setSubcate_id(jsonResponse.getString("scid"));
                                model.setPid(jsonResponse.getString("pid"));
                                item_name.setText(jsonResponse.getString("web_title"));
                                item_dis.setText(jsonResponse.getString("short_content"));
                                item_disprice.setText(" ₹ " + jsonResponse.getString("discountprize") + ".00");
                                item_oldprice.setText(" ₹ " + jsonResponse.getString("oldprice") + ".00");
                                item_saveprice.setText(" Save ₹ " + jsonResponse.getString("saveprize") + ".00");
                                ratingBar.setRating(Float.parseFloat(jsonResponse.getString("rating")));
                                item_code.setText(jsonResponse.getString("product_code"));
                                item_brand.setText(jsonResponse.getString("product_brand"));
                                item_sold.setText(jsonResponse.getString("product_soldby"));
                                item_returnpolicy.setText(jsonResponse.getString("product_returnpolicy"));
                                String chrg = jsonResponse.getString("deliveryamount");
                                offer = jsonResponse.getString("offer");
                                quntity = jsonResponse.getString("oldqunatity");
                                item_deliverytime.setText("₹ " + chrg);
                                item_wishlistcount.setText(jsonResponse.getString("wishlist_count"));
                                wishstatus = jsonResponse.getString("wishlist_count");

                                item_category.setText(jsonResponse.getString("product_category"));

                                if (jsonResponse.getString("totalcount").equalsIgnoreCase("0")) {
                                    item_stock.setText("Out of Stock");
                                } else {
                                    item_stock.setText("In Stock");
                                }

                                if (!offer.equals("0")) {
                                    offer_ll.setVisibility(View.VISIBLE);
                                    offertx.setText(offer + "%");
                                }


                                if (wishstatus.equalsIgnoreCase("2")) {
                                    fevorite.setImageResource(R.drawable.ic_favorite);
                                    item_wishlistcount.setText(jsonResponse.getString("wishlist_count"));

                                } else {
                                    fevorite.setImageResource(R.drawable.ic_favorite_border);
                                    item_wishlistcount.setText(jsonResponse.getString("wishlist_count"));
                                }
                                if (wishstatus.equalsIgnoreCase("1")) {
                                    wishll.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Animation animFadein = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fade_in );
                                            wishll.startAnimation( animFadein );

                                            if (cout == 1) {
                                                likeaction = "2";
                                                fevorite.setImageResource( R.drawable.ic_favorite );
                                                loadlike();
                                                cout = 2;
                                            } else {
                                                likeaction = "1";
                                                fevorite.setImageResource( R.drawable.ic_favorite_border );
                                                loadlike();
                                                cout = 1;
                                            }

                                        }
                                    } );
                                }  else {
                                    wishll.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Animation animFadein = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fade_in );
                                            wishll.startAnimation( animFadein );

                                            if (cout == 1) {
                                                likeaction = "1";
                                                fevorite.setImageResource( R.drawable.ic_favorite_border );
                                                loadlike();
                                                cout = 2;

                                            } else {
                                                likeaction = "2";
                                                fevorite.setImageResource( R.drawable.ic_favorite );
                                                loadlike();
                                                cout = 1;
                                            }

                                        }
                                    } );
                                }
                                final String url = jsonResponse.getString("web_image");
                                share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Picasso.with(getApplicationContext()).load(url).into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                                share.startAnimation(animFadein);

                                                Intent i = new Intent(Intent.ACTION_SEND);
                                                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                                i.setType("image/*");
                                                String fuull = " Zuway PlayStore Link : xxxxxxxxxxxxxx";
                                                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                                i.putExtra(Intent.EXTRA_TEXT, fuull);
                                                i.setType("text/plain");

                                                startActivity(Intent.createChooser(i, "Share Image"));
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                            }
                                        });
                                    }


                                });

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

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(ReleatedItemActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.parse(file.getAbsolutePath());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.Holder>{

        private Context context;
        private List<Productmodel> similarmodellist;
        String scid,cid,pid;

        public SimilarProductAdapter(Context context, List<Productmodel> similarmodellist) {
            this.context = context;
            this.similarmodellist = similarmodellist;
        }

        public  class Holder extends RecyclerView.ViewHolder {
            TextView item_name,item_disprice,item_oldprice,item_saveprice,wishlist_count;
            ImageView item_image;
            RatingBar ratingBar;
            Context context;
            LinearLayout ll_rel;

            private Holder(View itemView) {
                super(itemView);
                context        = itemView.getContext();
                ratingBar      = itemView.findViewById(R.id.ratingBar);
                ll_rel         = itemView.findViewById(R.id.ll_rel);
                item_name      = (TextView) itemView.findViewById(R.id.item_name);
                item_disprice  = (TextView) itemView.findViewById(R.id.item_disprice);
                item_oldprice  = (TextView) itemView.findViewById(R.id.item_oldprice);
                item_saveprice = (TextView) itemView.findViewById(R.id.item_saveprice);
                item_image     = (ImageView) itemView.findViewById(R.id.item_img);

            }
        }


        @NonNull
        @Override
        public SimilarProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            SimilarProductAdapter.Holder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            viewHolder = getViewHolder(viewGroup, inflater);
            return viewHolder;
        }
        private SimilarProductAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
            SimilarProductAdapter.Holder viewHolder;
            View v1 = inflater.inflate(R.layout.layout_similar_list, viewGroup, false);
            viewHolder = new SimilarProductAdapter.Holder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SimilarProductAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {

            final Productmodel item = similarmodellist.get(position);
            holder.item_name.setText(item.getProduct_name());
            holder.item_disprice.setText("₹ "+item.getProduct_disprice()+".00");
            holder.item_oldprice.setText("₹ "+item.getProduct_oldprice()+".00");
            holder.item_saveprice.setText("save ₹"+item.getProduct_saveprice()+".00");
            holder.ratingBar.setRating(item.getProduct_ratting());

            String img=item.getProduct_img();
            Glide.with(context)
                    .load(img)
                    .placeholder(R.drawable.list_gray)
                    .into(holder.item_image);

            holder.ll_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Productmodel model = similarmodellist.get(position);
                    ItemActivity.productmodel = model;
                    pid = similarmodellist.get(position).getPid();

                    scid = similarmodellist.get(position).getSubcate_id();
                    cid = similarmodellist.get(position).getCate_id();

                    Log.d("ids",pid+cid+scid);
                    Intent i = new Intent(context, ItemActivity.class);
                    i.putExtra("scid", scid);
                    i.putExtra("cid", cid);
                    i.putExtra("pid", pid);
                    context.startActivity(i);
                }
            });

        }

        @Override
        public int getItemCount() {
            return similarmodellist == null ? 0 : similarmodellist.size();
        }


    }


    private void loadcolor() {
        final Map<String, String> params = new HashMap<>();

        colormodellist = new ArrayList<>();
        String userid = BSession.getInstance().getUser_id(getApplicationContext());
        String par_str = "?scid=" + productmodel.getSubcate_id();
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;
        String baseUrl = ProductConfig.productview + par_str + par_str1 + par_str2;


        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {


                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList1");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    final Productmodel model = new Productmodel();
                                    model.setProduct_color_id(jsonObject.getString("coid"));
                                    model.setProduct_color(jsonObject.getString("colorname"));


                                    colormodellist.add(model);
                                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    color.setLayoutManager(linearLayoutManager2);
                                    colorProductAdapter = new ColorProductAdapter(ReleatedItemActivity.this, colormodellist);
                                    color.setAdapter(colorProductAdapter);

                                    color.addOnItemTouchListener(
                                            new RecyclerItemClickListener(ReleatedItemActivity.this, color, new RecyclerItemClickListener.ClickListener() {

                                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                @Override
                                                public void onItemClick(View view, final int position) {
                                                    Productmodel model = colormodellist.get(position);

                                                    for (int i = 0; i < size.getChildCount(); i++) {

                                                        if (position == i) {

                                                            color.getChildAt(i).setBackground(getDrawable(R.drawable.border_sizebg));
                                                            colorr = colormodellist.get(position).getProduct_color();

                                                        } else {
                                                            color.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onLongItemClick(View view, int position) {

                                                }
                                            })
                                    );

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

    private void loadsize() {

        final Map<String, String> params = new HashMap<>();
        String userid = BSession.getInstance().getUser_id(getApplicationContext());
        sizemodellist = new ArrayList<>();
        String par_str = "?scid=" + productmodel.getSubcate_id();
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;
        String baseUrl = ProductConfig.productview + par_str + par_str1 + par_str2;

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
                                    final Productmodel model = new Productmodel();
                                    model.setProduct_size_id(jsonObject.getString("sid"));
                                    model.setProduct_size(jsonObject.getString("sizename"));


                                    sizemodellist.add(model);
                                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    size.setLayoutManager(linearLayoutManager1);
                                    sizeProductAdapter = new SizeProductAdapter(ReleatedItemActivity.this, sizemodellist);
                                    size.setAdapter(sizeProductAdapter);

                                    size.addOnItemTouchListener(
                                            new RecyclerItemClickListener(getApplicationContext(), size, new RecyclerItemClickListener.ClickListener() {

                                                @Override
                                                public void onItemClick(View view, final int position) {
                                                    Productmodel model = sizemodellist.get(position);

                                                    for (int i = 0; i < size.getChildCount(); i++) {

                                                        if (position == i) {

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                size.getChildAt(i).setBackground(getDrawable(R.drawable.border_sizebg));
                                                            }
                                                            sizee = sizemodellist.get(position).getProduct_size();

                                                        } else {


                                                            size.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);


                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onLongItemClick(View view, int position) {

                                                }
                                            })
                                    );

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

    private void loadslider() {

        final Map<String, String> params = new HashMap<>();
        apiSliderList    = new ArrayList<>();
        sliderModel      = new Productmodel();
        String userid    = BSession.getInstance().getUser_id(getApplicationContext());
        String para_str  = "?scid=" + productmodel.getSubcate_id();
        String para_str1 = "&pid=" + pid;
        String par_str2  = "&user_id=" + userid;
        String baseUrl   = ProductConfig.productview + para_str + para_str1 + par_str2;

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList2");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    Productmodel model = new Productmodel();

                                    model.setProduct_listimg(jsonObject.getString("multi_image"));
                                    model.setProduct_listimg_id(jsonObject.getString("image_id"));
                                    apiSliderList.add(model);
                                }

                                setUpSlider();
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

    private void setUpSlider() {

        customCarouselView.setViewListener(viewListener);
        customCarouselView.setPageCount(apiSliderList.size());
        customCarouselView.setSlideInterval(100000);
        customCarouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
    }

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        auto_search = toolbar.findViewById(R.id.auto_search);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        actionView = MenuItemCompat.getActionView(menuItem).findViewById(R.id.badge_notification);
        badge_notification = (TextView) actionView.findViewById(R.id.badge_notification);


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                Intent intent = new Intent(getApplicationContext(),
                        MyCartActivity.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadgetcartcount() {

        final Map<String, String> params = new HashMap<>();


        String par_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());

        String baseUrl = ProductConfig.cartcount + par_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {


                                badge_notification = (TextView) actionView.findViewById(R.id.badge_notification);

                                badge_notification.setText(jsonResponse.getString("count"));


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

    private void loadserach() {

        spinnerList = new ArrayList<>();

        final Map<String, String> params = new HashMap<>();
        String par = "?type=" + "1";

        String baseUrl = ProductConfig.all_products+par;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {


                        JSONArray jsonArray = jsonResponse.getJSONArray("storeList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Productmodel model = new Productmodel();
                            model.setCate_id(jsonObject.getString("cid"));
                            model.setSubcate_id(jsonObject.getString("scid"));
                            model.setPid(jsonObject.getString("pid"));
                            model.setName(jsonObject.getString("product_name"));
                            model.setKeywords(jsonObject.getString("keywords"));
                            spinnerList.add(model);

                            fruitAdapter = new FruitAdapter(ReleatedItemActivity.this, R.layout.custom_row, spinnerList);
                            auto_search.setAdapter(fruitAdapter);
                            auto_search.setThreshold(1);
                            closekeboard();
                            auto_search.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Productmodel fruit = spinnerList.get(position);
                                            String text = spinnerList.get(position).getName();

                                            auto_search.setText(text);
                                            String textId = spinnerList.get(position).getCate_id();
                                            String keyword = spinnerList.get(position).getKeywords();
                                            Productmodel model = spinnerList.get(position);
                                            cid = model.getCate_id();
                                            pid = model.getPid();
                                            scid = model.getSubcate_id();

                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", keyword);
                                            auto_search.setText("");
                                            ItemListActivity.productmodel = model;
                                            startActivity(intent);


                                          /*  if (spinnerList.get(position).getCate_id().equals("0")) {


                                                Productmodel model = spinnerList.get(position);
                                                String cid = model.getId();

                                                Intent intent = new Intent(getApplicationContext(), ReleatedItemActivity.class);
                                                intent.putExtra("scid", scid);
                                                intent.putExtra("cid", cid);
                                                auto_search.setText("");
                                                startActivity(intent);

                                            } else if (!spinnerList.get(position).getCate_id().equals("0")) {
                                                Productmodel model = spinnerList.get(position);

                                                String scid = model.getId();
                                                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                                intent.putExtra("scid", scid);
                                                intent.putExtra("cid", cid);
                                                auto_search.setText("");
                                                startActivityForResult(intent, 1);


                                            }
*/

                                        }
                                    });

                            auto_search.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    // When user changed the Text
                                    fruitAdapter.getFilter().filter(cs);
                                }

                                @Override
                                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                              int arg3) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void afterTextChanged(Editable arg0) {
                                    // TODO Auto-generated method stub
                                }
                            });
                        }
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
    public void closekeboard()
    {
        View view=this.getCurrentFocus();
        if(view!=null){
            InputMethodManager inputMethodManager=(InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE );
            inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(),0 );
        }
    }

    private void loadaddcart() {
        final Map<String, String> params = new HashMap<>();

        progressDialog.show();

        String par_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&cid=" + productmodel.getCate_id();
        String par_str3 = "&scid=" + productmodel.getSubcate_id();
        String par_str4 = "&quantity=" + tv_qty.getText().toString();
        String par_str5 = "&size=" + sizee;
        String par_str6 = "&colour=" + colorr;

        Log.d("user_id=",BSession.getInstance().getUser_id(getApplicationContext()));
        Log.d("product_id=",pid);
        Log.d("cid=",productmodel.getCate_id());
        Log.d("scid=",productmodel.getSubcate_id());
        Log.d("quantity=",tv_qty.getText().toString());
        Log.d("size=",sizee);
        Log.d("colour=",colorr);

        progressDialog.show();
        String baseUrl = ProductConfig.addcart + par_str + par_str1 + par_str2 + par_str3 + par_str4 + par_str5 + par_str6;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            progressDialog.dismiss();
                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                Productmodel model = new Productmodel();

                                Intent intent = new Intent(ReleatedItemActivity.this, MyCartActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            } else {
                                progressDialog.dismiss();
                              Toast.makeText(getApplicationContext(), "no list items or Out Of Stock", Toast.LENGTH_SHORT).show();
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
                        progressDialog.dismiss();
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


    private void loadbuy() {
        final Map<String, String> params = new HashMap<>();

        progressDialog.show();

        String par_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&cid=" + productmodel.getCate_id();
        String par_str3 = "&scid=" + productmodel.getSubcate_id();
        String par_str4 = "&quantity=" + tv_qty.getText().toString();
        String par_str5 = "&size=" + sizee;
        String par_str6 = "&colour=" + colorr;

        progressDialog.show();
        String baseUrl = ProductConfig.buynow + par_str + par_str1 + par_str2 + par_str3 + par_str4 + par_str5 + par_str6;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            progressDialog.dismiss();
                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                Productmodel model = new Productmodel();


                                Intent intent = new Intent(ReleatedItemActivity.this, OrderPlacedActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                intent.putExtra("order_id", jsonResponse.getString("order_id"));
                                intent.putExtra("total", jsonResponse.getString("total"));
                                intent.putExtra("product_count", jsonResponse.getString("product_count"));
                                intent.putExtra("grandtotal", jsonResponse.getString("grandtotal"));
                                intent.putExtra("deliveryamount", jsonResponse.getString("deliveryamount"));
                               // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);



                            } else {
                                progressDialog.dismiss();
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
                        progressDialog.dismiss();
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


   /* @Override
    public void onBackPressed() {
        Intent intent=new Intent(ReleatedItemActivity.this,HomeActivity.class);
        startActivity(intent);

    }*/


    @Override
    public void onRestart() {
        super.onRestart();
        //finish();
        startActivity(getIntent());
    }

    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

      //  mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                scid = extras.getString("scid");
                cid = extras.getString("cid");
                pid = extras.getString("pid");
            }
            Intent intent=new Intent(ReleatedItemActivity.this,ReleatedItemActivity.class);
            intent.putExtra("scid",scid);
            intent.putExtra("cid",cid);
            intent.putExtra("pid",pid);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(ReleatedItemActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }

}
