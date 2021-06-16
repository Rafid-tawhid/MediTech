package com.hk.meditechauthenticate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hk.meditechauthenticate.AuthenticateActivity;
import com.hk.meditechauthenticate.Common;
import com.hk.meditechauthenticate.Model.Patient;
import com.hk.meditechauthenticate.Model.Visit;
import com.hk.meditechauthenticate.PatientActivity;
import com.hk.meditechauthenticate.R;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterSummery extends RecyclerView.Adapter<AdapterSummery.ViewHolder> {
    private Context context;
    private List<Visit> visitList;
    private Patient cPatient;
    private DatabaseReference databaseReference;


    public AdapterSummery(Context context, List<Visit> visitList) {
        this.context = context;
        this.visitList = visitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summery_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Visit cVisit = visitList.get(position);


        Picasso.get().load(cVisit.getImage()).placeholder(R.drawable.ic_add_profile_pic).into(holder.imageView);
        holder.id.setText(cVisit.getPatientId());
        holder.name.setText(cVisit.getName());
        holder.address.setText(cVisit.getAddress());


        //item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to patient activity within current patient initialized
                //initialize
                databaseReference = FirebaseDatabase.getInstance().getReference();

                holder.progress.setVisibility(View.VISIBLE);
                databaseReference.child("patients").child(cVisit.getPatientId()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            cPatient = dataSnapshot.getValue(Patient.class);
                            Common.currentPatient = cPatient;   //tracking current patient
                            if (Common.currentPatient != null) {
                                holder.progress.setVisibility(View.GONE);
                                context.startActivity(new Intent(context, PatientActivity.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imageView;
        private TextView id, name, address;
        private AVLoadingIndicatorView progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.patientImage);
            id = itemView.findViewById(R.id.patientId);
            name = itemView.findViewById(R.id.patientName);
            address = itemView.findViewById(R.id.patientAddress);
            progress = itemView.findViewById(R.id.progressIndicator);
        }
    }
}
