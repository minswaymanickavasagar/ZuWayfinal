package com.ZuWay.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ZuWay.BuildConfig;
import com.ZuWay.utils.NotificationONOff;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;
import com.ZuWay.EndActivity;
import com.ZuWay.R;
import com.ZuWay.atapter.CatrgoryHomeAdapter;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.atapter.TraditionalGritHomeAdapter;
import com.ZuWay.model.ExpandableHeightGridView;
import com.ZuWay.model.HomeTitleGridModel;
import com.ZuWay.model.HomeTitleModel;
import com.ZuWay.model.Productmodel;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.BSessionyou;
import com.ZuWay.utils.ProductConfig;
import com.ZuWay.utils.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    CatrgoryHomeAdapter catrgoryHomeAdapter;
    ImageView  volume;
    Toolbar toolbar;
    String cid;
    ImageView  moti_img;
    CarouselView customCarouselView;
    List<Productmodel> apiSliderList = new ArrayList<>();
    Productmodel sliderModel = new Productmodel();
    YouTubePlayerView youTubePlayerView1,youtube_player_view;
    YouTubePlayerView youtube;


    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);
            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);
            Productmodel model = apiSliderList.get(position);
            String url = model.getBn_img();

            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.banner_gray)
                    .into(fruitImageView);


            return customView;
        }
    };

    String imgurl;
    HomeTitleAdapter homeTitleAdapter;
    View actionView, v1;
    AutoCompleteTextView auto_search;
    TextView badge_notification;
    RelativeLayout badge_layout;
    LinearLayout video_share, image_share, playfullscreen,lin_search;
    boolean showingFirst = true;
    ProgressBar mProgressBar;
    VideoView mVideoView;
    String vurl,notification,notification_type="type",status="";
    FrameLayout placeholder;
    ImageView stop;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    NavigationView navigationView;
    FrameLayout video_ll;
    private RecyclerView caterecyclerView;
    private List<Productmodel> categoryModelList;
    private RecyclerView hometitlercy;
    private List<HomeTitleModel> hometitlemodellist;
    private FruitAdapter fruitAdapter;
    private String YOUTUBE_VIDEO_ID = "uZnWUZW1hQo";
    private String BASE_URL = "https://www.youtube.com";
    private String mYoutubeLink = BASE_URL + "/watch?v=" + YOUTUBE_VIDEO_ID;
    LinearLayout auto_search_lin;
    String currentVersion, latestVersion;
    Dialog dialog;
    LinearLayout lin_mute;
    TextView mute;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 100;
    boolean mFocusDuringOnPause;
    ScrollView scroll;
    //private AlertDialog progressDialog;
    AnimationDrawable loadingViewAnim;
    ImageView imageView1;
    ImageView progressBar;
    ToggleButton toggleButton;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        progressBar=findViewById(R.id.progressBar);

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, HomeActivity.this, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        mAppUpdateManager.registerListener(installStateUpdatedListener);

//version update code


        init();
        auto_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        } );


        getCurrentVersion();
        loadad();
        loadslider();
        toolbar();
        loadserach();
        loadgetcartcount();
        categoryModelList = new ArrayList<>();
        loadcate();
        hometitlemodellist = new ArrayList<>();
        setRecyclerview();

    }

    public  void init(){

        auto_search    = findViewById(R.id.auto_search);
        playfullscreen = findViewById(R.id.playfullscreen);
        video_ll       = findViewById(R.id.video_ll);
        moti_img       = findViewById(R.id.moti_img);
        placeholder    = findViewById(R.id.placeholder);
        video_share    = findViewById(R.id.videoshare);
        image_share    = findViewById(R.id.shareimg);
        stop           = findViewById(R.id.stop1);
        volume         = findViewById(R.id.voluem);

        simpleProgressBar  = (ProgressBar) findViewById(R.id.simpleProgressBar);
        auto_search_lin    = findViewById( R.id.auto_search_lin );
        youtube_player_view= findViewById( R.id.youtube_player_view );

        lin_mute           = findViewById( R.id.lin_mute);
        mute               = findViewById( R.id.mute );


        mVideoView         = (VideoView) findViewById(R.id.my_Video_View);


        customCarouselView = findViewById(R.id.customCarouselView);
        caterecyclerView   = findViewById(R.id.rvCategoryList);
        hometitlercy       = findViewById(R.id.hometitlelist);
        toolbar            = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        auto_search.setThreshold( 1 );

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView      = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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
        toggleButton = hView.findViewById(R.id.toggleButton);
        String statss= NotificationONOff.getInstance().getType(getApplicationContext());

        if(statss.equalsIgnoreCase("")) {
            toggleButton.setChecked(true);
            notification = "1";
            // Toast.makeText(HomeActivity.this, "on", Toast.LENGTH_SHORT).show();
            NotificationONOff.getInstance().initialize(HomeActivity.this,
                    notification_type=notification

            );
        }else if(statss.equalsIgnoreCase("1")){
            toggleButton.setChecked(true);
            notification = "1";
            //Toast.makeText(HomeActivity.this, "on", Toast.LENGTH_SHORT).show();
            NotificationONOff.getInstance().initialize(HomeActivity.this,
                    notification_type=notification

            );
        }else if(statss.equalsIgnoreCase("0")){
            toggleButton.setChecked(false);
            notification = "0";
            // Toast.makeText(HomeActivity.this, "on", Toast.LENGTH_SHORT).show();
            NotificationONOff.getInstance().initialize(HomeActivity.this,
                    notification_type=notification

            );
        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if(isChecked){
                    notification="1";
                    //status="1";
                    // Toast.makeText(HomeActivity.this, "on", Toast.LENGTH_SHORT).show();
                    NotificationONOff.getInstance().initialize(HomeActivity.this,
                            notification_type=notification

                    );
                }else{
                    notification="0";
                    //status="0";
                    // Toast.makeText(HomeActivity.this, "off", Toast.LENGTH_SHORT).show();
                    NotificationONOff.getInstance().initialize(HomeActivity.this,
                            notification_type=  notification
                    );
                }
                // text.setText("Status: " + isChecked);
            }
        });
        String notification= NotificationONOff.getInstance().getType(getApplicationContext());
        System.out.println("---notification"+notification);

        lin_search=findViewById(R.id.lin_search);
        lin_search.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the blinking time with this parameter
                anim.setStartOffset(30);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(3);
                anim.setBackgroundColor(R.color.blue);
                lin_search.startAnimation(anim);
                String edit=auto_search.getText().toString().trim();
                if(edit.isEmpty()&&edit==null){

                }else{

                    Intent intent=new Intent(HomeActivity.this,ItemListActivity.class);
                    intent.putExtra("keyword",auto_search.getText().toString().trim());
                    intent.putExtra("cid","");
                    intent.putExtra("scid","");
                    startActivity(intent);
                }
            }
        });
        auto_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String edit=auto_search.getText().toString().trim();
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {

                    if (edit.isEmpty() && edit == null) {

                    } else {
                        Intent intent = new Intent(HomeActivity.this, ItemListActivity.class);
                        intent.putExtra("keyword", auto_search.getText().toString().trim());
                        intent.putExtra("cid", "");
                        intent.putExtra("scid", "");
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onStop() {
        if (mAppUpdateManager != null)
            mAppUpdateManager.registerListener(installStateUpdatedListener);
        super.onStop();

    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState state) {

            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showCompletedUpdate();
            }

        }
    };

    private void showCompletedUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "New App is Ready", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RC_APP_UPDATE && requestCode != RESULT_OK) {

            Toast.makeText(HomeActivity.this, "CANCEL", Toast.LENGTH_LONG).show();

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    //version update code


    @Override
    public void onRestart() {
        super.onRestart();
        // finish();
        loadgetcartcount();
        loadad();
        // loadserach();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder BackAlertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogTheme));

            BackAlertDialog.setTitle("Activity Exit Alert");
            BackAlertDialog.setMessage("Are you sure want to exit ?");
            BackAlertDialog.setIcon(R.drawable.logo_big);
            BackAlertDialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            BackAlertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mVideoView.stopPlayback();
                    mVideoView.resume();
                    Intent i = new Intent(HomeActivity.this, EndActivity.class);
                    startActivity(i);
                }
            });
            BackAlertDialog.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, MyOrderActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, MyAccount.class);
            startActivity(intent);

        } else if (id == R.id.nav_tools) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, MyWishActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_shipping) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, ShippingandRetrun.class);
            startActivity(intent);

        } else if (id == R.id.nav_privacy) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, PrivacyPolicy.class);
            startActivity(intent);


        } else if (id == R.id.nav_terms) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, TermsandContidion.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, ContactUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            mVideoView.stopPlayback();
            Intent intent = new Intent(HomeActivity.this, AboutUs.class);
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
                                mVideoView.stopPlayback();
                                dialog.cancel();
                                logout();

                            }
                        })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.alert_no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                mVideoView.stopPlayback();
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadad() {

        final Map<String, String> params = new HashMap<>();

        String baseUrl = ProductConfig.motivation;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        ProductConfig.UserDetails = new User(jsonResponse);

                        vurl = jsonResponse.getString("mot_video");
                        Log.d("vul", vurl);
                        BSessionyou.getInstance().initialize(HomeActivity.this,
                                jsonResponse.getString("mot_video"));

                        youtube = findViewById(R.id.youtube);
                        getLifecycle().addObserver(youtube);

                             // Start the MediaController






                        youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {


                                vurl=BSessionyou.getInstance().getMot_video(getApplicationContext());
                                youTubePlayer.loadVideo(vurl, 0);
                                youTubePlayer.pause();


                            }
                        });

/*
                       if (!isFinishing()) {
                            new Handler().post(new Runnable() {
                                public void run() {
                                    YouTubeFragment youfragment = new YouTubeFragment();
                                    FragmentManager manager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame_fragment, youfragment);
                                    fragmentTransaction.commit();
                                }
                            });
                        }*/


                        lin_mute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (showingFirst == true) {
                                    mute.setText( "unmute" );
                                    showingFirst = false;
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                    }
                                    volume.setImageDrawable( getResources().getDrawable( R.drawable.ic_volume_off_black_24dp ) );
                                    mute.setText( "mute" );
                                    showingFirst = true;

                                }
                            }

                        });
                       /* playfullscreen.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View alertLayout = inflater.inflate(R.layout.layout_video, null);

                                youTubePlayerView1 = alertLayout.findViewById(R.id.youtube_player_view1);
                                final LinearLayout linearLayout = alertLayout.findViewById(R.id.videosharee);

                                final ImageView close = alertLayout.findViewById(R.id.close);
                                youTubePlayerView1.setEnableAutomaticInitialization(true);


                                getLifecycle().addObserver(youTubePlayerView1);
                                youTubePlayerView1.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        youTubePlayer.loadVideo(vurl, 0);
                                        youTubePlayer.setVolume(100);
                                     *//*   String videoId = vurl;
                                        youTubePlayer.loadVideo(videoId, 0);*//*
                                    }
                                });

                                linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                        linearLayout.startAnimation(animFadein);
                                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                        whatsappIntent.setType("text/plain");
                                        whatsappIntent.setPackage("com.whatsapp");
                                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + vurl + "\n\n" + "Zuway APP Link : " + "\n" + "https://play.google.com/store/apps/details?id=com.Zuway");
                                        try {
                                            HomeActivity.this.startActivity(whatsappIntent);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                        }

                                    }
                                });

                                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                                alert.setView(alertLayout);
                                final androidx.appcompat.app.AlertDialog dialog = alert.create();


                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Animation animFadein = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
                                        close.startAnimation(animFadein);
                                        dialog.cancel();
                                    }
                                });
                                dialog.getWindow().setLayout(720, 1240);
                                dialog.show();

                            }
                        });
*/


                        video_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                video_share.startAnimation(animFadein);
                                String logo = "http://timemin.co.in/garuda/zuway/assets/img/logo_big.jpg";


                                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                whatsappIntent.setType("text/plain");
                                whatsappIntent.setPackage("com.whatsapp");
                                whatsappIntent.putExtra(Intent.EXTRA_TEXT,  "Zuway APP Link : " + "\n" + "https://play.google.com/store/apps/details?id=com.Zuway");
                                try {
                                    HomeActivity.this.startActivity(whatsappIntent);
                                } catch (android.content.ActivityNotFoundException ex) {
                                }

                            }
                        });

                        imgurl = jsonResponse.getString("mot_image");

                        image_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                                image_share.startAnimation(animFadein);
                                String logo = "http://timemin.co.in/garuda/zuway/assets/img/logo_big.jpg";
                                shareimg(logo);
                            }
                        });


                        Glide.with(getApplicationContext())
                                .load(imgurl)
                                .placeholder(R.drawable.cate_gray)
                                .into(moti_img);


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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }


    @Override
    public void onStart() {
        super.onStart();
        startActivity(getIntent());
    }


    public void shareimg(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_TEXT, "\n\n" + "APP Link :  https://play.google.com/store/apps/details?id=com.Zuway" + "\n" + imgurl);
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


    private void logout() {

        BSession.getInstance().destroy(HomeActivity.this);
        Toast.makeText(HomeActivity.this, getResources().getString(R.string.logout_success), Toast.LENGTH_SHORT).show();
        mVideoView.stopPlayback();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(HomeActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.parse(file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void loadserach() {

        String userid = BSession.getInstance().getUser_id(getApplicationContext());
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
                            System.out.println("productname.."+model.getProduct_name());
                            System.out.println("keyword.."+model.getKeywords());
                            model.setKeywords(jsonObject.getString("keywords"));
                            spinnerList.add(model);

                            fruitAdapter = new FruitAdapter(HomeActivity.this, R.layout.custom_row, spinnerList);
                            auto_search.setAdapter(fruitAdapter);
                            auto_search.setThreshold(1);
                            auto_search.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Productmodel fruit = spinnerList.get(position);
                                            String text = spinnerList.get(position).getName();
                                            auto_search.setText(text);
                                            String textId = spinnerList.get(position).getCate_id();
                                            Productmodel model = spinnerList.get(position);
                                            mVideoView.stopPlayback();
                                            String scid = model.getSubcate_id();
                                            String cid = model.getCate_id();
                                            String pid = model.getPid();
                                            String product_name = model.getProduct_name();
                                            System.out.println("productname,,"+product_name);
                                            String keyword = model.getKeywords();
                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("product_name",product_name);
                                            intent.putExtra("keyword", keyword);
                                            auto_search.setText("");
                                            ItemActivity.productmodel = model;
                                            startActivity(intent);

                                        }
                                    });


                            auto_search.addTextChangedListener(new TextWatcher() {

                                @Override
                                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                    fruitAdapter.getFilter().filter(cs);
                                    //final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.move);auto_search_lin.setLayoutAnimation(controller);
                                   /* Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                                            R.anim.move);
                                    auto_search_lin.startAnimation(animation1);*/
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

        apiSliderList = new ArrayList<>();
        sliderModel = new Productmodel();

        String baseUrl = ProductConfig.banner;
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

                                    model.setBn_img(jsonObject.getString("banner_image"));
                                    model.setBnc_id(jsonObject.getString("cid"));
                                    model.setOffer(jsonObject.getString("offer"));
                                    apiSliderList.add(model);
                                }

                                setUpSlider();
                            } else {
                                //Toast.makeText(getApplicationContext(), "no list items", Toast.LENGTH_SHORT).show();
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
        customCarouselView.setSlideInterval(4000);
        customCarouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                mVideoView.stopPlayback();
                Productmodel model = apiSliderList.get(position);
                cid = apiSliderList.get(position).getBnc_id();
                String offer = apiSliderList.get(position).getOffer();
                Intent intent = new Intent(HomeActivity.this, BannerCategoryActivity.class);
                intent.putExtra("cid", cid);
                intent.putExtra("offer", offer);
                startActivity(intent);

            }
        });
    }

    private void toolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);
        activitytitle.setText("Zuway");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.menu_cart);
        v1 = MenuItemCompat.getActionView(menu.findItem(R.id.menu_cart)).findViewById(R.id.badge_notification);
        actionView = MenuItemCompat.getActionView(menuItem).findViewById(R.id.badge_layout);
        badge_notification = (TextView) v1.findViewById(R.id.badge_notification);
        badge_layout = (RelativeLayout) actionView.findViewById(R.id.badge_layout);

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
                mVideoView.stopPlayback();
                Animation animFadein = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
                badge_layout.startAnimation(animFadein);
                String val="0";
                Intent intent = new Intent(HomeActivity.this, MyCartActivity.class);
                intent.putExtra("scid",val);
                intent.putExtra("cid",val);
                startActivity(intent);
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                mVideoView.stopPlayback();
                Animation animFadein = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
                actionView.startAnimation(animFadein);
                Intent intent = new Intent(HomeActivity.this, MyCartActivity.class);
                intent.putExtra("scid","0");
                intent.putExtra("cid","0");
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void loadcate() {

        final Map<String, String> params = new HashMap<>();

        categoryModelList = new ArrayList<>();

        String baseUrl = ProductConfig.category;
        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {


                                JSONArray jsonResarray = jsonResponse.getJSONArray("storeList");
                                for (int i = 0; i < jsonResarray.length(); i++) {

                                    JSONObject jsonObject = jsonResarray.getJSONObject(i);
                                    final Productmodel model = new Productmodel();
                                    model.setCate_id(jsonObject.getString("cid"));
                                    model.setCate_img(jsonObject.getString("web_image"));
                                    model.setCate_name(jsonObject.getString("web_catname"));

                                    categoryModelList.add(model);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    caterecyclerView.setLayoutManager(layoutManager);
                                    catrgoryHomeAdapter = new CatrgoryHomeAdapter(getApplicationContext(), categoryModelList);
                                    caterecyclerView.setAdapter(catrgoryHomeAdapter);
                                    caterecyclerView.setHasFixedSize(true);
                                    final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.layout_animation);caterecyclerView.setLayoutAnimation(controller);

                                    RecyclerView.ItemDecoration itemDecoration =
                                            new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.VERTICAL);
                                    caterecyclerView.addItemDecoration(itemDecoration);
                                    caterecyclerView.setNestedScrollingEnabled(false);
                                    caterecyclerView.addOnItemTouchListener(
                                            new RecyclerItemClickListener(getApplicationContext(), caterecyclerView, new RecyclerItemClickListener.ClickListener() {

                                                @Override
                                                public void onItemClick(View view, final int position) {
                                                    mVideoView.stopPlayback();
                                                    Productmodel model = categoryModelList.get(position);

                                                    String id     = categoryModelList.get(position).getCate_id();
                                                    Intent intent = new Intent(getApplicationContext(), SubCategoryActivity.class);
                                                    intent.putExtra("cid", id);
                                                    startActivity(intent);

                                                }

                                                @Override
                                                public void onLongItemClick(View view, int position) {

                                                }
                                            })
                                    );

                                }

                            } else {
                                // Toast.makeText(getApplicationContext(), "no list items", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }*/
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

        hometitlemodellist = new ArrayList<>();
        final Map<String, String> params = new HashMap<>();
        String baseUrl = ProductConfig.hometitle;
       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
            if (progressDialog != null) {

                progressDialog.show();

            }
        }*/
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                        hometitlemodellist = new ArrayList<>();
                        JSONArray array = jsonResponse.getJSONArray("storeList");


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);

                            HomeTitleModel homeTitleModel = new HomeTitleModel();
                            homeTitleModel.setHome_title(array.getJSONObject(i).getString("cat_title"));
                            homeTitleModel.setHome_title_id(array.getJSONObject(i).getString("title_cid"));


                            JSONArray gritArr = product.getJSONArray("list");
                            List<HomeTitleGridModel> homegirtModelList = new ArrayList<>();
                            for (int j = 0; j < gritArr.length(); j++) {
                                JSONObject weight_result = gritArr.getJSONObject(j);
                                HomeTitleGridModel homeTitleGridModel = new HomeTitleGridModel();
                                homeTitleGridModel.setCid(gritArr.getJSONObject(j).getString("cid"));
                                homeTitleGridModel.setScid(gritArr.getJSONObject(j).getString("scid"));
                                homeTitleGridModel.setWeb_title(gritArr.getJSONObject(j).getString("web_title"));
                                homeTitleGridModel.setWeb_image(gritArr.getJSONObject(j).getString("web_image"));
                                homegirtModelList.add(homeTitleGridModel);
                            }
                            homeTitleModel.setHomegrid_title(homegirtModelList);

                            hometitlemodellist.add(homeTitleModel);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                        hometitlercy.setLayoutManager(layoutManager);
                        hometitlercy.setNestedScrollingEnabled(false);
                        homeTitleAdapter = new HomeTitleAdapter(HomeActivity.this, hometitlemodellist);
                        hometitlercy.setAdapter(homeTitleAdapter);
                        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.layout_animation);hometitlercy.setLayoutAnimation(controller);


                    } else {
                        // Toast.makeText(getApplicationContext(), "No Product Result Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  if (!HomeActivity.this.isFinishing() && progressDialog != null) {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
               /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }*/
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

    private void loadgetcartcount() {

        final Map<String, String> params = new HashMap<>();
       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
            if (progressDialog != null) {

                progressDialog.show();

            }
        }*/
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

                                String count = jsonResponse.getString("count");
                                badge_notification.setText(count);


                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {

                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                       /* if (!HomeActivity.this.isFinishing() && progressDialog != null) {
                            if (progressDialog != null) {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }*/
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

    public class HomeTitleAdapter extends RecyclerView.Adapter<HomeTitleAdapter.MailViewHolder> {

        List<HomeTitleModel> HomeTitleModelList;
        String  scid, cid;
        String title;
        TraditionalGritHomeAdapter traditionalGritHomeAdapter;
        private Context mContext;


        public HomeTitleAdapter(Context mContext, List<HomeTitleModel> HomeTitleModelList) {
            this.mContext = mContext;
            this.HomeTitleModelList = HomeTitleModelList;
        }

        @NonNull
        @Override
        public HomeTitleAdapter.MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_title_home, parent, false);
            return new HomeTitleAdapter.MailViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(@NonNull final MailViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            final HomeTitleModel model = HomeTitleModelList.get(position);
            title = HomeTitleModelList.get(position).getHome_title();
            cid = HomeTitleModelList.get(position).getHome_title_id();

            if (model.getHome_title().equalsIgnoreCase("Today's Offer")) {
                holder.tvviewmore.setVisibility(View.GONE);
            }

            holder.tvMenuTitle.setText(HomeTitleModelList.get(position).getHome_title());

            final List<Productmodel> homelist = new ArrayList<>();
            final List<HomeTitleGridModel> homegritModelList = HomeTitleModelList.get(position).getHomegrid_title();
            for (HomeTitleGridModel homeTitleGridModel : homegritModelList) {

                Productmodel categoryModel = new Productmodel();


                categoryModel.setCate_name(homeTitleGridModel.getWeb_title());
                categoryModel.setCate_img(homeTitleGridModel.getWeb_image());
                categoryModel.setCate_id(homeTitleGridModel.getCid());
                categoryModel.setSubcate_id(homeTitleGridModel.getScid());

                homelist.add(categoryModel);
            }

            traditionalGritHomeAdapter = new TraditionalGritHomeAdapter(mContext, R.layout.layout_hotdeals_home, homelist);
            holder.hometitlegirt.setAdapter(traditionalGritHomeAdapter);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.hometitlegirt.setNestedScrollingEnabled(false);
            }
            holder.hometitlegirt.setExpanded(true);
            holder.hometitlegirt.setNestedScrollingEnabled(false);
            holder.hometitlegirt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mVideoView.stopPlayback();

                    Productmodel model = homelist.get(position);
                    scid = model.getSubcate_id();
                    cid = model.getCate_id();
                    ItemListActivity.productmodel = model;
                    Intent intent = new Intent(mContext, ItemListActivity.class);
                    intent.putExtra("scid", scid);
                    intent.putExtra("cid", cid);
                    intent.putExtra("keyword", "");
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);


                }
            });

            holder.tvviewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVideoView.stopPlayback();
                    Animation animFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
                    holder.tvviewmore.startAnimation(animFadein);
                    HomeTitleModel mode = HomeTitleModelList.get(position);
                    // scid = mode.getHome_title_id();
                    cid = mode.getHome_title_id();
                    Intent intent = new Intent(mContext, SubCategoryActivity.class);
                    intent.putExtra("cid", cid);
                    mContext.startActivity(intent);

                }
            });




        }

        @Override
        public int getItemCount() {
            return HomeTitleModelList.size();

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
    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetLatestVersion().execute();

    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en").get();
                latestVersion = doc.getElementsByClass("htlgb").get(6).text();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (latestVersion != null) {
                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    if (!isFinishing()) { //This would help to prevent Error : BinderProxy@45d459c0 is not valid; is your activity running? error
                        showUpdateDialog();
                    }
                }
            } else {
            }

            super.onPostExecute(jsonObject);
        }
    }

    private void showUpdateDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setIcon(R.drawable.logo_200);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.ZuWay")));
                dialog.dismiss();
            }
        });


        builder.setCancelable(false);
        dialog = builder.show();
    }

    @Override
    protected void onResume() {

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.layout_animation);caterecyclerView.setLayoutAnimation(controller);
        final LayoutAnimationController controller1 = AnimationUtils.loadLayoutAnimation(HomeActivity.this, R.anim.layout_animation);hometitlercy.setLayoutAnimation(controller1);
        super.onResume();
    }

    /*  protected void onPause() {
        super.onPause();
         Toast.makeText(HomeActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        mFocusDuringOnPause = hasWindowFocus();
    }*/

   /* public void onStop() {
        super.onStop();

        if(mFocusDuringOnPause) {

            Intent intent=new Intent(HomeActivity.this,BannerCategoryActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(HomeActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }
    }*/


}
