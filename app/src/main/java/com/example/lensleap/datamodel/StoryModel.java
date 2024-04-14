package com.example.lensleap.datamodel;

public class StoryModel {
    private String username;
    private int image;

    public StoryModel(String username, int image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public int getImage() {
        return image;
    }
}
