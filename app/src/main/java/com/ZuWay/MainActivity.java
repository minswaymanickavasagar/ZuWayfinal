package com.ZuWay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ZuWay.activity.HomeActivity;
import com.ZuWay.activity.LoginActivity;
import com.ZuWay.activity.NewLoginActivity;
import com.ZuWay.activity.UpdateAccountActivity;
import com.ZuWay.utils.BSession;

import java.util.BitSet;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 300;
    boolean mFocusDuringOnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String user = BSession.getInstance().getUser_id(MainActivity.this);
                System.out.println("mmmmmmmmm"+user);
                System.out.println("mmusermmmmm"+user.equals(""));
                if (user.equals("")){
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }

               // finish();
            }
        }, SPLASH_TIME_OUT);
    }
   /* public void onPause() {
        super.onPause();
        // Toast.makeText(SubCategoryActivity.this, "OnPause", Toast.LENGTH_SHORT).show();
        mFocusDuringOnPause = hasWindowFocus();
    }

    public void onStop() {
        super.onStop();

        if(mFocusDuringOnPause) {


            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            //Toast.makeText(SubCategoryActivity.this, "normal", Toast.LENGTH_SHORT).show();

            // normal scenario
        } else {
            Toast.makeText(MainActivity.this, "off", Toast.LENGTH_SHORT).show();

            // activity was started when screen was off / screen was on with keygaurd displayed
        }
    }*/
}
