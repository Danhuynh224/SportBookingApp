package com.example.pjbookingsport.model;

public class Post {
    private Long postId;
    private String title;
    private String imgBackground;

    public Post(Long postId, String title, String imgBackground) {
        this.postId = postId;
        this.title = title;
        this.imgBackground = imgBackground;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgBackground() {
        return imgBackground;
    }

    public void setImgBackground(String imgBackground) {
        this.imgBackground = imgBackground;
    }
}
