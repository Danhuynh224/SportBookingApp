package com.example.pjbookingsport.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String username;
    private String password;

    public Account( String username,String password) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
