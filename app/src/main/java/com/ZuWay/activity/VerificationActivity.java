package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ZuWay.R;

public class VerificationActivity extends AppCompatActivity {
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        final Button signin=findViewById(R.id.btnveri);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                signin.startAnimation(animFadein);
                Intent intent=new Intent(VerificationActivity.this,OtpActivity.class);
                startActivity(intent);
            }
        });

        login =findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                login.startAnimation(animFadein);
                Intent intent=new Intent(VerificationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
