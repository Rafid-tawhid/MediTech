package com.hk.meditechauthenticate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hk.meditechauthenticate.DocumentViewActivity;
import com.hk.meditechauthenticate.Model.Document;
import com.hk.meditechauthenticate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDocument extends RecyclerView.Adapter<AdapterDocument.ViewHolder> {
    private List<Document> documentList;
    private Context context;
    private DatabaseReference databaseReference;
    private View getView;

    public AdapterDocument(List<Document> documentList, Context context) {
        this.documentList = documentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_layout, parent, false);
        getView = view;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Document cDocument = documentList.get(position);

        //load image
        Picasso.get().load(cDocument.getReportImage()).placeholder(R.drawable.ic_file).into(holder.reportImage);

        holder.reportName.setText(cDocument.getReportType());
        holder.generateDate.setText(cDocument.getGenerateDate());

        //full screen image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DocumentViewActivity.class);
                intent.putExtra("reportName",cDocument.getReportType());
                intent.putExtra("generateDate",cDocument.getGenerateDate());
                intent.putExtra("reportImage",cDocument.getReportImage());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reportName, generateDate;
        ImageView reportImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reportName = itemView.findViewById(R.id.reportName);
            generateDate = itemView.findViewById(R.id.dateTV);
            reportImage = itemView.findViewById(R.id.reportImage);

        }
    }


}
