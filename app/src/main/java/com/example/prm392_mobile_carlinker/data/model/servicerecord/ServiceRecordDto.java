package com.example.prm392_mobile_carlinker.data.model.servicerecord;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for Service Record
 */
public class ServiceRecordDto {
    @SerializedName("id")
    private int id;

    @SerializedName("serviceRecordStatus")
    private int serviceRecordStatus; // 0=Pending, 1=InProgress, 2=Completed, 3=Cancelled

    @SerializedName("totalCost")
    private double totalCost;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("user")
    private UserDto user;

    @SerializedName("vehicle")
    private VehicleDto vehicle;

    @SerializedName("serviceItems")
    private java.util.List<ServiceItemDto> serviceItems;

    // Constructors
    public ServiceRecordDto() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceRecordStatus() {
        return serviceRecordStatus;
    }

    public void setServiceRecordStatus(int serviceRecordStatus) {
        this.serviceRecordStatus = serviceRecordStatus;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
    }

    public java.util.List<ServiceItemDto> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(java.util.List<ServiceItemDto> serviceItems) {
        this.serviceItems = serviceItems;
    }

    /**
     * Get status text
     */
    public String getStatusText() {
        switch (serviceRecordStatus) {
            case 0:
                return "Chờ xử lý";
            case 1:
                return "Đang thực hiện";
            case 2:
                return "Hoàn thành";
            case 3:
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

    /**
     * Nested User DTO
     */
    public static class UserDto {
        @SerializedName("id")
        private int id;

        @SerializedName("fullName")
        private String fullName;

        @SerializedName("email")
        private String email;

        @SerializedName("phoneNumber")
        private String phoneNumber;

        @SerializedName("userRole")
        private int userRole;

        @SerializedName("userStatus")
        private int userStatus;

        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public int getUserRole() {
            return userRole;
        }

        public int getUserStatus() {
            return userStatus;
        }

        public String getImage() {
            return image;
        }
    }

    /**
     * Nested Vehicle DTO
     */
    public static class VehicleDto {
        @SerializedName("id")
        private int id;

        @SerializedName("licensePlate")
        private String licensePlate;

        @SerializedName("fuelType")
        private String fuelType;

        @SerializedName("transmissionType")
        private String transmissionType;

        @SerializedName("brand")
        private String brand;

        @SerializedName("model")
        private String model;

        @SerializedName("year")
        private int year;

        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public String getFuelType() {
            return fuelType;
        }

        public String getTransmissionType() {
            return transmissionType;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public int getYear() {
            return year;
        }

        public String getImage() {
            return image;
        }
    }

    /**
     * Nested Service Item DTO
     */
    public static class ServiceItemDto {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("price")
        private double price;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}

