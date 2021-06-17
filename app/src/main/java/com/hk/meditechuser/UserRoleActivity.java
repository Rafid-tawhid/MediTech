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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechuser.Model.Patient;
import com.hk.meditechuser.databinding.ActivityUserRoleBinding;

public class UserRoleActivity extends AppCompatActivity {
    private ActivityUserRoleBinding binding;
    private DatabaseReference databaseReference;
    private SharedPreferences sessionPreferences;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_role);
        //manage session
        sessionPreferences = getSharedPreferences("user", MODE_PRIVATE);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //progress dialogue set up

        progressDialog = new AlertDialog.Builder(UserRoleActivity.this).create();
        final View view = LayoutInflater.from(UserRoleActivity.this).inflate(R.layout.progress_diaog_layout, null);
        progressDialog.setCancelable(false);
        progressDialog.setView(view);

        //interaction
        binding.guestRoleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRoleActivity.this, MainActivity.class));
            }
        });

        binding.patientRoleId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                //check within shared preference for next time remembering
                String patientId = sessionPreferences.getString("phone", "");
                if (patientId.equals("")) {
                    progressDialog.dismiss();
                    startActivity(new Intent(UserRoleActivity.this, SignInActivity.class));
                } else {
                    //setStaticData();
                    databaseReference.child("patients").child(patientId).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Patient cPatient = dataSnapshot.getValue(Patient.class);
                                Common.currentPatient = cPatient;

                                progressDialog.dismiss();
                                startActivity(new Intent(UserRoleActivity.this, PatientActivity.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }
}
