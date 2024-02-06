package com.example.studentmanagement.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.model.Certificate;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class CertificateAdapter extends FirestoreRecyclerAdapter<Certificate, CertificateAdapter.ViewHolder> {
    private final OnCertificateClickListener certificateClickListener;

    public CertificateAdapter(@NonNull FirestoreRecyclerOptions<Certificate> options, OnCertificateClickListener certificateClickListener) {
        super(options);
        this.certificateClickListener = certificateClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_certificate, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Certificate model) {
        holder.bindData(position, model);
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificateClickListener.onEditCertificateClick(model.getId());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificateClickListener.onDeleteCertificateClick(model.getId());
            }
        });
    }

    public interface OnCertificateClickListener {
        void onEditCertificateClick(String uid);

        void onDeleteCertificateClick(String uid);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject;
        TextView tvOrganization;
        TextView tvCompletionDate;
        ImageView ivEdit;
        ImageView ivDelete;
        View viewContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;

            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvOrganization = itemView.findViewById(R.id.tv_organization);
            tvCompletionDate = itemView.findViewById(R.id.tv_completion_date);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }

        public void bindData(int position, Certificate certificate) {
            tvSubject.setText(certificate.getSubject());
            tvOrganization.setText(certificate.getOrganization());
            tvCompletionDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(certificate.getCompletionDate()).toString());
        }
    }
}
