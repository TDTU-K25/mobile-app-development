package com.example.ex3.adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex3.MyDatabaseHelper;
import com.example.ex3.R;
import com.example.ex3.model.Event;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private final Context context;
    private final MyDatabaseHelper myDatabaseHelper; // temp solution should use Room instead
    private ArrayList<Event> dataSource;

    public CustomAdapter(Context context) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        this.context = context;
        setDataSourceFromDB();
    }

    public void setDataSourceFromDB() {
        this.dataSource = myDatabaseHelper.getAllEvents();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false));
    }

    public void addEvent(Event event) {
        myDatabaseHelper.addEvent(event); // add to db
        dataSource.add(event); // can use the setDataSourceFromDB() but performance bad
        notifyDataSetChanged();
    }

    public void removeEvent(int position) {
        myDatabaseHelper.removeEvent(getEvent(position).getId()); // remove from db
        dataSource.remove(position);
        notifyDataSetChanged();
    }

    public void removeAll() {
        myDatabaseHelper.removeAllEvent(); // remove from db
        dataSource.clear();
        notifyDataSetChanged();
    }

    public void showAllEvents() {
        setDataSourceFromDB();
        notifyDataSetChanged();
    }

    public void showEnabledEvents() {
        dataSource = myDatabaseHelper.getAllEnabledEvents();
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
                    myDatabaseHelper.updateEventStatus(event.getId(), event.getEnabled());
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
