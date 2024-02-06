package com.example.metube.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.metube.R;
import com.example.metube.model.Video;

import java.util.ArrayList;

public class SearchVideoAdapter extends RecyclerView.Adapter<SearchVideoAdapter.ViewHolder> implements Filterable {
    private final Context context;
    private final ArrayList<Video> dataSource;
    private final VideoAdapter.OnVideoClickListener onVideoClickListener;
    private ArrayList<Video> dataFiltered;

    public SearchVideoAdapter(Context context, ArrayList<Video> dataSource, VideoAdapter.OnVideoClickListener onVideoClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        this.dataFiltered = dataSource;
        this.onVideoClickListener = onVideoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(position, dataFiltered.get(position));
        holder.ivThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVideoClickListener != null) {
                    onVideoClickListener.onVideoClick(dataFiltered.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String keyword = constraint.toString();
                if (keyword.isEmpty()) {
                    dataFiltered = dataSource;
                } else {
                    ArrayList<Video> filteredList = new ArrayList<>();
                    for (Video video : dataSource) {
                        if (video.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                            filteredList.add(video);
                        }
                    }
                    dataFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataFiltered = (ArrayList<Video>) results.values;
                notifyDataSetChanged();
            }
        };
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
            tvShortInfo.setText(String.format("Figma - %d views - 7 days ago", video.getViews()));
        }
    }
}
