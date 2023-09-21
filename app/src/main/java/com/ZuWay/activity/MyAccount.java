package com.ZuWay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ZuWay.R;
import com.ZuWay.utils.BSession;

public class MyAccount extends AppCompatActivity {

    TextView tv_Myorder,tv_Mywish,tv_Myenqu,tv_Myedit,name, email;
    CircularImageView profile;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        toolbar();
        init();

        tv_Myedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_Myedit.startAnimation(animFadein);
                Intent intent = new Intent(MyAccount.this, UpdateAccountActivity.class);

                startActivity(intent);
            }
        });

        tv_Myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_Myorder.startAnimation(animFadein);
                Intent intent = new Intent(MyAccount.this, MyOrderActivity.class);
                intent.putExtra("activity","myaccount");
                startActivity(intent);
            }
        });

        tv_Myenqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_Myenqu.startAnimation(animFadein);
                Intent intent = new Intent(MyAccount.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        tv_Mywish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                tv_Mywish.startAnimation(animFadein);
                Intent intent = new Intent(MyAccount.this, MyWishActivity.class);
                startActivity(intent);
            }
        });

    }
    public void init(){

        tv_Myedit  = findViewById(R.id.myaccount_edit);
        tv_Myorder = findViewById(R.id.myaccount_order);
        tv_Myenqu  = findViewById(R.id.myaccount_enquri);
        tv_Mywish  = findViewById(R.id.myaccount_wishlist);
        profile    = findViewById(R.id.profile);
        name       = findViewById(R.id.name);
        email      = findViewById(R.id.email);

        final String name1  = BSession.getInstance().getUser_name(getApplicationContext());
        final String mobile = BSession.getInstance().getUser_mobile(getApplicationContext());
        String img          = BSession.getInstance().getUser_profileimage(getApplicationContext());
        name.setText(name1);
        email.setText(mobile);

        if(img!=null&& !img.equals("")){
            Glide.with(getApplicationContext())
                    .load(img)
                    .placeholder(R.drawable.cate_gray)
                    .into(profile);
        }

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
        activitytitle.setText("My Account");


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(MyAccount.this,HomeActivity.class);
        startActivity(intent);
    }
    public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();

        //mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

     /*   if(mFocusDuringOnPause) {


            Intent intent=new Intent(MyAccount.this,MyAccount.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MyAccount.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }*/
    }
}
