package com.ZuWay.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

public class TermsandContidion extends AppCompatActivity {
    TextView terms_title,webView_tm;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_termsand_contidion );
        terms_title=findViewById( R.id.terms_title );
        webView_tm=findViewById( R.id.webView_tm );


        toolbar();
        Termsconditions();


    }

    private void Termsconditions() {

        final Map<String, String> params = new HashMap<>();

        String baseUrl = ProductConfig.termscondition;

        StringRequest jsObjRequest = new StringRequest( Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.e( "Response", response.toString() );
                try {

                    JSONObject jsonResponse = new JSONObject( response );

                    if (jsonResponse.has( "result" ) && jsonResponse.getString( "result" ).equals( "Success" )) {
                        terms_title.setText( Html.fromHtml(String.valueOf(Html.fromHtml(jsonResponse.getString( "web_title" ) ))));
                        webView_tm.setText(Html.fromHtml((jsonResponse.getString("web_content" )), Html.FROM_HTML_MODE_LEGACY));


                    } else {
                        Toast.makeText( TermsandContidion.this, "About us content empty", Toast.LENGTH_LONG ).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e( "Error", error.toString() );


            }
        } ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue( TermsandContidion.this );
        requestQueue.add( jsObjRequest );


    }



    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

               onBackPressed();
            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);

        activitytitle.setText("Terms & Conditions");


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
        //mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {


            Intent intent=new Intent(TermsandContidion.this,TermsandContidion.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(TermsandContidion.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
    }

