package com.ZuWay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.ZuWay.R;
import com.ZuWay.atapter.FruitAdapter;
import com.ZuWay.model.Productmodel;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainSearchActivity extends AppCompatActivity {

    public ArrayList<Productmodel> spinnerList = new ArrayList<>();
    AutoCompleteTextView auto_search;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        auto_search    = findViewById(R.id.auto_search);

        auto_search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        } );

        loadserach();
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

                            final FruitAdapter   fruitAdapter = new FruitAdapter(MainSearchActivity.this, R.layout.custom_row, spinnerList);
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
                                            String product_name = spinnerList.get(position).getProduct_name();

                                            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                            intent.putExtra("scid", scid);
                                            intent.putExtra("cid", cid);
                                            intent.putExtra("pid", pid);
                                            intent.putExtra("keyword", keyword);
                                            intent.putExtra("product_name", product_name);

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
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

       // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

       /* if(mFocusDuringOnPause) {


            Intent intent=new Intent(MainSearchActivity.this,MainSearchActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MainSearchActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}