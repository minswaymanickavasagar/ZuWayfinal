package com.ZuWay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ZuWay.R;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ZoomActivity extends AppCompatActivity {
    ImageViewTouch fruitImageView;
    List<Productmodel> apiSliderList = new ArrayList<>();
    Productmodel sliderModel = new Productmodel();
    ProgressDialog progressDialog;
    CarouselView customCarouselView;
    String scid,pid;

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.view_zoom, null);


            ImageViewTouch fruitImageView = (ImageViewTouch) customView.findViewById(R.id.fruitImageView);


            Productmodel model = apiSliderList.get(position);
            String url = model.getProduct_listimg();
            Glide.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.logo_big)
                    .into(fruitImageView);

            return customView;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        progressDialog     = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        fruitImageView=findViewById(R.id.fruitImageView);
        customCarouselView = findViewById(R.id.customCarouselView);
        bundle();
        loadslider();
    }
    public  void bundle(){

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            scid = extras.getString("scid");
            pid = extras.getString("pid");


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

}