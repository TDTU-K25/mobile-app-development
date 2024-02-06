package com.example.metube.model;

import java.util.Date;

public class Video {
    private String id;
    private String title;
    private Integer views;
    private Date publishDate;
    private Date duration; // date because store in firestore is timestamp
    private String url;

    private String description;

    private String thumbnail;
    private String channelId; // user uuid

    public Video() {
    }

    public Video(String title, Integer views, Date publishDate, Date duration, String thumbnail) {
        this.title = title;
        this.views = views;
        this.publishDate = publishDate;
        this.duration = duration;
        this.thumbnail = thumbnail;
    }

    public Video(String id, String title, Integer views, Date publishDate, Date duration, String url, String thumbnail) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.publishDate = publishDate;
        this.duration = duration;
        this.url = url;
        this.thumbnail = thumbnail;
    }



    public Video(String title, Integer views, Date publishDate, Date duration, String url, String description, String channelId) {
        this.title = title;
        this.views = views;
        this.publishDate = publishDate;
        this.duration = duration;
        this.url = url;
        this.description = description;
        this.channelId = channelId;
    }

    public Video(String id, String title, Integer views, Date publishDate, Date duration, String url, String description, String thumbnail, String channelId) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.publishDate = publishDate;
        this.duration = duration;
        this.url = url;
        this.thumbnail = thumbnail;
        this.description = description;
        this.channelId = channelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
