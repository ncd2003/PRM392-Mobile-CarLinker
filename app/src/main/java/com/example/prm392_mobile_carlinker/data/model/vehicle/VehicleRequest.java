package com.example.prm392_mobile_carlinker.data.model.vehicle;

import android.net.Uri;

public class VehicleRequest {
    private String licensePlate;
    private String fuelType;
    private String transmissionType;
    private String brand;
    private String model;
    private int year;
    private Uri imageUri; // Ảnh thật được chọn từ bộ nhớ

    public VehicleRequest(String licensePlate, String fuelType, String transmissionType,
                          String brand, String model, int year) {
        this.licensePlate = licensePlate;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getFuelType() { return fuelType; }
    public String getTransmissionType() { return transmissionType; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }

    public Uri getImageUri() { return imageUri; }
    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }
}