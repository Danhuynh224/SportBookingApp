package com.example.pjbookingsport.model;

public class JWT {
    private String email;
    private String token;

    public JWT(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public JWT() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String value) {
        this.token = value;
    }
}
