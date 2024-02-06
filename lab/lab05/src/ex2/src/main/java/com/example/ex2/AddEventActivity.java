package com.example.ex2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    EditText etName;
    EditText etPlace;
    EditText etDate;
    EditText etTime;

    Integer dayPreviousChosen;
    Integer monthPreviousChosen;
    Integer yearPreviousChosen;
    Integer hourPreviousChosen;
    Integer minutePreviousChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etName = findViewById(R.id.et_name);
        etPlace = findViewById(R.id.et_place);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);

        etPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPlaceDialog();
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateDialog();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTimePicker();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (validateData()) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra("name", etName.getText().toString());
                replyIntent.putExtra("place", etPlace.getText().toString());
                replyIntent.putExtra("date", etDate.getText().toString());
                replyIntent.putExtra("time", etTime.getText().toString());
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectPlaceDialog() {
        String[] choices = {"C201", "C202", "C203", "C204"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select place").setSingleChoiceItems(choices, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etPlace.setText(choices[which]);
                dialog.cancel();
            }
        }).create().show();
    }

    private void selectDateDialog() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dayPreviousChosen = dayOfMonth;
                monthPreviousChosen = month;
                yearPreviousChosen = year;
                etDate.setText(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.of(year, month + 1, dayOfMonth)));
            }
        };

        DatePickerDialog dialog;
        if (dayPreviousChosen != null && monthPreviousChosen != null && yearPreviousChosen != null) {
            dialog = new DatePickerDialog(this, onDateSetListener, yearPreviousChosen, monthPreviousChosen, dayPreviousChosen);
        } else {
            dialog = new DatePickerDialog(this, onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
        dialog.show();

    }

    private void selectTimePicker() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourPreviousChosen = hourOfDay;
                minutePreviousChosen = minute;
                etTime.setText(DateTimeFormatter.ofPattern("HH:mm").format(LocalTime.of(hourOfDay, minute)));
            }
        };
        TimePickerDialog dialog;
        if (hourPreviousChosen != null && minutePreviousChosen != null) {
            dialog = new TimePickerDialog(this, onTimeSetListener, hourPreviousChosen, minutePreviousChosen, true);
        } else {
            dialog = new TimePickerDialog(this, onTimeSetListener, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        }
        dialog.show();
    }

    private boolean validateData() {
        boolean isSatisfied = false;
        if (etName.getText().toString().isEmpty()) {
            etName.setError("Please enter event name");
        } else if (etPlace.getText().toString().isEmpty()) {
            etPlace.setError("Please enter event place");
        } else if (etDate.getText().toString().isEmpty()) {
            etDate.setError("Please enter event date");
        } else if (etTime.getText().toString().isEmpty()) {
            etTime.setError("Please enter event time");
        } else {
            isSatisfied = true;
            etName.setError(null);
            etPlace.setError(null);
            etDate.setError(null);
            etTime.setError(null);
        }
        return isSatisfied;
    }
}