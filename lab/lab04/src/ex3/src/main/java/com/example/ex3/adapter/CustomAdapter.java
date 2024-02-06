package com.example.ex3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex3.R;
import com.example.ex3.model.Brand;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Brand> dataSource;

    public CustomAdapter(Context context, ArrayList<Brand> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    public void removeAll() {
        int currentSize = getItemCount();
        dataSource.clear();
        notifyItemRangeRemoved(0, currentSize);
    }

    public void removeSelected() {
        int currentSize = getItemCount();
        ArrayList<Brand> selectedBrands = new ArrayList<>();
        for (int i = 0; i < currentSize; i++) {
            if (dataSource.get(i).getSelected()) {
                selectedBrands.add(dataSource.get(i));
            }
        }
        for (Brand brand : selectedBrands) {
            dataSource.remove(brand);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    private Brand getBrand(int position) {
        return this.dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position, getBrand(position));
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewContainer;
        private final TextView tvItem;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvItem = itemView.findViewById(R.id.tv_item);
            checkBox = itemView.findViewById(R.id.cb);
        }

        public void bindData(int position, Brand brand) {
            tvItem.setText(brand.getName());
            checkBox.setChecked(brand.getSelected());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    brand.setSelected(!brand.getSelected());
                    notifyItemChanged(position);
                }
            });
        }
    }
}
