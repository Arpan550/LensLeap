package com.example.lensleap.datamodel;

public class MyPostModel {
    private String imageUrl;

    public MyPostModel() {
    }

    public MyPostModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
