package com.example.ex4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex4.R;
import com.example.ex4.model.PC;

import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<PC> dataSource;

    public CustomAdapter(Context context) {
        this.context = context;
        this.dataSource = generateDataSet();
    }

    private Integer generateRandomNum() {
        // range [10, 100]
        return new Random().nextInt((101 - 10) + 10);
    }

    private ArrayList<PC> generateDataSet() {
        int capacity = generateRandomNum();
        ArrayList<PC> result = new ArrayList<>(capacity);
        for (int i = 1; i <= capacity; i++) {
            result.add(new PC("PC " + i));
        }
        return result;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    private PC getPC(int position) {
        return this.dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position, getPC(position));
    }

    @Override
    public int getItemCount() {
        return this.dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewContainer;
        private final ImageView iv;
        private final TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tv = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
        }

        public void bindData(int position, PC pc) {
            tv.setText(pc.getId());
            iv.setImageResource(pc.getStatus());

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pc.getStatus() == R.drawable.on) {
                        pc.setStatus(R.drawable.off);
                    } else if (pc.getStatus() == R.drawable.off) {
                        pc.setStatus(R.drawable.on);
                    }
                    notifyItemChanged(position);
                }
            });
        }
    }
}
