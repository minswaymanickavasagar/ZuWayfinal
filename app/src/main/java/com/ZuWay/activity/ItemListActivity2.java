package com.ZuWay.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
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

import com.ZuWay.utils.NotificationONOff;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.ZuWay.EndActivity;
import com.ZuWay.R;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.atapter.ItemGritHomeAdapter;
import com.ZuWay.model.ExpandableHeightGridView;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemListActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Productmodel productmodel = new Productmodel();
    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    ItemGritHomeAdapter itemGritHomeAdapter;
    String scid, cid,pid,key="";
    AutoCompleteTextView auto_search;
    TextView badge_notification;
    View actionView;
    Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private ExpandableHeightGridView item_gv;
    private List<Productmodel> itemGritModelList;
    private FruitAdapter fruitAdapter;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    boolean mFocusDuringOnPause;
    Integer length,pre,nex;
    Button privious,next;
    Double sum;
    ProgressBar progressBar;
    LinearLayout lin_search;
    FrameLayout noitem;
    TextView notifi;
    ToggleButton toggleButton;
    ImageView image_toggle;
    int[] animationList = {R.anim.fade_in, R.anim.fade_in, R.anim.fade_in, R.anim.fade_in};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list2);

        lin_search  =findViewById(R.id.lin_search);

        toolbar();
        bundle();
        init();
        // loadkeyword();
        loadserach();
        if(key.equalsIgnoreCase("")){
            loadproduct();
        }else{
            loadkeyword();
        }


    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scid = extras.getString("scid");
            cid = extras.getString("cid");
            pid = extras.getString("pid");
            key = extras.getString("keyword");

        }

    }

    public void init(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);

        item_gv                       = findViewById(R.id.item_gridviewimage);
        DrawerLayout drawer           = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu         = navigationView.getMenu();
        MenuItem tools    = menu.findItem(R.id.tool_tit);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        notifi = hView.findViewById(R.id.notifi);
        toggleButton=hView.findViewById(R.id.toggleButton);
        image_toggle=hView.findViewById(R.id.image_toggle);
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
        privious=findViewById(R.id.privious);
        next=findViewById(R.id.next);
        noitem = findViewById(R.id.noitem);



    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ItemListActivity2.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(ItemListActivity2.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(ItemListActivity2.this, MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(ItemListActivity2.this, MyWishActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_shipping) {
            Intent intent = new Intent(ItemListActivity2.this, ShippingandRetrun.class);
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            Intent intent = new Intent(ItemListActivity2.this, PrivacyPolicy.class);
            startActivity(intent);

        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(ItemListActivity2.this, TermsandContidion.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(ItemListActivity2.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(ItemListActivity2.this, AboutUs.class);
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
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.alert_yes), new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    logout();

                }
                }).setNegativeButton(getApplicationContext().getResources().getString(R.string.alert_no), new DialogInterface.OnClickListener() {@Override
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout() {

        BSession.getInstance().destroy(ItemListActivity2.this);
        Toast.makeText(ItemListActivity2.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ItemListActivity2.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void loadproduct() {

        final Map<String, String> params = new HashMap<>();
        String userid = BSession.getInstance().getUser_id(getApplicationContext());
        progressDialog.show();
        itemGritModelList = new ArrayList<>();
        String par_str    = "?scid=" + scid;
        String par_str1   = "&user_id=" + userid;
        String baseUrl    = ProductConfig.productlist + par_str + par_str1;
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
                                    model.setPid(jsonObject.getString("pid"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_disprice(jsonObject.getString("discountprize"));
                                    model.setProduct_saveprice(jsonObject.getString("saveprize"));
                                    model.setProduct_ratting(Float.parseFloat(jsonObject.getString("rating")));
                                    model.setProduct_withlistcount(jsonObject.getString("wishlist_count"));
                                    model.setProduct_withlistcount_status(jsonObject.getString("wishlist_add"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    itemGritModelList.add(model);

                                    itemGritHomeAdapter = new ItemGritHomeAdapter(ItemListActivity2.this, R.layout.layout_item_home, itemGritModelList);
                                    item_gv.setAdapter(itemGritHomeAdapter);
                                    item_gv.setNestedScrollingEnabled(false);
                                    item_gv.setExpanded(true);
                                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(ItemListActivity2.this, R.anim.layout_animation);item_gv.setLayoutAnimation(controller);

                                    progressDialog.dismiss();
                                    item_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            Productmodel model1 = itemGritModelList.get(position);
                                            String pid = model1.getPid();
                                            String cid = model1.getCate_id();
                                            String scid = model1.getSubcate_id();
                                            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("pid", pid);
                                            System.out.println("piddl:" + pid);
                                            System.out.println("ciddl:" + cid);
                                            System.out.println("sciddl:" + scid);
                                            ItemActivity.productmodel = model1;
                                            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });
                                } } else {

                                noitem.setVisibility(View.VISIBLE);
                                item_gv.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }progressDialog.dismiss();
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

    private void loadkeyword() {

        final Map<String, String> params = new HashMap<>();
        String userid = BSession.getInstance().getUser_id(getApplicationContext());
        progressDialog.show();
        itemGritModelList = new ArrayList<>();
        String par_str    = "?keywords=" + key;
        String par_str1   = "&user_id=" + userid;
        String baseUrl    = ProductConfig.loadkeyword + par_str +par_str1;
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
                                    model.setPid(jsonObject.getString("pid"));
                                    model.setProduct_name(jsonObject.getString("product_name"));
                                    model.setProduct_oldprice(jsonObject.getString("oldprice"));
                                    model.setProduct_disprice(jsonObject.getString("discountprize"));
                                    model.setProduct_saveprice(jsonObject.getString("saveprize"));
                                    model.setProduct_ratting(Float.parseFloat(jsonObject.getString("rating")));
                                    model.setProduct_withlistcount(jsonObject.getString("wishlist_count"));
                                    model.setProduct_withlistcount_status(jsonObject.getString("wishlist_add"));
                                    model.setProduct_img(jsonObject.getString("web_image"));
                                    itemGritModelList.add(model);

                                    itemGritHomeAdapter = new ItemGritHomeAdapter(ItemListActivity2.this, R.layout.layout_item_home, itemGritModelList);
                                    item_gv.setAdapter(itemGritHomeAdapter);
                                    item_gv.setNestedScrollingEnabled(false);
                                    item_gv.setExpanded(true);

                                    item_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            Productmodel model1 = itemGritModelList.get(position);
                                            String pid = model1.getPid();
                                            String cid = model1.getCate_id();
                                            String scid = model1.getSubcate_id();
                                            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("pid", pid);
                                            System.out.println("piddl:" + pid);
                                            System.out.println("ciddl:" + cid);
                                            System.out.println("sciddl:" + scid);
                                            ItemActivity.productmodel = model1;
                                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });
                                }progressDialog.show();

                            } else {

                                noitem.setVisibility(View.VISIBLE);
                                item_gv.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }progressDialog.dismiss();
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
    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(ItemListActivity2.this, R.anim.layout_animation);item_gv.setLayoutAnimation(controller);
        super.onResume();
    }

    private void toolbar() {

        toolbar     = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        auto_search = toolbar.findViewById(R.id.auto_search);

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
                    key=auto_search.getText().toString().trim();
                    noitem.setVisibility(View.GONE);
                    item_gv.setVisibility(View.VISIBLE);
                    loadkeyword();
                   /* Intent intent=new Intent(ItemListActivity.this,ItemListActivity.class);
                    key=auto_search.getText().toString().trim();
                    intent.putExtra("keyword",key);
                    intent.putExtra("cid",cid);
                    intent.putExtra("scid",scid);
                    intent.putExtra("pid",pid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
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
                        key=auto_search.getText().toString().trim();
                        System.out.println("---test"+"tstttt3");
                        noitem.setVisibility(View.GONE);
                        item_gv.setVisibility(View.VISIBLE);
                        loadkeyword();
                       /* Intent intent=new Intent(ItemListActivity.this,ItemListActivity.class);
                        intent.putExtra("keyword",key);
                        intent.putExtra("cid",cid);
                        intent.putExtra("scid",scid);
                        intent.putExtra("pid",pid);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);*/
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
        actionView              = MenuItemCompat.getActionView(menuItem);
        badge_notification      = (TextView) actionView.findViewById(R.id.badge_notification);

        loadgetcartcount();
        actionView.setOnClickListener(new View.OnClickListener() {@Override
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
                Intent intent = new Intent(getApplicationContext(), MyCartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadgetcartcount() {

        final Map<String, String> params = new HashMap<>();
        String par_str = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        progressDialog.show();
        String baseUrl = ProductConfig.cartcount + par_str;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                badge_notification.setText(jsonResponse.getString("count"));

                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }        progressDialog.dismiss();

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

                            fruitAdapter = new FruitAdapter(ItemListActivity2.this, R.layout.custom_row, spinnerList);
                            auto_search.setAdapter(fruitAdapter);
                            auto_search.setThreshold(1);
                            // progressDialog.dismiss();
                            auto_search.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Productmodel fruit = spinnerList.get(position);
                                            String text = spinnerList.get(position).getName();
                                            auto_search.setText(text);
                                            String textId = spinnerList.get(position).getCate_id();
                                            Productmodel model = spinnerList.get(position);
                                            scid = model.getSubcate_id();
                                            cid = model.getCate_id();
                                            pid = model.getPid();
                                            String keyword = spinnerList.get(position).getKeywords();

                                            Intent intent = new Intent(ItemListActivity2.this, ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", keyword);
                                            auto_search.setText("");
                                            System.out.println(BSession.getInstance().getUser_id(getApplicationContext()));
                                            System.out.println("check"+pid);
                                            System.out.println("check"+cid);
                                            System.out.println("check"+scid);
                                            ItemActivity.productmodel = model;
                                            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    });

                            auto_search.addTextChangedListener(new TextWatcher() {@Override
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
                        //  progressDialog.dismiss();

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


    public void onPause() {
      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");*/
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();
      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        if(mFocusDuringOnPause) {


            //Toast.makeText(ItemListActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
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
            Intent intent=new Intent(ItemListActivity.this,ItemListActivity.class);
            intent.putExtra("cid",cid);
            intent.putExtra("scid",scid);
            intent.putExtra("pid",pid);

            startActivity(intent);*/
        //Toast.makeText(ItemListActivity.this, "off", Toast.LENGTH_SHORT).show();

        // activity was started when screen was off / screen was on with keygaurd displayed

    }
}
