package com.ZuWay.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ZuWay.R;
import com.ZuWay.StartActivity;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewLoginActivity extends AppCompatActivity {

    EditText et_phone;
    Button signin;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    String Status;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");

        Status = BSession.getInstance().getStatus(NewLoginActivity.this);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        try {
           String FcmToken = FirebaseInstanceId.getInstance().getToken();
            System.out.println("----"+FcmToken);

        } catch (Exception ex) {
            String FcmToken = "";
            System.out.println("----"+FcmToken);
        }

       if (BSession.getInstance().isApplicationExit(NewLoginActivity.this)) {
           Intent intentc = new Intent(NewLoginActivity.this, HomeActivity.class);
           startActivity(intentc);
           finish();
       }else {
           init();
       }

        NewtworkConnection();


    }

    public void init() {
        signin = findViewById(R.id.btn_login);
        et_phone = findViewById(R.id.txt_mobile);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                signin.startAnimation(animFadein);


                String mobile = et_phone.getText().toString();

                Pattern p = Pattern.compile("[5-9][0-9]{9}");
                Matcher match = p.matcher(mobile);
                System.out.println("mobilenumberformet"+p);
                if ((match.find())) {

                    loginApiCall();
                    System.out.println("It is a valid mobile number.");

                }
                else{
                    Toast.makeText(getApplicationContext(), "Not a valid mobile number", Toast.LENGTH_LONG).show();


                }
            }
        });


    }

    private void loginApiCall() {

        final String mobile  = et_phone.getText().toString();

        final Map<String, String> params = new HashMap<>();
        progressDialog.show();
        params.put("user_mobile", mobile);
        String par_str = "?user_mobile=" + mobile;
        String baseUrl = ProductConfig.newlogin + par_str;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {

                        /*ProductConfig.UserDetails = new User(jsonResponse);
                        BSession.getInstance().initialize(NewLoginActivity.this,
                                "",
                                "",
                                jsonResponse.getString("user_mobile"),
                                "",
                                "", "", "", ""

                        );*/
                        Intent intent = new Intent(NewLoginActivity.this, StartActivity.class);
                        intent.putExtra("user_mobile",mobile);
                        startActivity(intent);
                       // finish();
                    } else if (jsonResponse.has("success") && jsonResponse.getString("success").equals("0")) {

                        ProductConfig.UserDetails = new User(jsonResponse);
                       BSession.getInstance().initialize(NewLoginActivity.this,
                                ""
                                ,
                                "",
                                jsonResponse.getString("user_mobile"),
                                "",
                                "", jsonResponse.getString("status"), "", ""

                        );
                        Intent intent = new Intent(NewLoginActivity.this, StartActivity.class);
                        intent.putExtra("user_mobile",mobile);
                        startActivity(intent);
                        //finish();
                    } else {
                        Toast.makeText(NewLoginActivity.this,
                                "Please enter valid credentials!", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsObjRequest);
    }

    private void NewtworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            if (!info.isConnected()) {
                Toast.makeText(this, "Please check your Network connection and try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please check your wireless connection and try again.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

       // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

       /* if(mFocusDuringOnPause) {

            Intent intent=new Intent(LoginActivity.this,LoginActivity.class);

            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(LoginActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
   /* @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }*/
}
