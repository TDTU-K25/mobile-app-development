package com.example.ex1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex1.R;
import com.example.ex1.model.Item;

import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Item> dataSource;

    public CustomAdapter(Context context) {
        this.context = context;
        this.dataSource = generateDataSet();
    }

    private Integer generateRandomNum() {
        // range [1, 50]
        return new Random().nextInt((49) + 1);
    }

    private ArrayList<Item> generateDataSet() {
        int capacity = generateRandomNum();
        ArrayList<Item> result = new ArrayList<>(capacity);
        for (int i = 1; i <= capacity; i++) {
            result.add(new Item("item " + i));
        }
        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    private Item getItem(int position) {
        return this.dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewContainer;
        private final TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvItem = itemView.findViewById(R.id.tv_item);
        }

        public void bindData(int position, Item item) {
            tvItem.setText(item.getTitle());
            viewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), tvItem.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
