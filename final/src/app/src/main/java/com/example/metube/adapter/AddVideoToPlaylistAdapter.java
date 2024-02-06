package com.example.metube.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.model.Video;

import java.util.List;

public class AddVideoToPlaylistAdapter extends RecyclerView.Adapter<AddVideoToPlaylistAdapter.ViewHolder> {
    private final Context context;
    private final List<Video> dataSource;
    private final OnVideoClickListener onVideoClickListener;
    private RecycleViewClickInterface recycleViewOnclick;

    public AddVideoToPlaylistAdapter(Context context, List<Video> dataSource, RecycleViewClickInterface recycleViewOnclick,
                                     OnVideoClickListener onVideoClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        this.recycleViewOnclick = recycleViewOnclick;
        this.onVideoClickListener = onVideoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_video_checkbox, parent, false));
    }

    private Video getVideo(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(position, getVideo(position));
        Video video = dataSource.get(position);
        Glide.with(context).load(video.getThumbnail()).into(holder.ivThumbnail);
        holder.tvTitle.setText(video.getTitle());
        holder.tvShortInfo.setText(String.format("Figma - %d views - 7 days ago", video.getViews()));
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
        implements View.OnClickListener{
        View container;
        ImageView ivThumbnail;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvShortInfo;
        ImageView ivMore;
        CheckBox cbSelected;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvShortInfo = itemView.findViewById(R.id.tv_short_info);
            ivMore = itemView.findViewById(R.id.iv_more);
            cbSelected = itemView.findViewById(R.id.cb_add_video_to_playlist);

            cbSelected.setOnClickListener(this);

        }

        public void bindData(int position, Video video) {
            Glide.with(context).load(video.getUrl()).into(ivThumbnail);
            tvTitle.setText(video.getTitle());
        }

        @Override
        public void onClick(View view) {
            recycleViewOnclick.onRecycleViewClick(view,getAdapterPosition());
        }
    }
}
