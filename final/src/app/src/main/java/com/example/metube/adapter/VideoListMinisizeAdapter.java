package com.example.metube.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.model.Video;

import java.util.List;

public class VideoListMinisizeAdapter extends RecyclerView.Adapter<VideoListMinisizeAdapter.ViewHolder> {
    private final Context context;
    private final List<Video> dataSource;
    private final OnVideoClickListener onVideoClickListener;
    private RecycleViewClickInterface recycleViewOnclick;
    private Boolean isHorizontal;
    private ImageView ivMore;

    public VideoListMinisizeAdapter(Context context, List<Video> dataSource, Boolean isHorizontal) {
        this.context = context;
        this.dataSource = dataSource;
        this.onVideoClickListener = null;
        this.isHorizontal = isHorizontal;
    }
    public VideoListMinisizeAdapter(Context context, List<Video> dataSource, OnVideoClickListener onVideoClickListener,
                                    Boolean isHorizontal) {
        this.context = context;
        this.dataSource = dataSource;
        this.onVideoClickListener = null;
        this.isHorizontal = isHorizontal;
    }

    public VideoListMinisizeAdapter(Context context, List<Video> dataSource, OnVideoClickListener onVideoClickListener,
                                    RecycleViewClickInterface recycleViewOnclick, Boolean isHorizontal) {
        this.context = context;
        this.dataSource = dataSource;
        this.onVideoClickListener = onVideoClickListener;
        this.recycleViewOnclick = recycleViewOnclick;
        this.isHorizontal = isHorizontal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isHorizontal)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_video_horizontal, parent, false));
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_video_minisize, parent, false));
    }

    private Video getVideo(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(position, getVideo(position));
        holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(getVideo(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnVideoClickListener {
        void onVideoClick(String id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
        View container;
        ImageView ivThumbnail;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);

            ivMore = itemView.findViewById(R.id.iv_more);
            if(!isHorizontal) ivMore.setOnClickListener(this);
        }

        public void bindData(int position, Video video) {
            Glide.with(context).load(video.getUrl()).into(ivThumbnail);
            tvTitle.setText(video.getTitle());
        }

        @Override
        public boolean onLongClick(View v) {
            recycleViewOnclick.onRecycleViewLongClick(v, getAdapterPosition());
            return  true;
        }

        @Override
        public void onClick(View v) {
            recycleViewOnclick.onRecycleViewClick(v, getAdapterPosition());
        }
    }
}
