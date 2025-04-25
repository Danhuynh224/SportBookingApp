package com.example.pjbookingsport.model;

import java.time.LocalDateTime;

public class ReviewRequest {
    private Long userId;
    private Long facilityId;
    private int rating;
    private String comment;

    public ReviewRequest(Long userId, Long facilityId, int rating, String comment) {
        this.userId = userId;
        this.facilityId = facilityId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
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

}
