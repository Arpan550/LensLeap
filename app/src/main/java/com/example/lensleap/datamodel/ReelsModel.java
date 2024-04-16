package com.example.lensleap.datamodel;

public class ReelsModel {
    private String uid; // User ID
    private String username;
    private String caption;
    private String profile_img; // Profile image URL
    private String reels_post; // Post video URL

    public ReelsModel() {
        // Empty constructor required for Firestore
    }

    public ReelsModel(String uid, String username, String caption, String profile_img, String reels_post) {
        this.uid = uid;
        this.username = username;
        this.caption = caption;
        this.profile_img = profile_img;
        this.reels_post = reels_post;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getReels_post() {
        return reels_post;
    }

    public void setReels_post(String reels_post) {
        this.reels_post = reels_post;
    }
}
