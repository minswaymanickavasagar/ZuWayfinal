package com.ZuWay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ZuWay.R;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.PlaceOrderSession;
import com.ZuWay.utils.ProductConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReturnPolicyScreen extends AppCompatActivity {
    EditText et_name,et_address,et_p_name,et_phone,et_message,et_Orderid;
    TextView web,mail,add;
    Button btnsumit;
    boolean mFocusDuringOnPause;
    String id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_return_policy_screen );
        toolbar();
        init();
        bundle();


        btnsumit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation( getApplicationContext(), R.anim.fade_in );
                btnsumit.startAnimation( animFadein );

                String name         = et_name.getText().toString();
                String mobile       = et_phone.getText().toString();
                String address      = et_address.getText().toString();
                String mes          = et_message.getText().toString();
                String product_name = et_p_name.getText().toString();
                String order_id     = et_Orderid.getText().toString();


                if (name.isEmpty()) {
                    et_name.setError( "Kindly enter your name" );
                    et_name.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    et_address.setError( "Kindly enter your address" );
                    et_address.requestFocus();
                    return;
                }if (product_name.isEmpty()) {
                    et_p_name.setError( "Kindly enter your product name" );
                    et_p_name.requestFocus();
                    return;
                }
                if (order_id.isEmpty()) {
                    et_Orderid.setError( "Kindly enter your order id" );
                    et_Orderid.requestFocus();
                    return;
                }
                if (mobile.isEmpty()) {
                    et_phone.setError( "Kindly enter your mobile" );
                    et_phone.requestFocus();
                    return;
                }
                if (mobile.length() < 10) {
                    et_phone.setError( "Kindly enter Valid MobileNo" );
                    et_phone.requestFocus();
                    return;
                }

                if (mes == null) {
                    et_message.setError( "Kindly enter your address" );
                    et_message.requestFocus();
                    return;
                }


                if (name != null && name != "" && mobile != null && mobile != "" &&
                        mes != null&& address != ""&& product_name != ""&& order_id != "") {


                    returnpolicy();


                }
            }
        } );

    }
    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("order_id");
            name = extras.getString("product_name");
            et_p_name.setText(name);
            et_Orderid.setText(id);
        }
    }
    public void init(){

        et_address = findViewById( R.id.et_address );
        et_p_name  = findViewById( R.id.et_p_name );
        et_Orderid = findViewById( R.id.et_Orderid );
        et_message = findViewById( R.id.et_message );
        et_name    = findViewById( R.id.et_name );
        et_phone   = findViewById( R.id.et_phone );
        btnsumit   = findViewById( R.id.help_submit );
        web        = findViewById( R.id.web );
        mail       = findViewById( R.id.email );
        add        = findViewById( R.id.add );

        final String namee = PlaceOrderSession.getInstance().getUser_name( getApplicationContext() );
        final String emaill = PlaceOrderSession.getInstance().getUser_email( getApplicationContext() );
        final String mobilee = BSession.getInstance().getUser_mobile( getApplicationContext() );
        final String addresss = PlaceOrderSession.getInstance().getAddress( getApplicationContext() );

        et_address.setText( emaill );
        et_name.setText( namee );
        et_phone.setText( mobilee );
        et_address.setText( addresss );

    }

    private void returnpolicy() {

        String name         = et_name.getText().toString();
        String mobile       = et_phone.getText().toString();
        String address      = et_address.getText().toString();
        String mes          = et_message.getText().toString();
        String product_name = et_p_name.getText().toString();
        String order_id     = et_Orderid.getText().toString();


        final String userid = BSession.getInstance().getUser_id(getApplicationContext());

        final Map<String, String> params = new HashMap<>();

        String para_str  = "?user_id=" + userid;
        String para_str3 = "&user_name=" + name;
        String para_str1 = "&user_mobile=" + mobile;
        String para_str2 = "&order_id=" + order_id;
        String para_str4 = "&user_address=" + address;
        String para_str5 = "&product_name=" + product_name;
        String para_str6 = "&message=" + mes;

        String baseUrl = ProductConfig.returnpolicy + para_str + para_str1 + para_str2 + para_str3 + para_str4+para_str5+para_str6;

        StringRequest jsObjRequest = new StringRequest( Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e( "Response", response.toString() );
                try {
                    JSONObject jsonResponse = new JSONObject( response );

                    if (jsonResponse.has( "success" ) && jsonResponse.getString( "success" ).equals( "1" )) {
                        ProductConfig.UserDetails = new User( jsonResponse );
                        Toast.makeText( getApplicationContext(), "Return Product Submit successfully ", Toast.LENGTH_SHORT ).show();

                        Intent intent = new Intent( getApplicationContext(), HomeActivity.class );
                        startActivity( intent );

                    } else {
                        Toast.makeText( getApplicationContext(), "Return Product Failed.. ", Toast.LENGTH_SHORT ).show();


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
                // progressDialog.dismiss();

            }
        } ) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue( getApplicationContext() );
        requestQueue.add( jsObjRequest );
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

        activitytitle.setText("Return Policy");


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ShippingandRetrun.class);
        startActivity(intent);
    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

       // mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

      /*  if(mFocusDuringOnPause) {

            Intent intent=new Intent(ReturnPolicyScreen.this,ReturnPolicyScreen.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(ReturnPolicyScreen.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}