package com.example.metube.model;

import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private boolean isPrivate;
    private String avatar;
    private ArrayList<String> likedVideos;
    private ArrayList<String> playlists;
    private ArrayList<String> subscribedChannels;
    private ArrayList<String> watched;

    public User() {
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.likedVideos = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.subscribedChannels = new ArrayList<>();
        this.watched = new ArrayList<>();
    }

    public User(String id, String name, String phoneNumber, String email, String password, boolean isPrivate, String avatar, ArrayList<String> likedVideos, ArrayList<String> playlists, ArrayList<String> subscribedChannels) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isPrivate = isPrivate;
        this.avatar = avatar;
        this.likedVideos = likedVideos;
        this.playlists = playlists;
        this.subscribedChannels = subscribedChannels;
        this.watched = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<String> getLikedVideos() {
        return likedVideos;
    }

    public void setLikedVideos(ArrayList<String> likedVideos) {
        this.likedVideos = likedVideos;
    }

    public ArrayList<String> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<String> playlists) {
        this.playlists = playlists;
    }

    public ArrayList<String> getSubscribedChannels() {
        return subscribedChannels;
    }

    public void setSubscribedChannels(ArrayList<String> subscribedChannels) {
        this.subscribedChannels = subscribedChannels;
    }

    public ArrayList<String> getWatched() {
        return watched;
    }

    public void setWatched(ArrayList<String> watched) {
        this.watched = watched;
    }
}
