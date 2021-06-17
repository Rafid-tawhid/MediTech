package com.hk.meditechuser;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechuser.Adapter.AdapterHealth;
import com.hk.meditechuser.Model.Health;
import com.hk.meditechuser.databinding.FragmentHealthBinding;

import java.util.ArrayList;
import java.util.List;


public class HealthFragment extends Fragment {
    private FragmentHealthBinding binding;
    private Context context;
    private List<Health> healthList;
    private DatabaseReference databaseReference;
    private AdapterHealth adapterHealth;
    public HealthFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_health, container, false);
        context = container.getContext();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize
        healthList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapterHealth = new AdapterHealth(context, healthList);
        binding.hConditionRV.setLayoutManager(new LinearLayoutManager(context));
        binding.hConditionRV.setAdapter(adapterHealth);

        getHealth();
    }
    private void getHealth() {
        binding.progress.setVisibility(View.VISIBLE);

        DatabaseReference healthReffReference = databaseReference.child("patients").child(Common.currentPatient.getPhoneNo()).child("healthConditions");
        healthReffReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    binding.emptyItemView.setVisibility(View.GONE);
                    healthList.clear();
                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()) {
                        Health cHealth = pSnapshot.getValue(Health.class);
                        healthList.add(cHealth);
                        adapterHealth.notifyDataSetChanged();
                    }
                    binding.progress.setVisibility(View.GONE);
                } else {
                    //empty data
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
