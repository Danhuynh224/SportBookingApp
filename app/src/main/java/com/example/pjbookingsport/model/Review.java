package com.example.pjbookingsport.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class Review implements Serializable {
    private Long reviewId;
    private User user;
    private SportFacility facility;
    private int rating;
    private String comment;
    private Date createdAt;

    public Review(Long reviewId, User user, SportFacility facility, int rating, String comment, Date createdAt) {
        this.reviewId = reviewId;
        this.user = user;
        this.facility = facility;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SportFacility getFacility() {
        return facility;
    }

    public void setFacility(SportFacility facility) {
        this.facility = facility;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
