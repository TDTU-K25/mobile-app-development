package com.example.ex2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2.R;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private final ArrayList<String> dataSource;
    private final Context context;

    private final OnCountryClickListener onCountryClickListener;

    public CountryAdapter(ArrayList<String> dataSource, Context context, OnCountryClickListener onCountryClickListener) {
        this.dataSource = dataSource;
        this.context = context;
        this.onCountryClickListener = onCountryClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position, dataSource.get(position));
        holder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCountryClickListener.onCountryClick(dataSource.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnCountryClickListener {
        void onCountryClick(String countryName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountryName;
        View viewContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewContainer = itemView;
            tvCountryName = itemView.findViewById(R.id.tv_country_name);
        }

        public void bind(int position, String countryName) {
            tvCountryName.setText(countryName);
        }
    }
}
