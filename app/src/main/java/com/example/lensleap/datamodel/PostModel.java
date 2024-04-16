package com.example.lensleap.datamodel;

public class PostModel {
    private String uid; // User ID
    private String username;
    private String caption;
    private String profile_img; // Profile image URL
    private String post_img; // Post image URL

    public PostModel() {
        // Empty constructor required for Firestore
    }

    public PostModel(String uid, String username, String caption, String profile_img, String post_img) {
        this.uid = uid;
        this.username = username;
        this.caption = caption;
        this.profile_img = profile_img;
        this.post_img = post_img;
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

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }
}
