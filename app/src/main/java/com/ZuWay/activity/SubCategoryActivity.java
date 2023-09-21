package com.ZuWay.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;

import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.material.navigation.NavigationView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;
import com.ZuWay.EndActivity;
import com.ZuWay.R;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.atapter.SubTitleAdapter;
import com.ZuWay.atapter.SubcateGritAdapter;
import com.ZuWay.model.ExpandableHeightGridView;
import com.ZuWay.model.Productmodel;
import com.ZuWay.model.SubTitleGridModel;
import com.ZuWay.model.SubTitleModel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SubCategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static SubCategoryActivity lastPausedActivity = null;

    SubcateGritAdapter subcateGritAdapter;
    SubTitleAdapter subTitleAdapter;
    CarouselView customCarouselView;
    List<Productmodel> apiSliderList = new ArrayList<>();
    Productmodel sliderModel = new Productmodel();
    String cid,scid,pid;
    boolean doubleBackToExitPressedOnce = false;
    private ExpandableHeightGridView subcate;
    private List<Productmodel> subcatemodellist;
    private RecyclerView subtitlery;
    private List<SubTitleModel> subtitlemodellist;
    private FruitAdapter fruitAdapter;
    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    AutoCompleteTextView auto_search;
    TextView badge_notification;
    View actionView;
    Toolbar toolbar;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    NavigationView navigationView;
    boolean mFocusDuringOnPause=true;
    TextView notifi;
    ToggleButton toggleButton;
    ImageView image_toggle;

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);


            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);


            Productmodel model = apiSliderList.get(position);
            String url = model.getSubbn_img();
            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.banner_gray)
                    .into(fruitImageView);

            return customView;
        }
    };
    GifImageView imageView;
    LinearLayout owl_lin,sub_lin,lin_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_category);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");

        bundle();
        init();
        toolbar();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        notifi = hView.findViewById(R.id.notifi);
        toggleButton=hView.findViewById(R.id.toggleButton);
        image_toggle=hView.findViewById(R.id.image_toggle);

        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.tool_tit);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

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


        loadslider();
        subcatemodellist = new ArrayList<>();
        loadsubcate();
        loadserach();
        loadgetcartcount();
        subtitlemodellist = new ArrayList<>();
        setRecyclerview();

    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cid = extras.getString("cid");

        }
    }

    public void init(){

        customCarouselView = findViewById(R.id.customCarouselView);
        simpleProgressBar  = (ProgressBar) findViewById(R.id.simpleProgressBar);
        subcate            = findViewById(R.id.subcate1_cate);
        subtitlery         = findViewById(R.id.subtitlelist);
        imageView=findViewById(R.id.image);
        owl_lin=findViewById(R.id.owl_lin);
        sub_lin=findViewById(R.id.sub_lin);




    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(SubCategoryActivity.this, HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(SubCategoryActivity.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(SubCategoryActivity.this, MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(SubCategoryActivity.this, MyWishActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_shipping) {
            Intent intent = new Intent(SubCategoryActivity.this, ShippingandRetrun.class);
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(SubCategoryActivity.this, PrivacyPolicy.class);
            startActivity(intent);

        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(SubCategoryActivity.this, TermsandContidion.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(SubCategoryActivity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(SubCategoryActivity.this, AboutUs.class);
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

        BSession.getInstance().destroy(SubCategoryActivity.this);
        Toast.makeText(SubCategoryActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SubCategoryActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }

   /* @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(SubCategoryActivity.this, "subOnResume", Toast.LENGTH_SHORT).show();

    }*/
  /* @Override
   protected void onResume() {

       super.onResume();
       if(this == lastPausedActivity) {
           lastPausedActivity = null;
           Intent intent = new Intent(this, HomeActivity.class);
           intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
           startActivity( intent );
       }
   }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

    }*/


    private void toolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        auto_search =toolbar.findViewById(R.id.auto_search);
        lin_search=toolbar.findViewById(R.id.lin_search);

        auto_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_search.setInputType( InputType.TYPE_CLASS_TEXT);
                auto_search.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(v, InputMethodManager.SHOW_FORCED);
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
                    Intent intent=new Intent(SubCategoryActivity.this,ItemListActivity.class);
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
                        Intent intent=new Intent(SubCategoryActivity.this,ItemListActivity.class);
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
            actionView = MenuItemCompat.getActionView(menuItem);
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
                        intent.putExtra("scid", scid);
                        intent.putExtra("cid", cid);
                        startActivity(intent);

                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    @SuppressLint("ResourceAsColor")
    private void loadgetcartcount() {

        final Map<String, String> params = new HashMap<>();

        if (progressDialog!=null) {

            progressDialog.show();

        }
     //  imageView.setVisibility(View.VISIBLE);
      //  sub_lin.setVisibility(View.GONE);
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

                              //  imageView.setVisibility(View.GONE);
                              //  sub_lin.setVisibility(View.VISIBLE);
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
                            model.setProduct_name(jsonObject.getString("product_name"));
                            model.setKeywords(jsonObject.getString("keywords"));
                            spinnerList.add(model);

                            final FruitAdapter   fruitAdapter = new FruitAdapter(SubCategoryActivity.this, R.layout.custom_row, spinnerList);
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
                                            String product = spinnerList.get(position).getProduct_name();

                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", keyword);
                                            intent.putExtra("keyword", product);

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


    private void loadslider() {

        final Map<String, String> params = new HashMap<>();
        if (progressDialog!=null) {

            progressDialog.show();

        }
        apiSliderList = new ArrayList<>();
        sliderModel = new Productmodel();
        String par_str = "?cid=" + cid;
        String baseUrl = ProductConfig.subcategorybanner + par_str;
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

                                    model.setSubbn_img(jsonObject.getString("banner_image"));
                                    model.setSubbn_id(jsonObject.getString("bid"));
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
        customCarouselView.setSlideInterval(4000);
        customCarouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

                    Productmodel model = apiSliderList.get(position);
                    String scid = model.getSubbn_id();
                    Intent intent = new Intent(SubCategoryActivity.this, ItemListActivity.class);
                    intent.putExtra("scid", scid);
                    intent.putExtra("cid", cid);
                    intent.putExtra("keyword", "");
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

            }
        });
    }


    private void loadsubcate() {

        final Map<String, String> params = new HashMap<>();

        subcatemodellist = new ArrayList<>();
        String par_str = "?cid=" + cid;

        if (progressDialog!=null) {

            progressDialog.show();

        }

        String baseUrl = ProductConfig.subcategorylist + par_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                                    model.setSubcate_img(jsonObject.getString("web_image"));
                                    model.setSubcate_name(jsonObject.getString("web_title"));

                                    subcatemodellist.add(model);
                                    subcateGritAdapter = new SubcateGritAdapter(SubCategoryActivity.this, R.layout.layout_subcate, subcatemodellist);
                                    subcate.setAdapter(subcateGritAdapter);
                                    subcate.setNestedScrollingEnabled(false);
                                    subcate.setExpanded(true);
                                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(SubCategoryActivity.this, R.anim.layout_animation);subcate.setLayoutAnimation(controller);

                                    subcate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                Productmodel model = subcatemodellist.get(position);
                                                ItemListActivity.productmodel = model;

                                                scid = model.getSubcate_id();
                                                Intent intent = new Intent(SubCategoryActivity.this, ItemListActivity.class);
                                                intent.putExtra("scid", scid);
                                                intent.putExtra("cid", cid);
                                                intent.putExtra("keyword", "");
                                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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


    private void setRecyclerview() {

        subtitlemodellist = new ArrayList<>();
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.homesubtitle + "?cid=" + cid;

        if (progressDialog!=null) {
            progressDialog.show();
        }

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        subtitlemodellist = new ArrayList<>();
                        JSONArray array = jsonResponse.getJSONArray("storeList");


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);

                            if (product.getJSONArray("list").length() != 0) {

                                SubTitleModel subTitleModel = new SubTitleModel();
                                subTitleModel.setHome_title(array.getJSONObject(i).getString("sub_title"));
                                subTitleModel.setHome_title_id(array.getJSONObject(i).getString("title_cid"));
                                subTitleModel.setHome_title_scid(array.getJSONObject(i).getString("subtitle_scid"));

                                JSONArray gritArr = product.getJSONArray("list");
                                List<SubTitleGridModel> subgirtModelList = new ArrayList<>();
                                for (int j = 0; j < gritArr.length(); j++) {
                                    JSONObject weight_result = gritArr.getJSONObject(j);
                                    SubTitleGridModel subTitleGridModel = new SubTitleGridModel();
                                    subTitleGridModel.setCid(gritArr.getJSONObject(j).getString("cid"));
                                    subTitleGridModel.setScid(gritArr.getJSONObject(j).getString("scid"));
                                    subTitleGridModel.setPid(gritArr.getJSONObject(j).getString("pid"));
                                    subTitleGridModel.setWeb_title(gritArr.getJSONObject(j).getString("product_name"));
                                    subTitleGridModel.setWeb_image(gritArr.getJSONObject(j).getString("web_image"));
                                    subgirtModelList.add(subTitleGridModel);
                                }

                                subTitleModel.setHomegrid_title(subgirtModelList);
                                subtitlemodellist.add(subTitleModel);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(SubCategoryActivity.this, LinearLayoutManager.VERTICAL, false);
                                subtitlery.setLayoutManager(layoutManager);
                                subtitlery.setNestedScrollingEnabled(false);
                                subTitleAdapter = new SubTitleAdapter(SubCategoryActivity.this, subtitlemodellist);
                                subtitlery.setAdapter(subTitleAdapter);
                                final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(SubCategoryActivity.this, R.anim.layout_animation);subtitlery.setLayoutAnimation(controller);

                            } else {
                            }

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Product Result Found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }



  /*  @Override
    public void onBackPressed() {
        Intent intent=new Intent(SubCategoryActivity.this,HomeActivity.class);
        startActivity(intent);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Divya", "In Start Method");
    }

  @Override
    protected void onResume() {
      final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(SubCategoryActivity.this, R.anim.layout_animation);subcate.setLayoutAnimation(controller);
      final LayoutAnimationController controller1 = AnimationUtils.loadLayoutAnimation(SubCategoryActivity.this, R.anim.layout_animation);subtitlery.setLayoutAnimation(controller1);
      super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Divya", "In Destroy Method");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Divya", "In Pause Method");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Divya", "In Restart Method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Divya", "In Stop Method");
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        mFocusDuringOnPause=false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFocusDuringOnPause=true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mFocusDuringOnPause) {
            loadslider();
            subcatemodellist = new ArrayList<>();
            loadsubcate();
            loadserach();
            loadgetcartcount();
            subtitlemodellist = new ArrayList<>();
            setRecyclerview();
        }
    }*/

    /* @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }*/
       /* if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent i = new Intent(getApplicationContext(), EndActivity.class);
            startActivity(i);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 50);
    }*/
}
