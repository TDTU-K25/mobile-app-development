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
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.model.Playlist;
import com.example.metube.model.Video;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private final Context context;
    private final List<Playlist> dataSource;
    private RecycleViewClickInterface recycleViewOnclick;

    public PlaylistAdapter(Context context, List<Playlist> dataSource, RecycleViewClickInterface recycleViewOnclick) {
        this.context = context;
        this.dataSource = dataSource;
        this.recycleViewOnclick = recycleViewOnclick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_video_minisize, parent, false));
    }

    private Playlist getVideo(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = dataSource.get(position);
        Glide.with(context).load(playlist.getThumbnail()).into(holder.ivThumbnail);
        holder.tvTitle.setText(playlist.getTitle());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
        View container;
        ImageView ivThumbnail;
        ImageView ivAvatar;
        TextView tvTitle;
        TextView tvShortInfo;
        ImageView ivMore;
        ImageView ivAddPlaylist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;

            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvShortInfo = itemView.findViewById(R.id.tv_short_info);
            ivMore = itemView.findViewById(R.id.iv_more);

            ivMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recycleViewOnclick.onRecycleViewClick(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            recycleViewOnclick.onRecycleViewLongClick(v, getAdapterPosition());
            return  true;
        }
//        public void bindData(int position, Playlist playlist) {
//            Glide.with(context).load(playlist.getThumbnail()).into(ivThumbnail);
//            tvTitle.setText(playlist.getTitle());
//            tvShortInfo.setText(String.format("Last update: ", playlist.getUpdateAt()));
//        }
        }
}
