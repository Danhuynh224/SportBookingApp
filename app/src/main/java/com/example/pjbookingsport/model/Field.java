package com.example.pjbookingsport.model;

public class Field {
    private String name;

    //Vĩ độ
    private double latitude;

    //Kinh độ
    private double longitude;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Field(String name, double latitude, double longitude, String img) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
