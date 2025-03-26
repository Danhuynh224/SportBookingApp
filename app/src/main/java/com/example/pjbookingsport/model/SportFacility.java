package com.example.pjbookingsport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SportFacility implements Serializable {
    private int sportsFacilityId;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String img;
    private Date createdAt;
    private List<Price> prices = new ArrayList<>();
    private String detail;

    public SportFacility() {
    }

    public SportFacility(int sportsFacilityId, String name, String address, double latitude, double longitude, String img, Date createdAt, List<Price> prices, String detail) {
        this.sportsFacilityId = sportsFacilityId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.img = img;
        this.createdAt = createdAt;
        this.prices = prices;
        this.detail = detail;
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
