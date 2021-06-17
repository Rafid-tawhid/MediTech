package com.hk.meditechuser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.hk.meditechuser.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        //added animation on Icon

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //Animation start

        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.blink_icon);
        binding.animateIcon.startAnimation(animation);
        //post delayed on welcome screen
        splash();
    }

    private void splash() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Common.isConnectedToInternet(SplashActivity.this)) {
                    startActivity(new Intent(SplashActivity.this, UserRoleActivity.class));
                    finish();
                }
                else{
                    //show no internet Dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                    final View view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.no_internet_dialogue, null);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button exitBtn = view.findViewById(R.id.exitButton);

                    exitBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    });

                    alertDialog.setView(view);
                    alertDialog.show();

                }

            }
        }, 2000);
    }
}
