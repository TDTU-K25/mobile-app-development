package com.example.metube.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.R;
import com.example.metube.model.Comment;
import com.example.metube.utils.TimeUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {
    private final OnCommentClickListener onCommentClickListener;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, OnCommentClickListener onCommentClickListener) {
        super(options);
        this.onCommentClickListener = onCommentClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_comment, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        holder.bindData(position, model);
        holder.ivDeleteCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentClickListener.onDeleteCommentClick(model.getId());
            }
        });
        holder.ivEditCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommentClickListener.onEditCommentClick(model.getId());
            }
        });
    }

    public interface OnCommentClickListener {
        void onEditCommentClick(String cmtId);

        void onDeleteCommentClick(String cmtId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvContent, tvCmtDate;
        ImageView ivEditCmt, ivDeleteCmt;
        View viewContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            ivAvatar = itemView.findViewById(R.id.iv_avatar_cmt);
            tvName = itemView.findViewById(R.id.tv_channel_name_cmt);
            tvContent = itemView.findViewById(R.id.tv_comment_content);
            tvCmtDate = itemView.findViewById(R.id.tv_cmt_date);
            ivEditCmt = itemView.findViewById(R.id.iv_edit_cmt);
            ivDeleteCmt = itemView.findViewById(R.id.iv_delete_cmt);
        }

        public void bindData(int position, Comment comment) {
            firestore.collection("Users").document(comment.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
                Glide.with(viewContainer.getContext()).load(documentSnapshot.getString("avatar")).into(ivAvatar);
                tvName.setText(documentSnapshot.getString("name"));
            });

            tvName.setText(comment.getUserId());
            tvContent.setText(comment.getContent());
            tvCmtDate.setText(TimeUtil.getTimeDistance(comment.getCreatedAt()));
        }
    }
}