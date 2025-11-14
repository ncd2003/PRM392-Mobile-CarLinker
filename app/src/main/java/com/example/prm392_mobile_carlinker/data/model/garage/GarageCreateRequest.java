package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

public class GarageCreateRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("description")
    private String description;

    @SerializedName("operatingTime")
    private String operatingTime;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public GarageCreateRequest() {
    }

    public GarageCreateRequest(String name, String email, String description, String operatingTime,
                              String phoneNumber, String latitude, String longitude) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.operatingTime = operatingTime;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

