package com.example.prm392_mobile_carlinker.data.model.garagestaff;

import com.google.gson.annotations.SerializedName;

public class GarageStaffDto {

    @SerializedName("id")
    private int id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("image")
    private String image;

    @SerializedName("garageRole")
    private int garageRole; // 0 = TECHNICIAN, 1 = MANAGER, etc.

    @SerializedName("userStatus")
    private int userStatus; // 0 = ACTIVE, 1 = INACTIVE, 2 = SUSPENDED

    @SerializedName("garageId")
    private int garageId;

    @SerializedName("garageName")
    private String garageName;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("createdAt")
    private String createdAt;

    // Getters and Setters
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGarageRole() {
        return garageRole;
    }

    public void setGarageRole(int garageRole) {
        this.garageRole = garageRole;
    }

    public String getGarageRoleString() {
        switch (garageRole) {
            case 0: return "Đại lý";      // DEALER
            case 1: return "Kho hàng";    // WAREHOUSE
            case 2: return "Nhân viên";   // STAFF
            default: return "Không xác định";
        }
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatusString() {
        switch (userStatus) {
            case 0: return "Hoạt động";
            case 1: return "Không hoạt động";
            case 2: return "Bị khóa";
            default: return "Không xác định";
        }
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to format created date
    public String getFormattedCreatedAt() {
        if (createdAt == null || createdAt.isEmpty()) {
            return "Không xác định";
        }
        try {
            // Parse ISO 8601 format: 2025-11-13T17:19:45.3450381+00:00
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            java.util.Date date = inputFormat.parse(createdAt.substring(0, 19));
            return outputFormat.format(date);
        } catch (Exception e) {
            return createdAt.substring(0, Math.min(10, createdAt.length()));
        }
    }
}
