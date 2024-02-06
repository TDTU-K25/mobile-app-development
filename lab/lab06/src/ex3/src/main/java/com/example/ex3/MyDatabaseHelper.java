package com.example.ex3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ex3.model.Event;
import com.example.ex3.utils.LocalDateTimeUtil;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lab06_ex3.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "events";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ROOM = "room";
    private static final String COLUMN_DATETIME = "datetime";
    private static final String COLUMN_IS_ENABLED = "isEnabled";
    private final Context context;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)", TABLE_NAME, COLUMN_ID, COLUMN_TITLE, COLUMN_ROOM, COLUMN_DATETIME, COLUMN_IS_ENABLED);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s", TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), LocalDateTimeUtil.convertDateStringToLocalDateTime(cursor.getString(3)), cursor.getInt(4) == 1);
            eventList.add(event);
            cursor.moveToNext();
        }
        return eventList;
    }

    public ArrayList<Event> getAllEnabledEvents() {
        ArrayList<Event> enabledEventList = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE %s = 1", TABLE_NAME, COLUMN_IS_ENABLED);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2), LocalDateTimeUtil.convertDateStringToLocalDateTime(cursor.getString(3)), cursor.getInt(4) == 1);
            enabledEventList.add(event);
            cursor.moveToNext();
        }
        return enabledEventList;
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, event.getTitle());
        cv.put(COLUMN_ROOM, event.getRoom());
        cv.put(COLUMN_DATETIME, String.valueOf(event.getDatetime()));
        cv.put(COLUMN_IS_ENABLED, event.getEnabled());

        db.insert(TABLE_NAME, null, cv);
    }

    public void removeEvent(Integer eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(eventId)});
    }

    public void removeAllEvent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public void updateEventStatus(int eventId, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IS_ENABLED, isEnabled ? 1 : 0);

        db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(eventId)});
        db.close();
    }
}
