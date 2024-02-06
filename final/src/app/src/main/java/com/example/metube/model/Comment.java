package com.example.metube.model;

import java.util.Date;

public class Comment {
    private String id;
    private String videoId;
    private String userId;
    private String content;
    private Date createdAt;

    public Comment() {
    }

    public Comment(String videoId, String userId, String content, Date createdAt) {
        this.videoId = videoId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Comment(String id, String videoId, String userId, String content, Date createdAt) {
        this.id = id;
        this.videoId = videoId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
