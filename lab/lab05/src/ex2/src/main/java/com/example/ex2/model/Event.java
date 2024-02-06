package com.example.ex2.model;

import java.time.LocalDateTime;

public class Event {
    private String title;
    private String room;
    private LocalDateTime datetime;
    private Boolean isEnabled;

    public Event(String title, String room, LocalDateTime datetime) {
        this.title = title;
        this.room = room;
        this.datetime = datetime;
        this.isEnabled = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}
