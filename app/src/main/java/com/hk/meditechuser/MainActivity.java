package com.hk.meditechuser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.hk.meditechuser.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getPermission();


        //button
        binding.findPationtId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //find out patient within QR code
                startActivity(new Intent(MainActivity.this, ScanActivity.class));

            }
        });

        binding.nearByPlaceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request for turn on gps location
                 manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();

                } else {
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                }

            }
        });

        binding.guideLineId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show apps guideline
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

    private void getPermission() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(permissions, 0);   //request for permission
            } else {

            }
        }
    }

    //check gps location on or off
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Turn on your GPS!");
        builder.setMessage("You should turn on your location, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
