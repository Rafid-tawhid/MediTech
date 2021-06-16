package com.hk.meditechauthenticate;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechauthenticate.Adapter.AdapterPrescription;
import com.hk.meditechauthenticate.Model.Prescription;
import com.hk.meditechauthenticate.databinding.FragmentPrescriptionBinding;

import java.util.ArrayList;
import java.util.List;


public class PrescriptionFragment extends Fragment {
    FragmentPrescriptionBinding binding;
    private DatabaseReference databaseReference;
    private List<Prescription> prescriptionList;
    private AdapterPrescription adapterPrescription;
    private Context context;

    public PrescriptionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prescription, container, false);
        context = container.getContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("patients")
                .child(Common.currentPatient.getPhoneNo())
                .child("prescriptions");
        prescriptionList = new ArrayList<>();
        adapterPrescription = new AdapterPrescription(prescriptionList, context);
        binding.prescriptionRV.setLayoutManager(new LinearLayoutManager(context));
        binding.prescriptionRV.setAdapter(adapterPrescription);

        getPrescription();


    }

    private void getPrescription() {
        binding.progress.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    prescriptionList.clear();
                    binding.emptyItemView.setVisibility(View.GONE);
                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()) {
                        Prescription cPrescription = pSnapshot.getValue(Prescription.class);
                        prescriptionList.add(cPrescription);
                        adapterPrescription.notifyDataSetChanged();
                    }
                    binding.progress.setVisibility(View.GONE);
                } else {
                    binding.emptyItemView.setVisibility(View.VISIBLE);
                    binding.progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
