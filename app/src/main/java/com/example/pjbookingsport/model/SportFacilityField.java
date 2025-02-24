package com.example.pjbookingsport.model;

public class SportFacilityField {
    private int img;
    private String title;
    private String address;
    private float rating;

    public SportFacilityField(int img, String title, String address, float rating) {
        this.img = img;
        this.title = title;
        this.address = address;
        this.rating = rating;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
