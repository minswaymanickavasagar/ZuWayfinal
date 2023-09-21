package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.mukesh.OtpView;
import com.ZuWay.R;
import com.ZuWay.model.User;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ForgotPassword extends AppCompatActivity {

    private EditText edt_pbone;
    private Button btn_sendOtp, btn_verify;
    LinearLayout ll_forgot_otp, ll_forgot_pass;
    OtpView otp;
    TextView txt_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();


        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
               btn_sendOtp.startAnimation(animFadein);

                String mobile = edt_pbone.getText().toString().trim();
                txt_mobile.setText("Enter SMS verification code sent to "+mobile);

                if (mobile.isEmpty()) {
                    edt_pbone.setError("Kindly enter your MobileNo");
                    edt_pbone.requestFocus();
                    return;
                }
                if (mobile.length() < 10) {
                    edt_pbone.setError("Kindly enter Valid MobileNo");
                    edt_pbone.requestFocus();
                    return;
                }
                if (mobile != null && mobile != "" && mobile.length() >= 10){

                    otpget();

                }else {
                    Toast.makeText(ForgotPassword.this, "Enter Your Register Mobile Number", Toast.LENGTH_SHORT).show();
                }




            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                btn_verify.startAnimation(animFadein);

                String otptx = otp.getText().toString().trim();

                if (otptx.isEmpty()) {
                    otp.setError("*Enter The MobileNo");
                    otp.requestFocus();
                    return;
                }
                if (!otptx.isEmpty()) {

                  otpsubmit();
                } else {

                    Toast.makeText(ForgotPassword.this, "Wrong Details Enter Correct Details", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public  void init(){
        edt_pbone      = findViewById(R.id.edt_pbone);
        btn_sendOtp    = findViewById(R.id.btn_sendOtp);
        otp            = findViewById(R.id.otp_view);
        btn_verify     = findViewById(R.id.btn_verify);
        ll_forgot_otp  = findViewById(R.id.ll_forgot_otp);
        ll_forgot_pass = findViewById(R.id.ll_forgot_pass);
        txt_mobile     = findViewById(R.id.txt_mobile);
    }


    private void otpget() {

        final Map<String, String> params = new HashMap<>();

        String mobile  = edt_pbone.getText().toString();

        String par_str ="?user_mobile="+mobile;
        String baseUrl = ProductConfig.forgotpassword+par_str;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("Success") && jsonResponse.getString("Success").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);

                        ll_forgot_otp.setVisibility(View.VISIBLE);
                        ll_forgot_pass.setVisibility(View.GONE);


                        Toast.makeText(getApplicationContext(),
                                "Otp Send successful", Toast.LENGTH_SHORT)
                                .show();



                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter Register Mobile Number", Toast.LENGTH_SHORT)
                                .show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        requestQueue.add(jsObjRequest);
    }


    private void otpsubmit() {

        final User user = new User();
        user.setAction("forgototpurl");

        final Map<String, String> params = new HashMap<>();
        String otptx = otp.getText().toString();
        final String mobile = edt_pbone.getText().toString();

        String par_str ="?forotp="+otptx;

        String baseUrl = ProductConfig.forgototpverify+par_str;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("Success") && jsonResponse.getString("Success").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);
                        Intent intent = new Intent(ForgotPassword.this, FrogotPasswordChangeActivity.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),
                                "Verification Success", Toast.LENGTH_SHORT)
                                .show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Verification failed! Check your OTP", Toast.LENGTH_SHORT)
                                .show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        requestQueue.add(jsObjRequest);


    }



}
