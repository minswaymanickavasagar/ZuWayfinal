package com.ZuWay.activity;

import android.app.Activity;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ZuWay.utils.PlaceOrderSession;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.ZuWay.R;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class


OrderPlacedActivity extends AppCompatActivity implements PaymentResultListener, LocationListener {
    private static final String TAG = OrderPlacedActivity.class.getSimpleName();

    TextView symbol_tv_shippingcharge, no_item, subtotal, deliverycharge, grandtotal, getLocat;
    EditText name, mobile, address, email;
    Button pay;
    String  cid,scid,st, dc, gt, orderid, pc,selectmethod = "COD", paidtype = "Unpaid";
    RadioGroup radioGroup;
    RadioButton radio1, radio2;
    boolean doubleBackToExitPressedOnce = false;
    LocationManager locationManager;
    ProgressBar simpleProgressBar;
    double totalg;
    ProgressDialog progressDialog;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        progressDialog = new ProgressDialog(OrderPlacedActivity.this);
        progressDialog.setMessage("Loading.....");

        Checkout.preload(getApplicationContext());
        bundle();
        init();

        getLocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(OrderPlacedActivity.this, R.anim.fade_in);
                getLocat.startAnimation(animFadein);
                getLocation();
            }
        });

        if (ContextCompat.checkSelfPermission(OrderPlacedActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderPlacedActivity.this
                , android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(OrderPlacedActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                boolean isChecked = radio1.isChecked();
                boolean isChecked1 = radio2.isChecked();

                if (isChecked) {
                    selectmethod = "COD";


                } else if (isChecked1) {
                    selectmethod = "ONLINE";

                }
            }
        });


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                pay.startAnimation(animFadein);

                String na = name.getText().toString();
                String mp = mobile.getText().toString();
                String em = email.getText().toString();
                String add = address.getText().toString();


                if (na.isEmpty()) {
                    name.setError("Kindly enter your name");
                    name.requestFocus();
                    return;
                }

                if (mp.isEmpty()) {
                    mobile.setError("Kindly enter your MobileNo");
                    mobile.requestFocus();
                    return;
                }
                if (mp.length() < 10) {
                    mobile.setError("Kindly enter Valid MobileNo");
                    mobile.requestFocus();
                    return;
                }

                if (add.isEmpty()) {
                    address.setError("Kindly enter your Address");
                    address.requestFocus();
                    return;
                }
                if (mp != null && mp != "" && na != null && na != "" &&
                        em != null && em != "" && mp.length() >= 10 && add != null && add != "") {

                    if (selectmethod != null) {
                        if (selectmethod.equalsIgnoreCase("ONLINE")) {
                            startPayment();
                        } else if (selectmethod.equalsIgnoreCase("COD")) {
                            loadplaceorder();
                            paidtype = "Unpaid";
                        }

                    } else {
                        Toast.makeText(OrderPlacedActivity.this, "Select your Payment Method", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            st = extras.getString("total");
            dc = extras.getString("deliveryamount");
            gt = extras.getString("grandtotal");
            orderid = extras.getString("order_id");
            pc = extras.getString("product_count");
            scid = extras.getString("scid");
            cid = extras.getString("cid");

        }
    }

    public void init(){
        simpleProgressBar       = (ProgressBar) findViewById(R.id.simpleProgressBar);
        no_item                 = findViewById(R.id.pro_num);
        subtotal                = findViewById(R.id.tv_subtotal);
        deliverycharge          = findViewById(R.id.tv_shippingcharge);
        grandtotal              = findViewById(R.id.tv_billtotal);
        name                    = findViewById(R.id.et_name);
        mobile                  = findViewById(R.id.et_phone);
        address                 = findViewById(R.id.et_add);
        email                   = findViewById(R.id.et_email);
        pay                     = findViewById(R.id.confirm_btn);
        radioGroup              = findViewById(R.id.radiogroup1);
        radio1                  = findViewById(R.id.radio1);
        radio2                  = findViewById(R.id.radio2);
        symbol_tv_shippingcharge=findViewById( R.id.symbol_tv_shippingcharge );

        no_item.setText(pc);
        subtotal.setText("₹"+" "+st);
        grandtotal.setText("₹"+" "+gt);
        if(dc.equalsIgnoreCase( "Free Shipping" )){
            deliverycharge.setText(dc);
            symbol_tv_shippingcharge.setVisibility( View.GONE );
        }else {
            deliverycharge.setText(dc);

        }

        totalg = Double.parseDouble(gt);

        final String namee = PlaceOrderSession.getInstance().getUser_name(getApplicationContext());
        final String emaill = PlaceOrderSession.getInstance().getUser_email(getApplicationContext());
        final String mobilee = BSession.getInstance().getUser_mobile(getApplicationContext());
        final String addresss = PlaceOrderSession.getInstance().getAddress(getApplicationContext());

        name.setText(namee);
        email.setText(emaill);
        mobile.setText(mobilee);
        address.setText(addresss);

        getLocat = findViewById(R.id.getlocation);
    }


    void getLocation() {
        try {
            progressDialog.show();
            locationManager = (LocationManager) OrderPlacedActivity.this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 8000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        progressDialog.show();
        try {
            Geocoder geocoder = new Geocoder(OrderPlacedActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            address.setText(addresses.get(0).getAddressLine(0));
            progressDialog.dismiss();

        } catch (Exception e) {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(OrderPlacedActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
    }

    private void loadplaceorder() {
        final Map<String, String> params = new HashMap<>();

        progressDialog.show();
        String para_str  = "?user_id=" + BSession.getInstance().getUser_id(getApplicationContext());
        String para_str1 = "&order_id=" + orderid;
        String para_str2 = "&payment_mode=" + paidtype;
        String para_str3 = "&fname=" + name.getText().toString();
        String para_str4 = "&phone=" + mobile.getText().toString();
        String para_str5 = "&email=" + email.getText().toString();
        String para_str6 = "&address=" + address.getText().toString();
        String para_str7 = "&payment_type=" + selectmethod;

        String baseUrl = ProductConfig.ordercheckout + para_str + para_str1 + para_str2 + para_str3 + para_str4 + para_str5 + para_str6 + para_str7;

        final StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONObject jsonResponse = new JSONObject(response);


                            if (jsonResponse.has("result") && jsonResponse.getString("result").equals("Success")) {

                                PlaceOrderSession.getInstance().initialize(OrderPlacedActivity.this,
                                        jsonResponse.getString("user_id"),
                                        jsonResponse.getString("user_name"), jsonResponse.getString("address"),
                                        jsonResponse.getString("user_email"),
                                        ""


                                );
                                Intent intent = new Intent(OrderPlacedActivity.this, MyOrderActivity.class);
                                startActivity(intent);


                            } else {
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        progressDialog.dismiss();
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


    public void startPayment() {
        /**   * Instantiate Checkout   */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID( "rzp_live_1TLXeLGXK8GXpa" );
        // co.setImage(R.drawable.logo_big);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Zuway");
            options.put("description", "Online Shopping");
            //You can omit the image option to fetch the image from dashboard
            //   options.put("image", "http://timemin.co.in/garuda/zuway/assets/img/logo_tr.png");
            options.put("currency", "INR");
            double paiseAmount = totalg * 100;
            //double paiseAmount = totalg * 100;
            options.put("amount", paiseAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email.getText().toString());
            preFill.put("contact", mobile.getText().toString());
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            Intent intent =new Intent( OrderPlacedActivity.this,ItemActivity.class );
            startActivity( intent );
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            progressDialog.show();
            String painf = "Paid";
            paidtype = painf;
            loadplaceorder();
            progressDialog.show();
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            paidtype = "Unpaid";
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();
            //Intent intent = null;
            onRestart();
            Intent intent=new Intent( OrderPlacedActivity.this,HomeActivity.class );
            startActivity( intent );



        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

  /*  @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
           Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
           // return;
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                //finish();
                Intent i = new Intent(getApplicationContext(), MyCartActivity.class);
                i.putExtra("scid",scid);
                i.putExtra("cid",cid);
                startActivity(i);
            }
        }, 20);
    }*/
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

      //  mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

     /*   if(mFocusDuringOnPause) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                st = extras.getString("total");
                dc = extras.getString("deliveryamount");
                gt = extras.getString("grandtotal");
                orderid = extras.getString("order_id");
                pc = extras.getString("product_count");
            }
            Intent intent=new Intent(OrderPlacedActivity.this,OrderPlacedActivity.class);
            intent.putExtra("total",st);
            intent.putExtra("deliveryamount",dc);
            intent.putExtra("grandtotal",gt);
            intent.putExtra("order_id",orderid);
            intent.putExtra("product_count",pc);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(OrderPlacedActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}
