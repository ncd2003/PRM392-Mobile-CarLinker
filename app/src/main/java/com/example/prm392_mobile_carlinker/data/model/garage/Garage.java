package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

/**
 * Model class đại diện cho thông tin một Garage
 */
public class Garage {
    @SerializedName("id")
    private int id;

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

    @SerializedName("image")
    private String image;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    // Khoảng cách tính toán từ vị trí hiện tại (km)
    private double distance;

    // Constructors
    public Garage() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Lấy latitude dạng double
     */
    public double getLatitudeDouble() {
        try {
            return Double.parseDouble(latitude);
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Lấy longitude dạng double
     */
    public double getLongitudeDouble() {
        try {
            return Double.parseDouble(longitude);
        } catch (Exception e) {
            return 0.0;
        }
    }
}

