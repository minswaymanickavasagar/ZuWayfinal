package com.ZuWay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;
import com.ZuWay.activity.HomeActivity;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class StartActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    CircleIndicator indicator;
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    Button start;
    CarouselView customCarouselView;
    boolean showingFirst = true;
    List<Productmodel> apiSliderList = new ArrayList<>();
    Productmodel sliderModel = new Productmodel();
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        customCarouselView = findViewById(R.id.customCarouselView);
        loadslider();
        start = findViewById(R.id.start);
        start.setEnabled(true);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }

    private void loadslider() {

        final Map<String, String> params = new HashMap<>();

        apiSliderList = new ArrayList<>();
        sliderModel = new Productmodel();

        String baseUrl = ProductConfig.bannerstart;
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
        customCarouselView.setSlideInterval(4000);
        customCarouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_custom, null);


            ImageView fruitImageView = (ImageView) customView.findViewById(R.id.fruitImageView);


            Productmodel model = apiSliderList.get(position);
            String url = model.getBn_img();
            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.start_gray)
                    .into(fruitImageView);

            return customView;
        }
    };
    /*public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
        mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

        if(mFocusDuringOnPause) {


            Intent intent=new Intent(StartActivity.this,StartActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(StartActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }
    }*/
}
