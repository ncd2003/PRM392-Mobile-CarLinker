package com.example.prm392_mobile_carlinker.data.model.garagestaff;

import com.google.gson.annotations.SerializedName;

public class GarageStaffUpdateRequest {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("garageRole")
    private Integer garageRole;

    @SerializedName("userStatus")
    private Integer userStatus; // 0 = ACTIVE, 1 = INACTIVE, 2 = SUSPENDED

    public GarageStaffUpdateRequest() {
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getGarageRole() {
        return garageRole;
    }

    public void setGarageRole(Integer garageRole) {
        this.garageRole = garageRole;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }
}

