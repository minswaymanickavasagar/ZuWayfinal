package com.ZuWay.fragment;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ZuWay.R;
import com.ZuWay.activity.LoginActivity;
import com.ZuWay.model.User;
import com.ZuWay.utils.BSession;
import com.ZuWay.utils.ProductConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeaPasswordFragment extends Fragment {
    String strtext;
    EditText changef_old_passw, changef_new_passw, changef_confirm_passw;
    Button chngepwdbtn;
    LinearLayout layout_change,lauout_guest;
    TextView regi;

    public ChangeaPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changea_password, container, false);



        chngepwdbtn = view.findViewById(R.id.change_butn);

        changef_old_passw = view.findViewById(R.id.changef_old_passw);
        changef_new_passw = view.findViewById(R.id.changef_new_passw);
        changef_confirm_passw = view.findViewById(R.id.changef_confirm_passw);

        chngepwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadein = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                chngepwdbtn.startAnimation(animFadein);
                SubmitButton();
            }
        });



        return view;
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

            final String userid = BSession.getInstance().getUser_id(getContext());
            final User user = new User();
            user.setAction("userchangepassword");
            final Map<String, String> params = new HashMap<>();
            params.put("customerid", userid);
            params.put("oldpassword", oldpassword);
            params.put("newpassword", Newpassword);
            params.put("retypepassword", retypepassword);


            String Para_str = "?user_id=" + userid;
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

                            Toast.makeText(getContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getContext(), "Wrong old password.", Toast.LENGTH_SHORT).show();
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
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(jsObjRequest);

        }
    }

}
