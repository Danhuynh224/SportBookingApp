package com.example.pjbookingsport.model;

public class PostModel {
    private int imgBackground;
    private String title;

    public PostModel(int imgBackground, String title) {
        this.imgBackground = imgBackground;
        this.title = title;
    }

    public int getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(int imgBackground) {
        this.imgBackground = imgBackground;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
