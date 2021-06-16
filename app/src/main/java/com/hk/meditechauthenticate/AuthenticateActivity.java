package com.hk.meditechauthenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechauthenticate.Model.Patient;
import com.hk.meditechauthenticate.Model.Visit;
import com.hk.meditechauthenticate.databinding.ActivityAuthenticateBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthenticateActivity extends AppCompatActivity {
    private ActivityAuthenticateBinding binding;
    private DatabaseReference databaseReference;
    private String patientId = "";
    private String cDate;
    private boolean saveData;
    private Patient cPatient;
    SimpleDateFormat cFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_authenticate);

        //firebase init
        databaseReference = FirebaseDatabase.getInstance().getReference();


        binding.progress.setVisibility(View.VISIBLE);
        //add listener when change any value of RFID node
        databaseReference.child("RFID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    patientId = dataSnapshot.getValue().toString();  //initializing a global variable
                    binding.patientId.setText(dataSnapshot.getValue().toString());
                    checkPatient(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //details button
        binding.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!patientId.equals("") && Common.currentPatient!=null) {
                    startActivity(new Intent(AuthenticateActivity.this, PatientActivity.class));
                }
                else{
                    Toast.makeText(AuthenticateActivity.this, "Unknown Patient with this Id", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkPatient(final String patientId) {

        databaseReference.child("patients").child(patientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    //patient exist with this number
                    showPatient(patientId);


                } else {

                    binding.progress.setVisibility(View.GONE);
                    //patient not exist with this number

                    binding.circleImageView.setImageResource(R.drawable.ic_add_profile_pic);
                    binding.name.setText("");
                    binding.address.setText("");
                    binding.genderId.setText("");
                    binding.ageId.setText("");
                    binding.bllodId.setText("");

                    binding.verifiedIV.setImageResource(R.drawable.ic_unvarified);
                    binding.verifiedTV.setText("Patient unverified!");
                    binding.verifiedTV.setTextColor(Color.RED);

                    binding.detailButton.setVisibility(View.GONE);  //hide detail button
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showPatient(final String patientId) {

        databaseReference.child("patients").child(patientId).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //show patient info
                    cPatient = dataSnapshot.getValue(Patient.class);
                    Common.currentPatient = cPatient;   //tracking current patient


                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).placeholder(R.drawable.ic_add_profile_pic).into(binding.circleImageView);
                    binding.name.setText(dataSnapshot.child("name").getValue().toString());
                    binding.address.setText(dataSnapshot.child("address").getValue().toString());
                    binding.genderId.setText(dataSnapshot.child("gender").getValue().toString());
                    binding.ageId.setText(dataSnapshot.child("age").getValue().toString());
                    binding.bllodId.setText(dataSnapshot.child("bloodGroup").getValue().toString());

                    //show status
                    binding.detailButton.setVisibility(View.VISIBLE);

                    binding.verifiedIV.setImageResource(R.drawable.ic_ok_varified_sign);
                    binding.verifiedTV.setText("Verified");
                    binding.verifiedTV.setTextColor(getResources().getColor(R.color.textColor));
                    binding.progress.setVisibility(View.GONE);

                    //save to summery as current date
                    saveToSummery(

                            patientId,
                            dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("address").getValue().toString(),
                            dataSnapshot.child("image").getValue().toString()

                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void saveToSummery(final String patientId, String name, String address,String image) {
        cDate = cFormat.format(new Date());
        Log.d("Date", cDate);

        saveData = true;
        final Visit visit = new Visit(patientId, name, address, image);

        databaseReference.child("summaries").child(cDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()) {
                        Visit cVisit = pSnapshot.getValue(Visit.class);
                        if (patientId.equals(cVisit.getPatientId())) {
                            saveData = false;
                        }
                    }
                    if (saveData) {
                        databaseReference.child("summaries").child(cDate).push().setValue(visit);
                    }
                } else {
                    databaseReference.child("summaries").child(cDate).push().setValue(visit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void goBack(View view) {
        finish();
    }
}
