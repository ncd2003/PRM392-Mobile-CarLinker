package com.example.prm392_mobile_carlinker.data.model.vehicle;

public class VehicleResponse {
    private int status;
    private String message;
    private Data data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Data getData() { return data; }

    public static class Data {
        private int id;
        private String licensePlate;
        private String fuelType;
        private String transmissionType;
        private String brand;
        private String model;
        private int year;
        private String image;

        public int getId() { return id; }
        public String getLicensePlate() { return licensePlate; }
        public String getFuelType() { return fuelType; }
        public String getTransmissionType() { return transmissionType; }
        public String getBrand() { return brand; }
        public String getModel() { return model; }
        public int getYear() { return year; }
        public String getImage() { return image; }
    }
}
