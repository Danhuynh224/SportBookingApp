package com.example.pjbookingsport.model;

import com.example.pjbookingsport.R;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Post implements Serializable {
    private Long postId;
    private String title;

    private String summary;
    private String content;
    private String img;

    public Post(Long postId, String title, String summary, String content, String img) {
        this.postId = postId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

        String imgUrl = String.valueOf(R.string.img_url_post);

        String[] fileNames = img.split(",");
        return Arrays.stream(fileNames)
                .map(name -> imgUrl + name.trim())
                .collect(Collectors.toList());
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
