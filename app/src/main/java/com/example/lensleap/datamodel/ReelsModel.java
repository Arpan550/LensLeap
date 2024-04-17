package com.example.lensleap.datamodel;

public class ReelsModel {
    private String reels_post;
    private String caption;
    private String uid;
    private String username;
    private String profile_img;
    private boolean isPaused; // Flag to track if the video should start in paused mode

    public ReelsModel() {
        // Default constructor required for Firestore
    }

    public ReelsModel(String reels_post, String caption, String uid, String username, String profile_img) {
        this.reels_post = reels_post;
        this.caption = caption;
        this.uid = uid;
        this.username = username;
        this.profile_img = profile_img;
        this.isPaused = true; // By default, video starts in paused mode
    }

    public String getReels_post() {
        return reels_post;
    }

    public void setReels_post(String reels_post) {
        this.reels_post = reels_post;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
