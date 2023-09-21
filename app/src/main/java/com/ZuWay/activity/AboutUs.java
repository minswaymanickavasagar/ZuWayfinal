package com.ZuWay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ZuWay.R;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AboutUs extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog progressDialog;
    TextView webView;
    TextView aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        webView = findViewById(R.id.webView_about);
        aboutus = findViewById(R.id.aboutus);

        toolbar();
        Aboutus();

    }

    private void Aboutus() {


        final Map<String, String> params = new HashMap<>();

        String baseUrl = ProductConfig.aboutus;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {
                        aboutus.setText(Html.fromHtml(String.valueOf(Html.fromHtml(jsonResponse.getString("web_title")))));
                        webView.setText(Html.fromHtml(String.valueOf(Html.fromHtml(jsonResponse.getString("web_content")))));

                    }
                    else {
                        Toast.makeText(AboutUs.this, "About us content empty", Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AboutUs.this);
        requestQueue.add(jsObjRequest);


    }

    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               onBackPressed();
        }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);

        activitytitle.setText("About Us");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
