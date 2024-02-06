package com.example.metube.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.Interface.RecycleViewClickInterface;
import com.example.metube.R;
import com.example.metube.model.Playlist;
import com.example.metube.model.User;
import com.example.metube.model.Video;

import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {
    private final Context context;
    private final List<User> dataSource;
    private RecycleViewClickInterface recycleViewOnclick;

    public ChannelAdapter(Context context, List<User> dataSource, RecycleViewClickInterface recycleViewOnclick) {
        this.context = context;
        this.dataSource = dataSource;
        this.recycleViewOnclick = recycleViewOnclick;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_channel, parent, false));
    }

    private User getVideo(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = dataSource.get(position);
        if (user.getAvatar() != null) {
            Glide.with(context).load(user.getAvatar()).into(holder.ivAvatar);
        }
        else {
            Drawable res = context.getResources().getDrawable(R.drawable.account_circle_fill0_wght400_grad0_opsz24);
            holder.ivAvatar.setImageDrawable(res);
        }
        holder.tvTitle.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        View container;
        ImageView ivAvatar;
        TextView tvTitle;
        ImageView ivMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;

            ivAvatar = itemView.findViewById(R.id.iv_avatar_channel);
            tvTitle = itemView.findViewById(R.id.tv_title);
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
    }
}
