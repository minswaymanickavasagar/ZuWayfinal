package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ZuWay.utils.PlaceOrderSession;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ZuWay.R;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContactUs extends AppCompatActivity {
    EditText et_name;
    EditText et_email;
    EditText et_phone;
    EditText et_message;
    TextView btnsumit,web,mail,add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        toolbar();
        init();
        feedback();



        btnsumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                btnsumit.startAnimation(animFadein);

                String name   = et_name.getText().toString();
                String mobile = et_phone.getText().toString();
                String mes    = et_message.getText().toString();


                if (name.isEmpty()) {
                    et_name.setError("Kindly enter your name");
                    et_name.requestFocus();
                    return;
                }

                if (mobile.isEmpty()) {
                    et_phone.setError("Kindly enter your mobile");
                    et_phone.requestFocus();
                    return;
                }
                if (mobile.length() < 10) {
                    et_phone.setError("Kindly enter Valid MobileNo");
                    et_phone.requestFocus();
                    return;
                }

                if (mes == null) {
                    et_message.setError("Kindly enter your address");
                    et_message.requestFocus();
                    return;
                }


                if (name != null && name != ""  && mobile != null && mobile != "" &&
                        mes != null) {


                    submitfeedback();


                }
            }
        });

    }

    public void init(){

        et_email   = findViewById(R.id.et_email);
        et_message = findViewById(R.id.et_message);
        et_name    = findViewById(R.id.et_name);
        et_phone   = findViewById(R.id.et_phone);
        btnsumit   = findViewById(R.id.help_submit);
        web        = findViewById(R.id.web);
        mail       = findViewById(R.id.email);
        add        = findViewById(R.id.add);

        final String namee    = PlaceOrderSession.getInstance().getUser_name(getApplicationContext());
        final String emaill   = PlaceOrderSession.getInstance().getUser_email(getApplicationContext());
        final String mobilee  = PlaceOrderSession.getInstance().getUser_mobile(getApplicationContext());

        et_email.setText(emaill);
        et_name.setText(namee);
        et_phone.setText(mobilee);

    }

    private void submitfeedback() {
        String name = et_name.getText().toString();
        String mobile = et_phone.getText().toString();
        String email = et_email.getText().toString();
        String mess = et_message.getText().toString();


        final String userid = BSession.getInstance().getUser_id(getApplicationContext());
        final Map<String, String> params = new HashMap<>();


        params.put("customerid", userid);
        params.put("name", name);
        params.put("mobile", mobile);
        params.put("email", email);
        params.put("message", mess);
        String baseUrl = ProductConfig.feedback;

        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);
                        Toast.makeText(getApplicationContext(), "Feedback Submit successfully ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Feedback Submit Failed.. ", Toast.LENGTH_SHORT).show();


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
                // progressDialog.dismiss();

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

    private void feedback() {


        final Map<String, String> params = new HashMap<>();

        String baseUrl = ProductConfig.contactdetails;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    //  progressDialog.dismiss();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("result") && jsonResponse.getString("result").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);

                        mail.setText(jsonResponse.getString("email"));
                        add.setText(jsonResponse.getString("address"));
                        web.setText(jsonResponse.getString("website"));
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
                // progressDialog.dismiss();

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
        activitytitle.setText("Contact Us");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
