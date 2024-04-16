package com.example.lensleap.datamodel;

public class MyReelsModel {
    private String videoUrl;

    public MyReelsModel() {
    }

    public MyReelsModel(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
