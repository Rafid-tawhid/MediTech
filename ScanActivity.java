package com.hk.meditechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.hk.meditechuser.Model.Patient;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private DatabaseReference databaseReference;
    private AlertDialog progressDialog, notFoundDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scannerView = findViewById(R.id.scanLayout);


        //progress dialogue set up

        progressDialog = new AlertDialog.Builder(ScanActivity.this).create();
        final View view = LayoutInflater.from(ScanActivity.this).inflate(R.layout.progress_diaog_layout, null);
        progressDialog.setCancelable(false);
        progressDialog.setView(view);


        //not found dialogue
        notFoundDialogue = new AlertDialog.Builder(ScanActivity.this).create();
        final View nView = LayoutInflater.from(ScanActivity.this).inflate(R.layout.not_patient_found_dialogue, null);
        Button reScanButton = nView.findViewById(R.id.reScanBtn);
        reScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notFoundDialogue.dismiss();
                startActivity(new Intent(ScanActivity.this, ScanActivity.class));
                finish();
            }
        });
        notFoundDialogue.setCancelable(false);
        notFoundDialogue.setView(nView);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());

        //check patientId within try block because result might be differents from firebase data node support
        try {
            checkPatient(myResult);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            progressDialog.dismiss();
            notFoundDialogue.show(); //show not found dialogue and rescan
        }


    }

    private void checkPatient(String patientId) {
        //binding.progress.setVisibility(View.VISIBLE);
        progressDialog.show();

        final DatabaseReference userRef = databaseReference.child("patients").child(patientId).child("profile");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    Common.currentPatient = patient;
                    //binding.progress.setVisibility(View.GONE);
                    progressDialog.dismiss();

                    //go to patient activity
                    startActivity(new Intent(ScanActivity.this, GuestActivity.class));
                    finish();
                } else {
                    //binding.progress.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    notFoundDialogue.show();  //show not found dialogue and rescan
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void goBack(View view) {
        startActivity(new Intent(ScanActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ScanActivity.this, MainActivity.class));
        finish();
    }
}
