package com.example.ex2.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2.R;
import com.example.ex2.model.Event;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Event> dataSource;
    private ArrayList<Event> dataSourceCopy; // temp solution for filter

    public CustomAdapter(Context context, ArrayList<Event> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
        this.dataSourceCopy = new ArrayList<>(dataSource);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    public void addEvent(Event event) {
        dataSource.add(event);
        notifyDataSetChanged();
        dataSourceCopy = new ArrayList<>(dataSource);
    }

    public void removeEvent(int position) {
        dataSource.remove(position);
        notifyDataSetChanged();
        dataSourceCopy = new ArrayList<>(dataSource);
    }

    public void removeAll() {
        dataSource.clear();
        notifyDataSetChanged();
    }

    public void showAllEvents() {
        dataSource = new ArrayList<>(dataSourceCopy);
        notifyDataSetChanged();
    }

    public void showEnabledEvents() {
        dataSourceCopy = new ArrayList<>(dataSource);
        ArrayList<Event> enabledEvents = new ArrayList<>();
        for (int i = 0; i < dataSource.size(); i++) {
            if (getEvent(i).getEnabled()) {
                enabledEvents.add(getEvent(i));
            }
        }
        dataSource = enabledEvents;
        notifyDataSetChanged();
    }

    private Event getEvent(int position) {
        return dataSource.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position, getEvent(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        View viewContainer;
        TextView tvTitle;
        TextView tvRoom;
        TextView tvDatetime;
        SwitchCompat switchEnabledEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewContainer = itemView;
            tvTitle = itemView.findViewById(R.id.tv_event_title);
            tvRoom = itemView.findViewById(R.id.tv_room);
            tvDatetime = itemView.findViewById(R.id.tv_datetime);
            switchEnabledEvent = itemView.findViewById(R.id.switch_isEnabled_event);

            itemView.setOnCreateContextMenuListener(this);
        }

        public void bindData(int position, Event event) {
            tvTitle.setText(event.getTitle());
            tvRoom.setText(event.getRoom());
            tvDatetime.setText(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(event.getDatetime()));
            switchEnabledEvent.setChecked(event.getEnabled());

            switchEnabledEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    event.setEnabled(!event.getEnabled());
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), R.id.delete_item, 0, "Delete");
            menu.add(this.getAdapterPosition(), R.id.edit_item, 0, "Edit");
        }
    }
}
