package com.example.ex1.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex1.R;
import com.example.ex1.model.ContactInfo;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private final Context context;
    private final OnContactClickListener onContactClickListener;
    private ArrayList<ContactInfo> dataSource;
    private ArrayList<ContactInfo> dataSourceClone;

    public ContactAdapter(Context context, ArrayList<ContactInfo> dataSource, OnContactClickListener onContactClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        cloneDatasource();
        this.onContactClickListener = onContactClickListener;
    }

    public void resetDatasource() {
        this.dataSource = new ArrayList<>(dataSourceClone);
    }

    private void cloneDatasource() {
        this.dataSourceClone = new ArrayList<>(dataSource);
    }

    public void addContact(ContactInfo contactInfo) {
        this.dataSource.add(contactInfo);
        cloneDatasource();
        notifyItemInserted(dataSource.size() - 1);
    }

    public void search(String keyword) {
        ArrayList<ContactInfo> filteredList = new ArrayList<>();
        for (ContactInfo contactInfo : dataSource) {
            if (contactInfo.getFullName().toLowerCase().contains(keyword.toLowerCase()) || contactInfo.getPhoneNumber().contains(keyword)) {
                filteredList.add(contactInfo);
            }
        }
        dataSource.clear();
        dataSource.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_contact, parent, false));
    }

    public ContactInfo getContactInfo(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(position, getContactInfo(position));
        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContactClickListener.onContactClick(getContactInfo(position).getPhoneNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnContactClickListener {
        void onContactClick(String phoneNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View viewContainer;
        TextView etName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            etName = itemView.findViewById(R.id.et_name);
        }

        public void bindData(int position, ContactInfo contactInfo) {
            etName.setText(contactInfo.getFullName());
        }
    }
}
