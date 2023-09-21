package com.ZuWay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements LocationListener {
    TextView  getlocation;
    EditText txtname, txtMobile, txtaddress, txtPwd, txtemail;
    Button signin;
    LocationManager locationManager;
    String email,phone;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading.....");
        init();

        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                getlocation.startAnimation(animFadein);
                progressDialog.show();
                getLocation();
            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                signin.startAnimation(animFadein);

                String name = txtname.getText().toString();
                String mobile = txtMobile.getText().toString();
                String email = txtemail.getText().toString();
                String add = txtaddress.getText().toString();

                if (name.isEmpty()) {
                    txtname.setError("Kindly enter your name");
                    txtname.requestFocus();
                    return;
                }


                if (mobile.length() < 10) {
                    txtMobile.setError("Kindly enter Valid MobileNo");
                    txtMobile.requestFocus();
                    return;
                }

                if (add.isEmpty()) {
                    txtaddress.setError("Kindly enter your address");
                    txtaddress.requestFocus();
                    return;
                }
                if (mobile != null && mobile != "" && name != null && name != "" &&
                         add != null && add != "") {

                    RegisterButton();
                } else {
                    Toast.makeText(RegisterActivity.this, "Please enter your details!", Toast.LENGTH_SHORT).show();

                }



            }
        });



    }

    public void init(){

        phone=BSession.getInstance().getUser_mobile(getApplicationContext());
        txtMobile         = findViewById(R.id.txtMobile);
        txtMobile.setText( phone );
        txtemail          = findViewById(R.id.txtEmail);
        txtaddress        = findViewById(R.id.txtaddress);
        txtname           = findViewById(R.id.txtname);
        getlocation       = findViewById(R.id.tv_currentlocation);
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        signin            = findViewById(R.id.update_btn);
    }

    void getLocation() {
        progressDialog.show();
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 8000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        progressDialog.show();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            txtaddress.setText(addresses.get(0).getAddressLine(0));
            progressDialog.dismiss();
            getlocation.setVisibility(View.GONE);
        } catch (Exception e) {


        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(RegisterActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private void RegisterButton() {

        final User user = new User();
        user.setAction("register");

        String name = txtname.getText().toString();
        final String mobile = txtMobile.getText().toString();

        if (txtemail == null) {
            email = "null";
        } else {
            email = txtemail.getText().toString();
        }

        final String address = txtaddress.getText().toString();

        final Map<String, String> params = new HashMap<>();

        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);

        params.put("user_address", address);

        String para_str = "?user_mobile=" + mobile;
        String para_str1 = "&user_name=" + name;
        String para_str2 = "&user_email=" + email;
        String para_str4 = "&user_address=" + address;

        progressDialog.show();

        String baseUrl = ProductConfig.Registerurl + para_str + para_str1 + para_str2 +  para_str4;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    progressDialog.show();
                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);
                        progressDialog.dismiss();

                        BSession.getInstance().initialize(RegisterActivity.this,
                                jsonResponse.getString("customerid"),
                                jsonResponse.getString("user_name"),
                                jsonResponse.getString("user_mobile"),
                                jsonResponse.getString("user_email"),
                                jsonResponse.getString("user_address"),
                                "","",""
                        );
                        Toast.makeText(RegisterActivity.this, "Welcome!,OTP Send Your Mobile Number", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                        //finish();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed..!Mobile number already exists ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(jsObjRequest);
    }

    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

       // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {

            Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(RegisterActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }


}
