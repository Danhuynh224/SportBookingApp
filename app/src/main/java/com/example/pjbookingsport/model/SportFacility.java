package com.example.pjbookingsport.model;

import java.util.Date;

public class SportFacility {
    private int sportsFacilityId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String img;
    private Date createdAt;

    public SportFacility(int sportsFacilityId, String name, String address, double latitude, String img, double longitude, Date createdAt) {
        this.sportsFacilityId = sportsFacilityId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.img = img;
        this.longitude = longitude;
        this.createdAt = createdAt;
    }

    public SportFacility() {
    }

    public int getSportsFacilityId() {
        return sportsFacilityId;
    }

    public void setSportsFacilityId(int sportsFacilityId) {
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
}
