package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.ZuWay.model.User;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FrogotPasswordChangeActivity extends AppCompatActivity {

    EditText newpass;
    EditText confirmpass;
    Button Savepass;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frogot_password_change);

        bundle();
        init();

        Savepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animFadein  = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Savepass.startAnimation(animFadein);

                String Newpassword    = newpass.getText().toString();
                String retypepassword = confirmpass.getText().toString();


                if (Newpassword.equals("") || Newpassword.length() < 4) {
                    newpass.setError("*Enter maximum 4 Characters");
                    newpass.requestFocus();
                    return;
                }

                if (retypepassword.equals("") || retypepassword.length() < 4) {
                    confirmpass.setError("Enter maximum 4 Characters");
                    confirmpass.requestFocus();
                    return;
                }

                if (!retypepassword.equals(Newpassword)) {
                    confirmpass.setError("*Mismatch confirm password");
                    confirmpass.requestFocus();
                    return;
                }

                if (retypepassword != null && retypepassword != "" && retypepassword.length() >= 4 && Newpassword != null && Newpassword != "" && Newpassword.length() >= 4
                        && retypepassword.equals(Newpassword)) {

                    forgotpasswordupdate();
                }


            }
        });

    }

    public void bundle(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobile    = extras.getString("mobile");

        }
    }

    public void init(){
        newpass     = findViewById(R.id.forgot_new_pass_edit);
        confirmpass = findViewById(R.id.forgot_confirm_pass_edit);
        Savepass    = findViewById(R.id.btn_save);

    }


    private void forgotpasswordupdate() {

        String Newpassword = newpass.getText().toString();
        String retypepassword = confirmpass.getText().toString();

        final Map<String, String> params = new HashMap<>();

        String par_str  ="?user_mobile="+mobile;
        String par_str1 ="&newpassword="+Newpassword;
        String par_str2 ="&retypepassword="+retypepassword;


        String baseUrl = ProductConfig.forgot_changepasswordurl+par_str+par_str1+par_str2;

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    if (jsonResponse.has("Success") && jsonResponse.getString("Success").equals("1")) {
                        ProductConfig.UserDetails = new User(jsonResponse);
                        Intent intent = new Intent(FrogotPasswordChangeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),
                                "Password updated successfully", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Password Update Failed !Password Mismatch", Toast.LENGTH_SHORT)
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
        RequestQueue requestQueue = Volley.newRequestQueue(FrogotPasswordChangeActivity.this);
        requestQueue.add(jsObjRequest);
    }

}
