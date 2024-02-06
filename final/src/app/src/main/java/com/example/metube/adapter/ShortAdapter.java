package com.example.metube.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.R;
import com.example.metube.model.Video;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ShortAdapter extends RecyclerView.Adapter<ShortAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Video> dataSource;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ShortAdapter(Context context, ArrayList<Video> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_short, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(position, dataSource.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View container;
        VideoView vvShort;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView;
            vvShort = itemView.findViewById(R.id.videoView);
            ivAvatar = itemView.findViewById(R.id.shortsImage);
            tvTitle = itemView.findViewById(R.id.shortsTitle);
            tvUserName = itemView.findViewById(R.id.shortsUser);
        }

        public void bindData(int position, Video video) {
            firestore.collection("Users").document(video.getChannelId()).get().addOnSuccessListener(documentSnapshot -> {
                Glide.with(context).load(documentSnapshot.getString("avatar")).into(ivAvatar);
                tvUserName.setText(documentSnapshot.getString("name"));
            });
            tvTitle.setText(video.getTitle());
            vvShort.setVideoPath(video.getUrl());
            vvShort.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    float screenRatio = vvShort.getWidth() / (float) vvShort.getHeight();
                }
            });

            vvShort.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    vvShort.start();
                }
            });
        }
    }
}