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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class VideoManagementAdapter extends FirestoreRecyclerAdapter<Video, VideoManagementAdapter.ViewHolder> {
    private final Context context;

    public interface OnVideoClickListener {
        void onVideoClick(Video video);
    }

    public VideoManagementAdapter(@NonNull FirestoreRecyclerOptions<Video> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Video model) {
        holder.bindData(position, model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_one_video_minisize, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvShortInfo;
        ImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvShortInfo = itemView.findViewById(R.id.tv_short_info);
            ivMore = itemView.findViewById(R.id.iv_more);
        }

        public void bindData(int position, Video video) {
            Glide.with(context).load(video.getUrl()).into(ivThumbnail);
            tvTitle.setText(video.getTitle());
            tvShortInfo.setText("Test");
        }
    }
}
