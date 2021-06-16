package com.hk.meditechauthenticate.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.hk.meditechauthenticate.DetailHealthSheet;
import com.hk.meditechauthenticate.Model.Health;
import com.hk.meditechauthenticate.R;


import java.util.List;

public class AdapterHealth extends RecyclerView.Adapter<AdapterHealth.ViewHolder> {
    private Context context;
    private List<Health> healthList;






    public AdapterHealth(Context context, List<Health> healthList) {
        this.context = context;
        this.healthList = healthList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Health cHealth = healthList.get(position);

        //layout process
        holder.name.setText(cHealth.getDeasesName());
        holder.type.setText(cHealth.getReportType());
        holder.value.setText(cHealth.getLabResult());
        holder.condition.setText(cHealth.getCondition());

        //set condition color
        if(cHealth.getCondition().equals("Normal")){
            holder.condition.setTextColor(Color.GREEN);
        }
        else{
            holder.condition.setTextColor(Color.RED);
        }


        //item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //details bottom sheet

                DetailHealthSheet detailHealthSheet = new DetailHealthSheet(
                        cHealth.getDeasesName(),
                        cHealth.getReportType(),
                        cHealth.getLabResult(),
                        cHealth.getComment(),
                        cHealth.getExamineDate(),
                        cHealth.getCondition()
                );
                detailHealthSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "Health Details");
            }
        });


    }




    @Override
    public int getItemCount() {
        return healthList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, type, value, condition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.deasesName);
            type = itemView.findViewById(R.id.reportType);
            value = itemView.findViewById(R.id.resultValue);
            condition = itemView.findViewById(R.id.conditionType);

        }
    }

}
