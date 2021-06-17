package com.hk.meditechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
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
import com.hk.meditechuser.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
private ActivitySignUpBinding binding;
    private AlertDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        //progress dialogue set up

        progressDialog = new AlertDialog.Builder(SignUpActivity.this).create();
        final View view = LayoutInflater.from(SignUpActivity.this).inflate(R.layout.progress_diaog_layout, null);
        progressDialog.setCancelable(false);
        progressDialog.setView(view);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.phoneET.getText().toString().trim().equals("")){
                    binding.phoneET.setError("Enter Phone No");
                    binding.phoneET.requestFocus();
                    return;
                }
                if(binding.passWordET.getText().toString().trim().equals("")){
                    binding.passWordET.setError("Enter Password");
                    binding.passWordET.requestFocus();
                    return;
                }
                if(binding.confirmPassWordET.getText().toString().trim().equals("")){
                    binding.confirmPassWordET.setError("Confirm Password");
                    binding.confirmPassWordET.requestFocus();
                    return;
                }
                if(!binding.confirmPassWordET.getText().toString().equals(binding.passWordET.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Password not matched", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData(binding.phoneET.getText().toString(), binding.passWordET.getText().toString());
            }
        });

        binding.signInTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
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
    public void saveData(final String phone, final String pass){
    progressDialog.show();
        databaseReference.child("patients").child(phone).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    Common.currentPatient = patient;
                    databaseReference.child("patients").child(phone).child("password").setValue(pass);
                    progressDialog.dismiss();

                    Toast.makeText(SignUpActivity.this, "SignUp successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this,PatientActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Unregistered Patients!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
