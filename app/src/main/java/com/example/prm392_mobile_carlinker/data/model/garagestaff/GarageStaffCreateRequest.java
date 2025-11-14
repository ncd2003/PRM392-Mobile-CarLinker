package com.example.prm392_mobile_carlinker.data.model.garagestaff;

import com.google.gson.annotations.SerializedName;

public class GarageStaffCreateRequest {

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

    @SerializedName("garageRole")
    private int garageRole; // Maps to backend RoleGarage enum: 0=DEALER, 1=WAREHOUSE, 2=STAFF

    public GarageStaffCreateRequest() {
    }

    public GarageStaffCreateRequest(String email, String password, String fullName, String phoneNumber, int garageRole) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.garageRole = garageRole;
    }

    // Getters and Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGarageRole() {
        return garageRole;
    }

    public void setGarageRole(int garageRole) {
        this.garageRole = garageRole;
    }
}
