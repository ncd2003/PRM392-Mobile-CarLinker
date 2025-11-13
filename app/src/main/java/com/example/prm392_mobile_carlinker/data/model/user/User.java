package com.example.prm392_mobile_carlinker.data.model.user;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int userRole;
    private int userStatus;
    private String image;

    public User() {
    }

    public User(int id, String fullName, String email, String phoneNumber, int userRole, int userStatus, String image) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Helper methods
    public String getUserRoleText() {
        switch (userRole) {
            case 0: return "Khách hàng";
            case 1: return "Garage";
            case 2: return "Admin";
            default: return "Không xác định";
        }
    }

    public String getUserStatusText() {
        switch (userStatus) {
            case 0: return "Đang hoạt động";
            case 1: return "Bị khóa";
            default: return "Không xác định";
        }
    }
}

