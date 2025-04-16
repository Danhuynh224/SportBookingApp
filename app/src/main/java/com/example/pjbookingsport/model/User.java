package com.example.pjbookingsport.model;

import com.example.pjbookingsport.enums.Role;

import java.time.LocalDate;

public class User {
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private String sex;

    private String address;

    private LocalDate birthday;

    private boolean isSave = false;

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public User(Long userId, String fullName, String email, String phone, Role role, String sex, String address, LocalDate birthday) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.sex = sex;
        this.address = address;
        this.birthday = birthday;
    }

    public User(Long userId, String fullName, String email, String phone, Role role, String sex, String address, LocalDate birthday, boolean isSave) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.sex = sex;
        this.address = address;
        this.birthday = birthday;
        this.isSave = isSave;
    }

    public User(Long userId, String fullName, String email, String phone, Role role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
