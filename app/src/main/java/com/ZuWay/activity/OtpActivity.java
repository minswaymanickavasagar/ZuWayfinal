package com.ZuWay.activity;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.ZuWay.R;
import com.ZuWay.StartActivity;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OtpActivity extends AppCompatActivity  implements OnOtpCompletionListener {
    private OtpView otpView;
    String otpStr="",mobile,FcmToken;
    TextView txt_mobile;
    Button signin;
    Boolean sendOTP = false;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
        try {
            FcmToken = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception ex) {
            FcmToken = "";
        }
        init();


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                signin.startAnimation(animFadein);
                String otp=otpView.getText().toString().trim();

                if (otp.isEmpty()) {
                    otpView.setError("Kindly enter otp");
                    otpView.requestFocus();
                    return;
                }

                if (!otp.isEmpty() ) {
                       checkOTP();

                } else {

                    Toast.makeText(OtpActivity.this, "Wrong Otp Kindly enter Correct Otp", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void init(){
        mobile=BSession.getInstance().getUser_mobile(getApplicationContext());
        otpView = findViewById(R.id.otp_view);
        signin=findViewById(R.id.btn_validate);
        txt_mobile=findViewById( R.id.txt_mobile );

        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        txt_mobile.setText( "Enter SMS verification code sent " + mobile );

    }


    @Override
    public void onOtpCompleted(String otp) {
        otpStr = String.valueOf(otpView.getText());
        if (sendOTP) {
        } else {
        }
    }
    private void checkOTP() {
        final Map<String, String> params = new HashMap<>();
        String otp=otpView.getText().toString().trim();

        params.put("otp", otp);

        String par_str="?user_mobile="+mobile;
        String par_str1="&otp="+otp;
        String par_str2="&note_id="+FcmToken;

        progressDialog.show();
        String baseUrl = ProductConfig.otpurl+par_str+par_str1+par_str2;
        StringRequest jsObjRequest = new StringRequest(Request.Method.GET , baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        BSession.getInstance().initialize(OtpActivity.this,
                                jsonResponse.getString("user_id"),
                                "",
                                jsonResponse.getString("user_mobile"),
                                "",
                                "",
                                "","",""
                        );
                        Intent i = new Intent( OtpActivity.this, HomeActivity.class );
                        startActivity( i );
                        //finish();
                        //   }

                    }
                  /*  else if (jsonResponse.has("status") && jsonResponse.getString("status").equals("0")){
                        BSession.getInstance().initialize(OtpActivity.this,
                                jsonResponse.getString("user_id"),
                                "",
                                jsonResponse.getString("user_mobile"),
                                "",
                                "",
                                "","",""
                        );
                        Intent i = new Intent( OtpActivity.this, StartActivity.class );
                        startActivity( i );
                        finish();
                    }*/
                    else {
                        Toast.makeText( getApplicationContext(), "please check your Otp", Toast.LENGTH_LONG ).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace(); }progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Error", error.toString());
                //  progressDialog.dismiss();
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
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

      //  mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {

            Intent intent=new Intent(OtpActivity.this,OtpActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(OtpActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
    /*@Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }*/

}

