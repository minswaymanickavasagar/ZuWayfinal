package com.ZuWay.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ZuWay.R;
import com.ZuWay.activity.HomeActivity;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileUpdateFragment extends Fragment implements LocationListener {
    EditText txname, txmobile, txaddress, txemail;
    Button btnsubmit;
    TextView getLocat;
    LocationManager locationManager;
    String placeimg,img;
    Bitmap bitmap;
    ProgressBar simpleProgressBar;
    ProgressDialog progressDialog;
    CircularImageView capture_img;
    public ProfileUpdateFragment() {
    }
    View view;
    private Uri mCropImageUri;
    public static final int RequestPermissionCode = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile_update, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading.....");

        init();

        getLocat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                getLocat.startAnimation(animFadein);
                getLocation();
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


       if(img!=null&& !img.equals("")){
            Glide.with(getActivity())
                    .load(img)
                    .placeholder(R.drawable.cate_gray)
                    .into(capture_img);
        }

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                btnsubmit.startAnimation(animFadein);
                String name    = txname.getText().toString();
                String mobile  = txmobile.getText().toString();
                String email   = txemail.getText().toString();
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
                    Toast.makeText(getContext(),
                            "Please enter your details!", Toast.LENGTH_SHORT)
                            .show();
                }


            }
        });


        capture_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                capture_img.startAnimation(animFadein);
                onSelectImageClick(v);
            }
        });
        EnableRuntimePermission();


        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra("crop", "true");
        i.putExtra("aspectX", 100);
        i.putExtra("aspectY", 100);
        i.putExtra("outputX", 256);
        i.putExtra("outputY", 356);

        try {

            i.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(i, "Select Picture"), 0);
        }catch (ActivityNotFoundException ex){
            ex.printStackTrace();
        }

        return view;

    }
    public void init(){
        simpleProgressBar = (ProgressBar)view.findViewById(R.id.simpleProgressBar);
        getLocat          = view.findViewById(R.id.getlocation);
        capture_img       = view.findViewById(R.id.Improfile);
        txname            = view.findViewById(R.id.et_name);
        txmobile          = view.findViewById(R.id.et_phone);
        txemail           = view.findViewById(R.id.et_email);
        txaddress         = view.findViewById(R.id.et_address);
        btnsubmit         = view.findViewById(R.id.setting_sumit_butn);

    }


    private void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {
                startCropImageActivity(imageUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((CircularImageView) view.findViewById(R.id.Improfile)).setImageURI(result.getUri());

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), (result.getUri()));

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

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {

            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
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
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 8000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        progressDialog.show();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            txaddress.setText(addresses.get(0).getAddressLine(0));
            progressDialog.dismiss();
            getLocat.setVisibility(View.GONE);

        } catch (Exception e) {


        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
        String mobile  = txmobile.getText().toString();
        String email   = txemail.getText().toString();
        String address = txaddress.getText().toString();

        if (bitmap != null) {
            placeimg = getStringImage(bitmap);

        } else {
            placeimg = "0";
        }
        final String userid = BSession.getInstance().getUser_id(getContext());

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

                        Toast.makeText(getContext(), "Welcome to our Zuway – Profile Updated", Toast.LENGTH_SHORT).show();
                        ProductConfig.UserDetails = new User(jsonResponse);
                        BSession.getInstance().initialize(getActivity(),
                                jsonResponse.getString("user_id"),
                                jsonResponse.getString("user_name"),
                                jsonResponse.getString("user_mobile"),
                                jsonResponse.getString("user_email"),
                                jsonResponse.getString("user_address"),jsonResponse.getString("user_profileimage"),"",""
                        );
                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getContext(), "Update Failed.. ", Toast.LENGTH_SHORT).show();


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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsObjRequest);

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
