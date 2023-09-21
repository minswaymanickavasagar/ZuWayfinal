package com.ZuWay.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ZuWay.R;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UpdateAccountActivity extends AppCompatActivity implements LocationListener {


    LinearLayout llpf,llch,llprofile;
    FrameLayout llchpass;
    EditText changef_old_passw, changef_new_passw, changef_confirm_passw,txname, txmobile, txaddress, txemail;
    Button btnsubmit,chngepwdbtn;
    TextView getLocat;
    LocationManager locationManager;
    String placeimg;
    Bitmap bitmap;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    CircularImageView capture_img;
    View ll1,ll2;
    private Uri mCropImageUri;
    public static final int RequestPermissionCode = 1;
    boolean mFocusDuringOnPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        progressDialog = new ProgressDialog(UpdateAccountActivity.this);
        progressDialog.setMessage("Loading.....");

        init();
        toolbar();


        chngepwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadein = AnimationUtils.loadAnimation(UpdateAccountActivity.this, R.anim.fade_in);
                chngepwdbtn.startAnimation(animFadein);
                SubmitButton();
            }
        });

        llpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llprofile.setVisibility(View.VISIBLE);
                llchpass.setVisibility(View.GONE);
                ll1.setVisibility(View.VISIBLE);
                ll2.setVisibility(View.GONE);

            }
        });

        llch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llprofile.setVisibility(View.GONE);
                llchpass.setVisibility(View.VISIBLE);
                ll1.setVisibility(View.GONE);
                ll2.setVisibility(View.VISIBLE);

            }
        });




        getLocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(UpdateAccountActivity.this, R.anim.fade_in);
                getLocat.startAnimation(animFadein);
                getLocation();
            }
        });

        if (ContextCompat.checkSelfPermission(UpdateAccountActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateAccountActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(UpdateAccountActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }



        String img = BSession.getInstance().getUser_profileimage(UpdateAccountActivity.this);

        if(img!=null&& !img.equals("http://timemin.co.in/garuda/zuway/webupload/original/")){
            Glide.with(UpdateAccountActivity.this)
                    .load(img)
                    .placeholder(R.drawable.cate_gray)
                    .into(capture_img);
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animFadein = AnimationUtils.loadAnimation(UpdateAccountActivity.this, R.anim.fade_in);
                btnsubmit.startAnimation(animFadein);
                String name    = txname.getText().toString();
                String mobile  = txmobile.getText().toString();
                String address = txaddress.getText().toString();

                if (name.isEmpty()) {
                    txname.setError("Kindly enter your name");
                    txname.requestFocus();
                    return;
                }

                if (mobile.isEmpty()) {
                    txmobile.setError("Kindly enter your mobile");
                    txmobile.requestFocus();
                    return;
                }
                if (mobile.length() < 10) {
                    txmobile.setError("*Kindly enter Valid MobileNo");
                    txmobile.requestFocus();
                    return;
                }



                if (address.isEmpty()) {
                    txaddress.setError("Kindly enter your address");
                    txaddress.requestFocus();
                    return;
                }
                if (txaddress==null) {
                    txaddress.setError("Kindly enter your address");
                    txaddress.requestFocus();
                    return;
                }


                if (name != null && name != "" && mobile != null && mobile != "" &&
                        txaddress != null  && address!=null&& address!="") {
                    Updateprofile();

                } else {
                    Toast.makeText(UpdateAccountActivity.this,
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });


        capture_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(UpdateAccountActivity.this, R.anim.fade_in);
                capture_img.startAnimation(animFadein);
                onSelectImageClick(v);
            }
        });
        EnableRuntimePermission();


    }

    public void init(){

        llpf                  = findViewById(R.id.pfll);
        llch                  = findViewById(R.id.chll);
        llprofile             = findViewById(R.id.llprofile);
        llchpass              = findViewById(R.id.llchpass);
        ll2                   = findViewById(R.id.ll2);
        ll1                   = findViewById(R.id.ll1);
        chngepwdbtn           = findViewById(R.id.change_butn);
        changef_old_passw     = findViewById(R.id.changef_old_passw);
        changef_new_passw     = findViewById(R.id.changef_new_passw);
        changef_confirm_passw = findViewById(R.id.changef_confirm_passw);
        txname                = findViewById(R.id.et_name);
        txmobile              = findViewById(R.id.et_phone);
        txemail               = findViewById(R.id.et_email);
        txaddress             = findViewById(R.id.et_address);
        btnsubmit             = findViewById(R.id.setting_sumit_butn);
        simpleProgressBar     = (ProgressBar)findViewById(R.id.simpleProgressBar);
        getLocat              = findViewById(R.id.getlocation);
        capture_img           = findViewById(R.id.Improfile);

        final String name     = BSession.getInstance().getUser_name(UpdateAccountActivity.this);
        final String email    = BSession.getInstance().getUser_email(UpdateAccountActivity.this);
        final String mobile   = BSession.getInstance().getUser_mobile(UpdateAccountActivity.this);
        final String address  = BSession.getInstance().getUser_address(UpdateAccountActivity.this);

        txname.setText(name);
        txemail.setText(email);
        txmobile.setText(mobile);
        txaddress.setText(address);
    }

    private void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(UpdateAccountActivity.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(UpdateAccountActivity.this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(UpdateAccountActivity.this, imageUri)) {
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

            } else {
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((CircularImageView) findViewById(R.id.Improfile)).setImageURI(result.getUri());

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(UpdateAccountActivity.this.getContentResolver(), (result.getUri()));

                    capture_img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropImageUri);
        } else {
        }
    }

    private void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateAccountActivity.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(UpdateAccountActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(UpdateAccountActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(UpdateAccountActivity.this);
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp = ((BitmapDrawable) capture_img.getDrawable()).getBitmap();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }



    void getLocation() {
        try {
            progressDialog.show();
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
            Geocoder geocoder = new Geocoder(UpdateAccountActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            txaddress.setText(addresses.get(0).getAddressLine(0));
            progressDialog.dismiss();
            getLocat.setVisibility(View.GONE);

        } catch (Exception e) {


        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(UpdateAccountActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }



    private void Updateprofile() {
        final User user = new User();
        user.setAction("userprofileupdate");
        String name    = txname.getText().toString();
        String mobile  =  txmobile.getText().toString();
        String email   = txemail.getText().toString();
        String address = txaddress.getText().toString();

        if (bitmap != null) {
            Bitmap bm400=getScaledBitmap(bitmap,500,500);
            placeimg = getStringImage(bm400);
        } else {
            placeimg = "0";
        }

        final String userid = BSession.getInstance().getUser_id(getApplicationContext());
        final Map<String, String> params = new HashMap<>();

        params.put("user_id", userid);
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_address", address);
        params.put("user_email", email);
        params.put("user_profileimage", placeimg);

        progressDialog.show();
        String baseUrl = ProductConfig.userprofileupdate;
        StringRequest jsObjRequest = new StringRequest(Request.Method.POST, baseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response.toString());
                try {
                    progressDialog.show();
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                        progressDialog.dismiss();
                        ProductConfig.UserDetails = new User(jsonResponse);

                        Toast.makeText(UpdateAccountActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        ProductConfig.UserDetails = new User(jsonResponse);
                        BSession.getInstance().initialize(UpdateAccountActivity.this,
                                jsonResponse.getString("user_id"),
                                jsonResponse.getString("user_name"),
                                jsonResponse.getString("user_mobile"),
                                jsonResponse.getString("user_email"),
                                jsonResponse.getString("user_address"),jsonResponse.getString("user_profileimage"),"",""
                        );
                        Intent intent = new Intent(UpdateAccountActivity.this, HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(UpdateAccountActivity.this, "Update Failed.. ", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateAccountActivity.this);
        requestQueue.add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



    private void toolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               // finish();
                onBackPressed();
            }
        });

        TextView activitytitle = findViewById(R.id.toolbar_activity_title1);
        activitytitle.setText("Profile Edit");


    }


    private void SubmitButton() {


        String oldpassword = changef_old_passw.getEditableText().toString();
        String Newpassword = changef_new_passw.getEditableText().toString();
        String retypepassword = changef_confirm_passw.getEditableText().toString();

        if (oldpassword.equals("") || oldpassword.length() < 4) {
            changef_old_passw.setError("*Enter Valid Old Password");
            changef_old_passw.requestFocus();
            return;
        }
        if (Newpassword.equals("") || Newpassword.length() < 4) {
            changef_new_passw.setError("Kindly enter Valid Old Password");
            changef_new_passw.requestFocus();
            return;
        }
        if (retypepassword.equals("") || retypepassword.length() < 4) {
            changef_confirm_passw.setError("Kindly enter Valid Old Password");
            changef_confirm_passw.requestFocus();
            return;
        }
        if (!retypepassword.equals(Newpassword)) {
            changef_confirm_passw.setError("Kindly enter Valid Old Password");
            changef_confirm_passw.requestFocus();
            return;
        }

        if (retypepassword.equals(oldpassword)) {
            changef_confirm_passw.setError("Old Password and New Password Same.\n Use different Password to update");
            return;
        }
        if (retypepassword != null && retypepassword != "" && retypepassword.length() >= 4 && Newpassword != null && Newpassword != "" && Newpassword.length() >= 4
                && retypepassword.equals(Newpassword)) {

            final String userid = BSession.getInstance().getUser_id(getApplicationContext());
            final User user = new User();
            user.setAction("userchangepassword");
            final Map<String, String> params = new HashMap<>();
            params.put("customerid", userid);
            params.put("oldpassword", oldpassword);
            params.put("newpassword", Newpassword);
            params.put("retypepassword", retypepassword);


            String Para_str  = "?user_id=" + userid;
            String Para_str1 = "&oldpassword=" + oldpassword;
            String Para_str2 = "&newpassword=" + Newpassword;
            String Para_str3 = "&retypepassword=" + retypepassword;

            String baseUrl = ProductConfig.userchangepassword+Para_str+Para_str1+Para_str2+Para_str3;
            StringRequest jsObjRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.
                    Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Response", response.toString());
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("success") && jsonResponse.getString("success").equals("1")) {
                            ProductConfig.UserDetails = new User(jsonResponse);

                            Toast.makeText(UpdateAccountActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(UpdateAccountActivity.this, "Wrong old password.", Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(UpdateAccountActivity.this);
            requestQueue.add(jsObjRequest);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            Intent intent=new Intent(UpdateAccountActivity.this,HomeActivity.class);
            startActivity(intent);

    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
      //  mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

       /* if(mFocusDuringOnPause) {


            Intent intent=new Intent(UpdateAccountActivity.this,UpdateAccountActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(UpdateAccountActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}

