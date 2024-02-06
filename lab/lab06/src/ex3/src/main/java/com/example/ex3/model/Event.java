package com.example.ex3.model;

import java.time.LocalDateTime;

public class Event {
    private Integer id;
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

    public Event(Integer id, String title, String room, LocalDateTime datetime, Boolean isEnabled) {
        this.id = id;
        this.title = title;
        this.room = room;
        this.datetime = datetime;
        this.isEnabled = isEnabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
