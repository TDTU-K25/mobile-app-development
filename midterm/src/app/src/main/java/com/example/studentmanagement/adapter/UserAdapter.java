package com.example.studentmanagement.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder> {

    private final OnUserClickListener onUserClickListener;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options, OnUserClickListener onUserClickListener) {
        super(options);
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_user, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
        holder.bindData(position, model);
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserClickListener.onEditUserClick(model.getId());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserClickListener.onDeleteUserClick(model.getId());
            }
        });

        holder.ivDetailedInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserClickListener.onSeeDetailedUserInfoClick(model.getId());
            }
        });
    }

    // this method is popular
    public interface OnUserClickListener {
        void onEditUserClick(String uid);

        void onDeleteUserClick(String uid);
        void onSeeDetailedUserInfoClick(String uid);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civAvatar;
        TextView tvName;
        TextView tvRole;
        ImageView ivDetailedInfo;
        ImageView ivEdit;
        ImageView ivDelete;
        View viewContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            civAvatar = itemView.findViewById(R.id.civ_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRole = itemView.findViewById(R.id.tv_role);
            ivDetailedInfo = itemView.findViewById(R.id.iv_detailed_info);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }

        public void bindData(int position, User user) {
            // set avatar
            tvName.setText(user.getName());
            tvRole.setText(user.getRole() == 0 ? "Employee" : "Manager");

            if (user.getAvatar() != null) {
                byte[] binaryData = Base64.getDecoder().decode(user.getAvatar());
                Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                civAvatar.setImageBitmap(bitmap);
            }
        }
    }
}