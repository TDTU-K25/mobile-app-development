package com.example.ex5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex5.R;
import com.example.ex5.model.User;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<User> dataSource;

    public CustomAdapter(Context context) {
        this.context = context;
        this.dataSource = new ArrayList<>();
    }

    public void addFiveUsers() {
        int currentSize = getItemCount();
        for (int i = currentSize + 1; i <= currentSize + 5; i++) {
            dataSource.add(new User("User " + i, "user" + i + "@tdtu.edu.vn"));
        }
        notifyItemRangeInserted(getItemCount() + 1, 5);
    }

    public void removeLastFiveUsers() {
        int currentSize = getItemCount();
        if (currentSize >= 5) {
            for (int i = currentSize - 1; i >= currentSize - 5; i--) {
                dataSource.remove(i);
            }
            notifyItemRangeRemoved(currentSize - 5, 5);
        } else if (currentSize == 0) {
            Toast.makeText(context, "List of users is empty", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    private User getUser(int position) {
        return this.dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position, getUser(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View viewContainer;
        private final TextView tvUsername;
        private final TextView tvEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvEmail = itemView.findViewById(R.id.tv_email);
        }

        public void bindData(int position, User user) {
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
        }
    }
}
