package com.example.prm392_mobile_carlinker.data.model.auth;

import com.google.gson.annotations.SerializedName;

public class PartnerRegisterRequest {

    // Personal Information
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

    @SerializedName("confirmPassword")
    private String confirmPassword;

    // Garage Information
    @SerializedName("garageName")
    private String garageName;

    @SerializedName("garageEmail")
    private String garageEmail;

    @SerializedName("garagePhoneNumber")
    private String garagePhoneNumber;

    @SerializedName("description")
    private String description;

    @SerializedName("operatingTime")
    private String operatingTime;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public PartnerRegisterRequest() {
        this.operatingTime = "8:00 - 18:00"; // Default value
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getGarageName() {
        return garageName;
    }

    public void setGarageName(String garageName) {
        this.garageName = garageName;
    }

    public String getGarageEmail() {
        return garageEmail;
    }

    public void setGarageEmail(String garageEmail) {
        this.garageEmail = garageEmail;
    }

    public String getGaragePhoneNumber() {
        return garagePhoneNumber;
    }

    public void setGaragePhoneNumber(String garagePhoneNumber) {
        this.garagePhoneNumber = garagePhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(String operatingTime) {
        this.operatingTime = operatingTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

