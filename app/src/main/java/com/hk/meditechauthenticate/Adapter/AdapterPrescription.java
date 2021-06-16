package com.hk.meditechauthenticate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.hk.meditechauthenticate.Model.Prescription;
import com.hk.meditechauthenticate.PrescriptionViewActivity;
import com.hk.meditechauthenticate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPrescription extends RecyclerView.Adapter<AdapterPrescription.ViewHolder> {
    private List<Prescription> prescriptionList;
    private Context context;


    public AdapterPrescription(List<Prescription> prescriptionList, Context context) {
        this.prescriptionList = prescriptionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Prescription cPrescription = prescriptionList.get(position);

        //load image
        Picasso.get().load(cPrescription.getPrescriptionImage()).placeholder(R.drawable.ic_file).into(holder.prescriptionIV);

        holder.drName.setText(cPrescription.getDoctorName());
        holder.drDesignation.setText(cPrescription.getDoctorDesignation());
        holder.prescribeDate.setText(cPrescription.getPrescribeDate());

        //full screen image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PrescriptionViewActivity.class);
                intent.putExtra("doctorName", cPrescription.getDoctorName());
                intent.putExtra("prescribeDate", cPrescription.getPrescribeDate());
                intent.putExtra("prescriptionImage", cPrescription.getPrescriptionImage());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView prescriptionIV;
        private TextView drName, drDesignation, prescribeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drName = itemView.findViewById(R.id.doctorName);
            drDesignation = itemView.findViewById(R.id.doctorDesignation);
            prescribeDate = itemView.findViewById(R.id.prescribeDateTV);
            prescriptionIV = itemView.findViewById(R.id.prescriptionImage);

        }
    }


}
