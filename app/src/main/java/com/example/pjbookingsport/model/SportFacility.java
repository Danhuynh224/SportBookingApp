package com.example.pjbookingsport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SportFacility implements Serializable {
    private Long sportsFacilityId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String img;
    private Date createdAt;
    private List<Price> prices = new ArrayList<>();
    private String detail;
    private List<Review> reviews = new ArrayList<>();

    public SportFacility() {
    }

    public SportFacility(Long sportsFacilityId, String name, String address, double latitude, double longitude, String img, Date createdAt, List<Price> prices, String detail, List<Review> reviews) {
        this.sportsFacilityId = sportsFacilityId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.img = img;
        this.createdAt = createdAt;
        this.prices = prices;
        this.detail = detail;
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }


    public Long getSportsFacilityId() {
        return sportsFacilityId;
    }

    public void setSportsFacilityId(Long sportsFacilityId) {
        this.sportsFacilityId = sportsFacilityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        double average = sum / reviews.size();
        return Math.round(average * 10.0) / 10.0;
    }

    public int getReviewCount() {
        return reviews != null ? reviews.size() : 0;
    }


}
