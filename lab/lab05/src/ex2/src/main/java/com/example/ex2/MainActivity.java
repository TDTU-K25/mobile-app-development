package com.example.ex2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ex2.adapter.CustomAdapter;
import com.example.ex2.model.Event;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv_events);

        // set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // set apdater
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Sinh hoat chu nhiem", "C120", LocalDateTime.of(2020, Month.MARCH, 9, 4, 43)));
        events.add(new Event("Huong dan luan van", "C120", LocalDateTime.of(2020, Month.MARCH, 9, 4, 43)));
        adapter = new CustomAdapter(this, events);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem switchCompatItem = menu.findItem(R.id.sw_enabled_or_disabled_event);
        SwitchCompat switchCompatView = (SwitchCompat) MenuItemCompat.getActionView(switchCompatItem);
        switchCompatView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.showAllEvents();
                } else {
                    adapter.showEnabledEvents();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.remove_all) {
            showConfirmRemoveAllDialog();
        } else if (itemId == R.id.add_event) {
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String place = data.getStringExtra("place");
                String date = data.getStringExtra("date");
                String time = data.getStringExtra("time");

                String[] dateParts = date.split("-");
                String[] timeParts = time.split(":");

                Integer day = Integer.parseInt(dateParts[0]);
                Integer month = Integer.parseInt(dateParts[1]);
                Integer year = Integer.parseInt(dateParts[2]);

                Integer hour = Integer.parseInt(timeParts[0]);
                Integer minute = Integer.parseInt(timeParts[1]);

                Event event = new Event(name, place, LocalDateTime.of(year, month, day, hour, minute));
                adapter.addEvent(event);
            }
        }
    }

    private void showConfirmRemoveAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove all?").setMessage("Are you sure to remove all events?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.removeAll();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.delete_item) {
            adapter.removeEvent(item.getGroupId());
        } else if (itemId == R.id.edit_item) {
        }
        return super.onContextItemSelected(item);
    }
}