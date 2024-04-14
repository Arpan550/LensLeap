package com.example.lensleap.datamodel;

public class PostModel {
    private String username;
    private int profile_img, post_img;

    public PostModel(int post_img) {
        this.post_img = post_img;
    }

    public PostModel(String username, int profile_img, int post_img) {
        this.username = username;
        this.profile_img = profile_img;
        this.post_img = post_img;
    }
    public String getUsername() {
        return username;
    }
    public int getProfile_img() {
        return profile_img;
    }
    public int getPost_img() {
        return post_img;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    public void setPost_img(int post_img) {
        this.post_img = post_img;
    }
}

