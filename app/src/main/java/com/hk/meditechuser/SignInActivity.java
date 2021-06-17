package com.hk.meditechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechuser.Model.Patient;
import com.hk.meditechuser.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private AlertDialog progressDialog;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);



        //progress dialogue set up

        progressDialog = new AlertDialog.Builder(SignInActivity.this).create();
        final View view = LayoutInflater.from(SignInActivity.this).inflate(R.layout.progress_diaog_layout, null);
        progressDialog.setCancelable(false);
        progressDialog.setView(view);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.phoneET.getText().toString().trim().equals("")) {
                    binding.phoneET.setError("Enter Phone No");
                    binding.phoneET.requestFocus();
                    return;
                }
                if (binding.passWordET.getText().toString().trim().equals("")) {
                    binding.passWordET.setError("Enter Password");
                    binding.passWordET.requestFocus();
                    return;
                }

                checkPatient(binding.phoneET.getText().toString(), binding.passWordET.getText().toString());
            }
        });

        binding.signupTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
            }
        });

        //back button
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkPatient(String phone, final String pass) {
        progressDialog.show();

        databaseReference.child("patients").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("password").exists()) {
                        if (dataSnapshot.child("password").getValue().toString().equals(pass)) {
                            Patient patient = dataSnapshot.child("profile").getValue(Patient.class);
                            Common.currentPatient = patient;
                            Toast.makeText(SignInActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();

                            //go patient panel
                            startActivity(new Intent(SignInActivity.this,PatientActivity.class));
                            finish();

                        }
                        else{
                            Toast.makeText(SignInActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Unregistered patientID!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Unknown patient Id!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
