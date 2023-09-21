package com.ZuWay.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ZuWay.atapter.ReviewListAdapter;
import com.ZuWay.utils.NotificationONOff;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;
import com.ZuWay.EndActivity;
import com.ZuWay.R;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.atapter.SimilarProductAdapter;

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

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import pl.droidsonroids.gif.GifImageView;

public class ItemActivity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Productmodel productmodel = new Productmodel();
    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    ImageView imagevieww,produ_img, fevorite, share;
    String sizee="", colorr="", scid, wishstatus;
    TextView item_name, item_dis, item_stock, item_oldprice, item_saveprice, item_disprice, item_sold, item_brand,
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
    GifImageView simpleProgressBar;
    ProgressDialog progressDialog;

    Uri bmpUri;
    public static final int PERMISSION_WRITE = 0;
    String fileUri;
    String url;
    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_customm, null);


            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);


            Productmodel model = apiSliderList.get(position);
            String url = model.getProduct_listimg();
            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.logo_big)
                    .into(fruitImageView);
            customView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent(ItemActivity1.this,ZoomActivity.class);
                    intent.putExtra("scid",scid);
                    intent.putExtra("pid",pid);
                    startActivity(intent);
                }
            });
            return customView;
        }
    };

    RelativeLayout wishll;
    int cout = 1;
    String likeaction = "";
    AutoCompleteTextView auto_search;
    TextView badge_notification, tv_plus, tv_qty, tv_add, tv_minus;
    View actionView;
    Toolbar toolbar;
    Integer i;
    String customer_id, pid, cid;
    String editqut, type,status="1";
    String  offer, quntity,variation;
    FrameLayout offer_ll;
    TextView offertx;
    boolean doubleBackToExitPressedOnce = false;
    private RecyclerView itemrecy;
    private List<Productmodel> itemmodellist;
    private RecyclerView size;
    private List<Productmodel> sizemodellist;
    private RecyclerView color,reviewslist;
    private List<Productmodel> colormodellist;
    private List<Productmodel> reviewlist;
    private FruitAdapter fruitAdapter;
    private BottomSheetDialog mBottomSheetDialog;
    LinearLayout sizell,colorll,lin_search;
    boolean showingFirst = true;
    String count;
    TextView about;
    boolean mFocusDuringOnPause;
    TextView notifi;
    ToggleButton toggleButton;
    ImageView image_toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item1);

        progressDialog     = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");

        simpleProgressBar  =  findViewById(R.id.simpleProgressBar);


        bundle();
        init();
        loadslider();
        toolbar();
        loadserach();
        loadproductview();
        loadreview();
        addcart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       String user_id = BSession.getInstance().getUser_id(ItemActivity1.this);
       System.out.println("user_id---"+user_id);
       if (user_id.equalsIgnoreCase("")){
           Intent intent=new Intent(ItemActivity1.this,LoginActivity.class);
           startActivity(intent);
       }else{

                Animation animFadein = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fade_in );
                addcart.startAnimation( animFadein );

                Log.d( "sele", colorr + sizee );

                if (tv_qty.getText().toString().equalsIgnoreCase( "0" )) {
                    tv_qty.setText( "1" );
                    loadaddcart();
                } else {
                    loadaddcart();
                }
      }
            }
        } );

        buynow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String user_id = BSession.getInstance().getUser_id(ItemActivity1.this);
                System.out.println("user_id---"+user_id);
                if (user_id.equalsIgnoreCase("")){
                    Intent intent=new Intent(ItemActivity1.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    buynow.startAnimation(animFadein);

                    if (tv_qty.getText().toString().equalsIgnoreCase("0")) {
                        tv_qty.setText("1");
                        loadbuy();
                        //loadaddcart();
                    } else {
                        loadbuy();
                        // loadaddcart();
                   }
                }

            }
        } );

        //  progressDialog.dismiss();

        itemmodellist = new ArrayList<>();
        loadreleated();
        sizemodellist = new ArrayList<>();
        loadsize();
        colormodellist = new ArrayList<>();
        loadcolor();

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

        int qut= Integer.parseInt(tv_qty.getText().toString());
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
                        if(actionId== EditorInfo.IME_ACTION_DONE){
                            final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());
                            pid = productmodel.getPid();
                            editqut = qyutt.getText().toString();

                            typee.setText(editqut);

                            type = typee.getText().toString();
                            tv_qty.setText(editqut);

                            if(editqut!=null){
                                hideKeyboard(ItemActivity1.this);
                            }

                            mBottomSheetDialog.dismiss();
                        }
                        return false;
                    }
                });

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

                mBottomSheetDialog = new BottomSheetDialog(ItemActivity1.this);
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

    }

    public  void bundle(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            scid = extras.getString("scid");
            cid = extras.getString("cid");
            pid = extras.getString("pid");

            System.out.println(BSession.getInstance().getUser_id(getApplicationContext()));
            System.out.println("piddla:" + pid);
            System.out.println("ciddla:" + cid);
            System.out.println("sciddla:" + scid);
        }
        //}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

        }
        if (resultCode == Activity.RESULT_CANCELED) {
            // Write your code if there's no result
        }
        else{

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
        sizell             = findViewById(R.id.sizell);
        colorll            = findViewById(R.id.colorll);
        about              = findViewById( R.id.about );
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
        imagevieww         = findViewById(R.id.imagevieww);
        reviewslist        = findViewById(R.id.reviewslist);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ItemActivity1.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(ItemActivity1.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(ItemActivity1.this, MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(ItemActivity1.this, MyWishActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_shipping) {
            Intent intent = new Intent(ItemActivity1.this, ShippingandRetrun.class);
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(ItemActivity1.this, PrivacyPolicy.class);
            startActivity(intent);

        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(ItemActivity1.this, TermsandContidion.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(ItemActivity1.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(ItemActivity1.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            logoutAlert();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void logoutAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme);
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

        BSession.getInstance().destroy(ItemActivity1.this);
        Toast.makeText(ItemActivity1.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ItemActivity1.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }


    private void loadlike() {

        final String customer_id = BSession.getInstance().getUser_id(getApplicationContext());

        final Map<String, String> params = new HashMap<>();

        String par_str  = "?user_id=" + customer_id;
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&status=" + likeaction;


        if (progressDialog!=null) {

            progressDialog.show();

        }

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

                        status=jsonResponse.getString("status");

                        String mess = jsonResponse.getString("message");
                        if (mess.equals("Successfully liked whislist")) {
                            fevorite.setImageResource(R.drawable.ic_favorite);
                            item_wishlistcount.setText(jsonResponse.getString("count"));

                            Toast.makeText(ItemActivity1.this, " Thanks for your like", Toast.LENGTH_SHORT).show();
                        } else {
                            item_wishlistcount.setText(jsonResponse.getString("count"));
                            fevorite.setImageResource(R.drawable.ic_favorite_border);
                            Toast.makeText(ItemActivity1.this, " Remove like", Toast.LENGTH_SHORT).show();

                        }



                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }if (progressDialog!=null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub

                Log.e("Error", error.toString());
                if (progressDialog!=null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

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

        String par_str  = "?scid=" + scid;
        String par_str1 = "&pid=" + pid;

        if (progressDialog!=null) {

            progressDialog.show();

        }

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
                                    similarProductAdapter = new SimilarProductAdapter(ItemActivity1.this, itemmodellist);
                                    itemrecy.setAdapter(similarProductAdapter);

                                    itemrecy.addOnItemTouchListener(
                                            new RecyclerItemClickListener(getApplicationContext(), color, new RecyclerItemClickListener.ClickListener() {

                                                @Override
                                                public void onItemClick(View view, final int position) {


                                                    Productmodel model = itemmodellist.get(position);
                                                    ItemActivity1.productmodel = model;
                                                    String pid  = itemmodellist.get(position).getPid();
                                                    String scid = itemmodellist.get(position).getSubcate_id();
                                                    String cid  = itemmodellist.get(position).getCate_id();
                                                    Log.d("ids", pid + cid + scid);
                                                    Intent i = new Intent(ItemActivity1.this,ItemActivity.class);
                                                    i.putExtra("scid", scid);
                                                    i.putExtra("cid", cid);
                                                    i.putExtra("pid", pid);
                                                    startActivity(i);



                                                }

                                                @Override
                                                public void onLongItemClick(View view, int position) {

                                                }
                                            })
                                    );

                                    pre.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            onBackPressed();

                                           /* Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            pre.startAnimation(animFadein);
                                            scid = model.getSubcate_id();
                                            pid  = model.getPid();
                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("keyword", "");
                                           // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);*/

                                        }
                                    });

                                    next.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                            next.startAnimation(animFadein);
                                            scid = model.getSubcate_id();
                                            pid = model.getPid();
                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", "");
                                            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });

                                }

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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
        String par_str  = "?scid=" + scid;
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;

        if (progressDialog!=null) {

            progressDialog.show();

        }

        String baseUrl = ProductConfig.productview + par_str + par_str1 + par_str2;
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
                                item_dis.setText( Html.fromHtml(String.valueOf( Html.fromHtml(jsonResponse.getString("short_content")))));
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
                                wishstatus=(jsonResponse.getString("wishlist_count"));
                                variation=jsonResponse.getString("variation");
                                about.setText(  Html.fromHtml(String.valueOf(Html.fromHtml(jsonResponse.getString("web_content")))) );
                                item_category.setText(jsonResponse.getString("product_category"));
                                count=jsonResponse.getString("totalcount");

                                if (jsonResponse.getString("totalcount").equalsIgnoreCase("0")) {
                                    item_stock.setText("Out of Stock");

                                } else {
                                    item_stock.setText("In Stock");

                                }


                                if (!offer.equals("0")) {
                                    offer_ll.setVisibility(View.VISIBLE);
                                    offertx.setText(offer + "%");
                                }

                                if(variation.equalsIgnoreCase("No")){
                                    sizell.setVisibility(View.GONE);
                                    colorll.setVisibility(View.GONE);
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
                                }  else   {
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

                                url = jsonResponse.getString("web_image");
                                Glide.with(getApplicationContext())
                                        .load(url)
                                        .placeholder(R.drawable.logo_big)
                                        .into(imagevieww);

                                share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imagevieww.getDrawable();
                                        Bitmap bitmap = bitmapDrawable.getBitmap();
                                        shareImageandText(bitmap);
                                    }
                                });


                              /*  share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Picasso.with(getApplicationContext()).load(url).into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                                share.startAnimation(animFadein);
                                                progressDialog.show();
                                                if(share.isEnabled()){

                                                if (showingFirst == true) {
                                                    getLocalBitmapUri(bitmap);
                                                    share.setEnabled(false);
                                                    try {
                                                    Intent i = new Intent(Intent.ACTION_SEND);
                                                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    i.setType("image/*");
                                                    String fuull = "Zuway APP Link :  \"https://play.google.com/store/apps/details?id=com.ZuWay";
                                                    //i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                                    i.putExtra(Intent.EXTRA_TEXT, fuull);
                                                    i.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                                    i.setType("text/plain");
                                                   // i.setPackage("com.whatsapp");
                                                    ItemActivity1.this.startActivity(i);
                                                    } catch (android.content.ActivityNotFoundException ex) {
                                                    }

                                                    progressDialog.dismiss();

                                                } else {
                                                    progressDialog.dismiss();
                                                Toast.makeText(ItemActivity.this, "Wait for responce", Toast.LENGTH_SHORT).show();
                                                }

                                                }


                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable errorDrawable) {
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                            }
                                        });
                                   }


                               });*/

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        String fuull = "Zuway APP Link :  \"https://play.google.com/store/apps/details?id=com.ZuWay";
        intent.putExtra(Intent.EXTRA_TEXT, fuull);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Zuway Shoping !");
        intent.setType("image/png");
        Intent chooser = Intent.createChooser(intent, "Share File");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooser);

    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }


    public Uri getLocalBitmapUri(Bitmap bmp) {
        bmpUri = null;
        try {
            File file = new File(ItemActivity1.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.parse(file.getAbsolutePath());
            Log.d("bmuri0", String.valueOf(bmpUri));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void loadcolor() {

        final Map<String, String> params = new HashMap<>();
        colormodellist  = new ArrayList<>();
        String userid   = BSession.getInstance().getUser_id(getApplicationContext());
        String par_str  = "?scid=" + scid;
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;

        if (progressDialog!=null) {

            progressDialog.show();

        }

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
                                    System.out.println("colourname==="+model.getProduct_color());
                                    colormodellist.add(model);
                                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    color.setLayoutManager(linearLayoutManager2);
                                    colorProductAdapter = new ColorProductAdapter(ItemActivity1.this, colormodellist);
                                    color.setAdapter(colorProductAdapter);

                                }

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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

    private void loadreview() {

        final Map<String, String> params = new HashMap<>();
        reviewlist  = new ArrayList<>();
        String userid   = BSession.getInstance().getUser_id(getApplicationContext());
        String par_str  = "?product_id=" + pid;


        if (progressDialog!=null) {

            progressDialog.show();

        }

        String baseUrl = ProductConfig.usercomment + par_str ;
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
                                    model.setReview_status(jsonObject.getString("review_status"));
                                    model.setComment(jsonObject.getString("comment"));
                                    model.setCommand_date(jsonObject.getString("command_date"));
                                    model.setName(jsonObject.getString("name"));


                                    reviewlist.add(model);
                                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                    reviewslist.setLayoutManager(linearLayoutManager2);
                                    ReviewListAdapter colorProductAdapter = new ReviewListAdapter(ItemActivity1.this, reviewlist);
                                    reviewslist.setAdapter(colorProductAdapter);

                                }

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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


    public class ColorProductAdapter extends RecyclerView.Adapter<ColorProductAdapter.Holder>{

        private Context context;
        private List<Productmodel> electrmodellist;


        int row_index;
        public ColorProductAdapter(Context context, List<Productmodel> electrmodellist) {
            this.context = context;
            this.electrmodellist = electrmodellist;
        }

        public  class Holder extends RecyclerView.ViewHolder {

            public TextView size_tx;
            LinearLayout select_ll;
            private Holder(View itemView) {
                super(itemView);

                size_tx = itemView.findViewById(R.id.size_tx);
                select_ll = itemView.findViewById(R.id.select);
            }
        }
        @NonNull
        @Override
        public ColorProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ColorProductAdapter.Holder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            viewHolder = getViewHolder(viewGroup, inflater);
            return viewHolder;
        }
        private ColorProductAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
            ColorProductAdapter.Holder viewHolder;
            View v1 = inflater.inflate(R.layout.layout_size, viewGroup, false);
            viewHolder = new ColorProductAdapter.Holder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ColorProductAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {

            final Productmodel model = electrmodellist.get(position);

            holder.size_tx.setText(model.getProduct_color());

            holder.select_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colorr = electrmodellist.get(position).getProduct_color();
                    Productmodel model = electrmodellist.get(position);
                    row_index=position;
                    notifyDataSetChanged();


                }
            });

            if(row_index==position){
                holder.select_ll.setBackgroundResource(R.drawable.border_sizebg);
                colorr = electrmodellist.get(position).getProduct_color();
                Log.d("color",colorr);

            }
            else
            {
                holder.select_ll.setBackgroundColor(Color.parseColor("#ffffff"));

            }


        }

        @Override
        public int getItemCount() {
            return electrmodellist == null ? 0 : electrmodellist.size();
        }


    }


    private void loadsize() {

        final Map<String, String> params = new HashMap<>();
        String userid   = BSession.getInstance().getUser_id(getApplicationContext());
        sizemodellist   = new ArrayList<>();
        String par_str  = "?scid=" + scid;
        String par_str1 = "&pid=" + pid;
        String par_str2 = "&user_id=" + userid;
        String baseUrl  = ProductConfig.productview + par_str + par_str1 + par_str2;
        if (progressDialog!=null) {

            progressDialog.show();

        }

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

                                    System.out.println("productsize=="+model.getProduct_size());

                                    sizemodellist.add(model);
                                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                                    size.setLayoutManager(linearLayoutManager1);
                                    sizeProductAdapter = new SizeProductAdapter(ItemActivity1.this, sizemodellist);
                                    size.setAdapter(sizeProductAdapter);

                                }

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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


    public class SizeProductAdapter extends RecyclerView.Adapter<SizeProductAdapter.Holder>{

        private Context context;
        private List<Productmodel> electrmodellist;
        int row_index;

        public SizeProductAdapter(Context context, List<Productmodel> electrmodellist) {
            this.context = context;
            this.electrmodellist = electrmodellist;
        }

        public  class Holder extends RecyclerView.ViewHolder {

            public TextView size_tx;
            LinearLayout select_ll;
            private Holder(View itemView) {
                super(itemView);
                select_ll = itemView.findViewById(R.id.select);
                size_tx = itemView.findViewById(R.id.size_tx);
            }
        }
        @NonNull
        @Override
        public SizeProductAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            SizeProductAdapter.Holder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            viewHolder = getViewHolder(viewGroup, inflater);
            return viewHolder;
        }
        private SizeProductAdapter.Holder getViewHolder(ViewGroup viewGroup, LayoutInflater inflater) {
            SizeProductAdapter.Holder viewHolder;
            View v1 = inflater.inflate(R.layout.layout_size, viewGroup, false);
            viewHolder = new SizeProductAdapter.Holder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SizeProductAdapter.Holder holder, @SuppressLint("RecyclerView") final int position) {

            final Productmodel model = electrmodellist.get(position);

            holder.size_tx.setText(model.getProduct_size());


            holder.select_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Productmodel model = electrmodellist.get(position);

                    row_index = position;
                    notifyDataSetChanged();
                }
            });

            if (row_index == position) {
                holder.select_ll.setBackgroundResource(R.drawable.border_sizebg);
                sizee=electrmodellist.get(position).getProduct_size();
             System.out.println("sizeeeee"+electrmodellist.get(position).getProduct_size());

            } else {
                holder.select_ll.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        }
        @Override
        public int getItemCount() {
            return electrmodellist == null ? 0 : electrmodellist.size();
        }


    }

    private void loadslider() {

        final Map<String, String> params = new HashMap<>();

        apiSliderList    = new ArrayList<>();
        sliderModel      = new Productmodel();
        String userid    = BSession.getInstance().getUser_id(getApplicationContext());
        String para_str  = "?scid=" +scid;
        String para_str1 = "&pid=" + pid;
        String par_str2  = "&user_id=" + userid;
        String baseUrl   = ProductConfig.productview + para_str + para_str1 + par_str2;

        if (progressDialog!=null) {

            progressDialog.show();

        }

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
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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
        lin_search = toolbar.findViewById(R.id.lin_search);

        auto_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        } );
        lin_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("---test"+"tstttt1");
                String edit=auto_search.getText().toString().trim();
                if(edit.isEmpty()||edit==null){
                    System.out.println("---test"+"tstttt2");

                }else{
                    System.out.println("---test"+"tstttt3");
                    Intent intent=new Intent(ItemActivity1.this,ItemListActivity.class);
                    intent.putExtra("keyword",auto_search.getText().toString().trim());
                    intent.putExtra("cid",cid);
                    intent.putExtra("scid",scid);
                    intent.putExtra("pid",pid);
                    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        auto_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String edit=auto_search.getText().toString().trim();
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {

                    if(edit.isEmpty()||edit==null){
                        System.out.println("---test"+"tstttt2");

                    }else{
                        System.out.println("---test"+"tstttt3");
                        Intent intent=new Intent(ItemActivity1.this,ItemListActivity.class);
                        intent.putExtra("keyword",auto_search.getText().toString().trim());
                        intent.putExtra("cid",cid);
                        intent.putExtra("scid",scid);
                        intent.putExtra("pid",pid);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        actionView = MenuItemCompat.getActionView(menuItem).findViewById(R.id.badge_notification);
        badge_notification = (TextView) actionView.findViewById(R.id.badge_notification);
        loadgetcartcount();

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
                intent.putExtra("scid",scid);
                intent.putExtra("cid",cid);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadgetcartcount() {

        final Map<String, String> params = new HashMap<>();

        String par_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());

        if (progressDialog!=null) {

            progressDialog.show();

        }

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
                        }if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        if (progressDialog!=null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

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

                            final FruitAdapter   fruitAdapter = new FruitAdapter(ItemActivity1.this, R.layout.custom_row, spinnerList);
                            auto_search.setAdapter(fruitAdapter);
                            auto_search.setThreshold(1);

                            auto_search.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Productmodel fruit = spinnerList.get(position);
                                            String text = spinnerList.get(position).getName();

                                            auto_search.setText(text);

                                            Productmodel model = spinnerList.get(position);
                                            String scid = model.getSubcate_id();
                                            String cid = model.getCate_id();
                                            String pid = model.getPid();
                                            String keyword = spinnerList.get(position).getKeywords();

                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", keyword);

                                            auto_search.setText("");
                                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);


                                        }
                                    });

                            auto_search.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
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
            inputMethodManager.hideSoftInputFromWindow( view.getWindowToken(),1 );
        }
    }
    private void loadaddcart() {
        final Map<String, String> params = new HashMap<>();


        String par_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&cid=" + cid;
        String par_str3 = "&scid=" + scid;
        String par_str4 = "&quantity=" + tv_qty.getText().toString();
        String par_str5 = "&size=" + sizee;
        String par_str6 = "&colour=" + colorr;
        // productmodel.getCate_id()
        // productmodel.getSubcate_id()
        System.out.println("user_id="+BSession.getInstance().getUser_id(getApplicationContext()));
        System.out.println("product_id="+pid);
        System.out.println("cid="+cid);
        System.out.println("scid="+scid);
        System.out.println("quantity="+tv_qty.getText().toString());
        System.out.println("size="+sizee);
        System.out.println("colour="+colorr);

        simpleProgressBar.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.addcart + par_str + par_str1 + par_str2 + par_str3 + par_str4 + par_str5 + par_str6;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                Productmodel model = new Productmodel();

                                Intent intent = new Intent(ItemActivity1.this, MyCartActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }
                            else {
                                Toast.makeText(getApplicationContext(), "no list items or Out Of Stock", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }        simpleProgressBar.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        // progressDialog.dismiss();
                        simpleProgressBar.setVisibility(View.GONE);

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

        String par_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&cid=" + cid;
        String par_str3 = "&scid=" + scid;
        String par_str4 = "&quantity=" + tv_qty.getText().toString();
        String par_str5 = "&size=" + sizee;
        String par_str6 = "&colour=" + colorr;

        simpleProgressBar.setVisibility(View.VISIBLE);

        System.out.println(BSession.getInstance().getUser_id(getApplicationContext()));
        System.out.println("pid:"+pid);
        System.out.println("cid"+cid);
        System.out.println(scid);
        System.out.println(tv_qty.getText().toString().trim());
        System.out.println(sizee);
        System.out.println(colorr);

        String baseUrl = ProductConfig.buynow + par_str + par_str1  + par_str2+par_str3+par_str4 + par_str5 + par_str6;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                Productmodel model = new Productmodel();
                                Intent intent = new Intent(ItemActivity1.this, OrderPlacedActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                intent.putExtra("order_id", jsonResponse.getString("order_id"));
                                intent.putExtra("total", jsonResponse.getString("total"));
                                intent.putExtra("product_count", jsonResponse.getString("product_count"));
                                intent.putExtra("grandtotal", jsonResponse.getString("grandtotal"));
                                intent.putExtra("deliveryamount", jsonResponse.getString("deliveryamount"));
                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else {

                                Toast.makeText(getApplicationContext(), "no list items or Out Of Stock", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }        simpleProgressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        simpleProgressBar.setVisibility(View.GONE);
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

    private void cartsave() {

        final Map<String, String> params = new HashMap<>();

        String par_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String par_str1 = "&product_id=" + pid;
        String par_str2 = "&cid=" + productmodel.getCate_id();
        String par_str3 = "&scid=" + productmodel.getSubcate_id();
        String par_str4 = "&quantity=" + tv_qty.getText().toString();
        String par_str5 = "&size=" + sizee;
        String par_str6 = "&colour=" + colorr;

        simpleProgressBar.setVisibility(View.VISIBLE);
        String baseUrl = ProductConfig.addcart + par_str + par_str1 + par_str2 + par_str3 + par_str4 + par_str5 + par_str6;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                                Productmodel model = new Productmodel();
                                Intent intent = new Intent(ItemActivity1.this, MyCartActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("2")) {
                                Toast.makeText(getApplicationContext(), "Unfortunately you can't add the product , Already added in your cart", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ItemActivity1.this, MyCartActivity.class);
                                intent.putExtra("scid", scid);
                                intent.putExtra("cid", cid);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                            else {

                                Toast.makeText(getApplicationContext(), "no list items or Out Of Stock", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }        simpleProgressBar.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        // progressDialog.dismiss();
                        simpleProgressBar.setVisibility(View.GONE);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    // Alternative variant for API 5 and higher
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }*/
 /*   @Override
    public void onPause() {
     super.onPause();
        Intent intent=new Intent(ItemActivity1.this,HomeActivity.class);
        startActivity(intent);
    }*/
  /*  @Override
    public void onResume() {
        super.onResume();
        Intent intent=new Intent(ItemActivity.this,HomeActivity.class);
        startActivity(intent);
    }*/



   /*@Override
    public void onBackPressed() {
     //  moveTaskToBack(true);
     //  super.onBackPressed();

       Intent i = new Intent(getApplicationContext(), ItemListActivity.class);
        i.putExtra("cid",cid);
        i.putExtra("scid",scid);
        i.putExtra("keyword", "");
        System.out.println("cid:"+cid);
        System.out.println("scid:"+scid);
        startActivity(i);

    }*/



   /* @Override
    public void onBackPressed() {
        finish();

      *//*  if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(), EndActivity.class);
            startActivity(i);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to close Zuway", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                finish();
            }
        }, 20);
    *//*
    }*/


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
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        //mFocusDuringOnPause = hasWindowFocus();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Toast.makeText(ItemActivity.this, "subOnResume", Toast.LENGTH_SHORT).show();

    }
    public void onStop() {
        super.onStop();

        // if(mFocusDuringOnPause) {


        // Toast.makeText(ItemActivity.this, "normal", Toast.LENGTH_SHORT).show();

        // normal scenario
     /*   } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {


                scid = extras.getString("scid");
                cid = extras.getString("cid");
                pid = extras.getString("pid");

                System.out.println(BSession.getInstance().getUser_id(getApplicationContext()));
                System.out.println("piddla:" + pid);
                System.out.println("ciddla:" + cid);
                System.out.println("sciddla:" + scid);
            }
            Intent intent=new Intent(ItemActivity.this,ItemActivity.class);
            intent.putExtra("cid",cid);
            intent.putExtra("scid",scid);
            intent.putExtra("pid",pid);

            startActivity(intent);
            Toast.makeText(ItemActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}
