package com.hk.meditechauthenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechauthenticate.Adapter.PatientPagerAdapter;
import com.hk.meditechauthenticate.databinding.ActivityPatientBinding;

public class  PatientActivity extends AppCompatActivity {
    private ActivityPatientBinding binding;
    private PatientPagerAdapter patientViewPagerAdapter;
    private DatabaseReference databaseReference;
    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient);

        //progress dialogue set up
        progressDialog = new AlertDialog.Builder(PatientActivity.this).create();
        final View view = LayoutInflater.from(PatientActivity.this).inflate(R.layout.progress_diaog_layout, null);

        progressDialog.setCancelable(false);
        progressDialog.setView(view);

        //firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //setting tablayout
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Health").setIcon(R.drawable.ic_healthcondition));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Reports").setIcon(R.drawable.ic_healthdocuments));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Prescriptions").setIcon(R.drawable.ic_prescriptions));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Profile").setIcon(R.drawable.ic_profile));
        patientViewPagerAdapter = new PatientPagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(patientViewPagerAdapter);

        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //set title header including patient id and date
        setHeader();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setHeader() {
        progressDialog.show();
        if (Common.currentPatient != null) {
            //set date
            databaseReference.child("patients").child(Common.currentPatient.getPhoneNo()).child("lastUpdated").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        binding.updatedDate.setText(dataSnapshot.getValue().toString());
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //set patient id
            binding.patientId.setText(Common.currentPatient.getPhoneNo());
        }
    }
}
