package com.hk.meditechauthenticate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.hk.meditechauthenticate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.authPationtId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AuthenticateActivity.class));
            }
        });

        binding.summeryId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowSummeryActivity.class));
            }
        });

        binding.guideLineId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GuideLineActivity.class));
            }
        });

        binding.aboutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show no internet Dialog
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                final View alertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.about_dialogue, null);
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button okBtn = alertView.findViewById(R.id.okButton);

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setView(alertView);
                alertDialog.show();
            }
        });
    }


}
