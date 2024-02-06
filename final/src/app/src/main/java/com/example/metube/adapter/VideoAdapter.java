package com.example.metube.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.R;
import com.example.metube.model.Video;
import com.example.metube.utils.TimeUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class VideoAdapter extends FirestoreRecyclerAdapter<Video, VideoAdapter.ViewHolder> {
    private final Context context;
    private final OnVideoClickListener onVideoClickListener;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public VideoAdapter(@NonNull FirestoreRecyclerOptions<Video> options, Context context, OnVideoClickListener onVideoClickListener) {
        super(options);
        this.context = context;
        this.onVideoClickListener = onVideoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_video, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {
        holder.bindData(position, model);
        holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(model.getId());
                }
            }
        });
    }

    public interface OnVideoClickListener {
        void onVideoClick(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View container;
        ImageView ivThumbnail;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvShortInfo;
        ImageView ivMore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvShortInfo = itemView.findViewById(R.id.tv_short_info);
            ivMore = itemView.findViewById(R.id.iv_more);
        }

        public void bindData(int position, Video video) {
            Glide.with(context).load(video.getUrl()).into(ivThumbnail);
            tvTitle.setText(video.getTitle());

            firestore.collection(context.getString(R.string.users_collection)).document(video.getChannelId()).get().addOnSuccessListener(documentSnapshot -> {
                Glide.with(context).load(documentSnapshot.getString("avatar")).into(ivAvatar);
                tvShortInfo.setText(String.format("%s - %d views - %s", documentSnapshot.get("name"), video.getViews(), TimeUtil.getTimeDistance(video.getPublishDate())));
            });
        }
    }
}
