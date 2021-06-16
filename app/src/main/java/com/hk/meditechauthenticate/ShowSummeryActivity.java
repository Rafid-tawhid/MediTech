package com.hk.meditechauthenticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechauthenticate.Adapter.AdapterSummery;
import com.hk.meditechauthenticate.Model.Visit;
import com.hk.meditechauthenticate.databinding.ActivityShowSummeryBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowSummeryActivity extends AppCompatActivity {
    private ActivityShowSummeryBinding binding;
    private List<Visit> visitList;
    private DatabaseReference databaseReference;
    private AdapterSummery adapterSummery;
    private String cDate;

    SimpleDateFormat cFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_summery);

        //initialize
        databaseReference = FirebaseDatabase.getInstance().getReference();
        visitList = new ArrayList<>();
        adapterSummery = new AdapterSummery(ShowSummeryActivity.this, visitList);
        binding.summeryRV.setLayoutManager(new LinearLayoutManager(ShowSummeryActivity.this));
        binding.summeryRV.setAdapter(adapterSummery);


        getData();

    }

    private void getData() {
        binding.progress.setVisibility(View.VISIBLE);
        cDate = cFormat.format(new Date());
        Log.d("Date", cDate);
        databaseReference.child("summaries").child(cDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    binding.emptyItemView.setVisibility(View.GONE);
                    visitList.clear();
                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()) {
                        Visit visit = pSnapshot.getValue(Visit.class);
                        visitList.add(visit);
                        adapterSummery.notifyDataSetChanged();
                    }
                    binding.progress.setVisibility(View.GONE);
                } else {
                    //empty
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


    public void back(View view) {
        finish();
    }
}
