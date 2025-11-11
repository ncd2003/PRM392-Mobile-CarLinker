package com.example.prm392_mobile_carlinker.data.model.vehicle;

public class VehicleRequest {
    private String licensePlate;
    private String fuelType;
    private String transmissionType;
    private String brand;
    private String model;
    private int year;
    private String imageFile;

    public VehicleRequest(String licensePlate, String fuelType, String transmissionType,
                          String brand, String model, int year, String imageFile) {
        this.licensePlate = licensePlate;
        this.fuelType = fuelType;
        this.transmissionType = transmissionType;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.imageFile = imageFile;
    }

    public String getLicensePlate() { return licensePlate; }
    public String getFuelType() { return fuelType; }
    public String getTransmissionType() { return transmissionType; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getImageFile() { return imageFile; }
}
