package com.ashesha.V3Care;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;

import com.ashesha.V3Care.Dashboard.NavDashboardActivity;
import com.ashesha.V3Care.Logins.MainLoginsActivity;
import com.ashesha.V3Care.Utils.AppConstants;
import com.ashesha.V3Care.Utils.GlobalVariable;
import com.ashesha.V3Care.Utils.PrefManager;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    AppCompatImageView ivImage;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        //  ivImage=findViewById(R.id.logo_one);
        prefManager = new PrefManager(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getSupportActionBar().hide();
        GlobalVariable.deviceWidth = displayMetrics.widthPixels;
        GlobalVariable.deviceHeight = displayMetrics.heightPixels;

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!prefManager.getBoolean(AppConstants.APP_USER_LOGIN)) {
                    Intent intent = new Intent(SplashActivity.this, MainLoginsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, NavDashboardActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    finish();
                }
            }
        }, 2000);
    }


}

