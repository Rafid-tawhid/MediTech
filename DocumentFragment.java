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
import com.hk.meditechuser.Adapter.AdapterDocument;
import com.hk.meditechuser.Model.Document;
import com.hk.meditechuser.databinding.FragmentDocumentBinding;

import java.util.ArrayList;
import java.util.List;


public class DocumentFragment extends Fragment {
    private FragmentDocumentBinding binding;
    private DatabaseReference databaseReference;
    private List<Document> documentList;
    private AdapterDocument adapterDocument;
    private Context context;

    public DocumentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_document, container, false);
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
                .child("documents");
        documentList = new ArrayList<>();
        adapterDocument = new AdapterDocument(documentList, context);
        binding.documentRV.setLayoutManager(new LinearLayoutManager(context));
        binding.documentRV.setAdapter(adapterDocument);

        getDocument();

    }

    private void getDocument() {
        binding.progress.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    documentList.clear();
                    binding.emptyItemView.setVisibility(View.GONE);
                    for (DataSnapshot pSnapshot : dataSnapshot.getChildren()) {
                        Document document = pSnapshot.getValue(Document.class);
                        documentList.add(document);
                        adapterDocument.notifyDataSetChanged();
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
