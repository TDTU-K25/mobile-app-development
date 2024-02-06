package com.example.metube.model;

import java.util.Date;
import java.util.List;

// true: public
// false: private
public class Playlist {
    private String id;
    private String userID;
    private String title;
    private Boolean state;
    private Date createAt;
    private Integer numOfVideo;
    private List<String> videos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Playlist(String id) {
        this.id = id;
    }

    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Playlist() {
    }

    public Playlist(String userID, String title, Boolean state, Date createAt, Integer numOfVideo, String thumbnail, List<String> videos) {
        this.userID = userID;
        this.title = title;
        this.state = state;
        this.createAt = createAt;
        this.numOfVideo = numOfVideo;
        this.thumbnail = thumbnail;
        this.videos = videos;
    }

    public Playlist(String id, String userID, String title, Boolean state, Date createAt, Integer numOfVideo, List<String> videos, String thumbnail) {
        this.id = id;
        this.userID = userID;
        this.title = title;
        this.state = state;
        this.createAt = createAt;
        this.numOfVideo = numOfVideo;
        this.videos = videos;
        this.thumbnail = thumbnail;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getNumOfVideo() {
        return numOfVideo;
    }

    public void setNumOfVideo(Integer numOfVideo) {
        this.numOfVideo = numOfVideo;
    }
}