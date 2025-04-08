package com.example.pjbookingsport.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Post {
    private Long postId;
    private String title;
    private String img;

    public Post(Long postId, String title, String img) {
        this.postId = postId;
        this.title = title;
        this.img = img;
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

    public List<String> getImg() {
        if (img == null || img.isEmpty()) return List.of();

        String[] fileNames = img.split(",");
        return Arrays.stream(fileNames)
                .map(name -> "http://10.0.2.2:8080/images/" + name.trim())
                .collect(Collectors.toList());
    }

    public void setImg(String img) {
        this.img = img;
    }
}
