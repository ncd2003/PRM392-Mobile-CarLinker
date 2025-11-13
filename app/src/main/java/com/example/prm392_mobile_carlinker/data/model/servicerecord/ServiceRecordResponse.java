package com.example.prm392_mobile_carlinker.data.model.servicerecord;

import com.google.gson.annotations.SerializedName;

public class ServiceRecordResponse {

    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ServiceRecordData data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceRecordData getData() {
        return data;
    }

    public void setData(ServiceRecordData data) {
        this.data = data;
    }

    public static class ServiceRecordData {
        @SerializedName("vehicleId")
        private int vehicleId;

        @SerializedName("serviceItems")
        private java.util.List<Integer> serviceItems;

        public int getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(int vehicleId) {
            this.vehicleId = vehicleId;
        }

        public java.util.List<Integer> getServiceItems() {
            return serviceItems;
        }

        public void setServiceItems(java.util.List<Integer> serviceItems) {
            this.serviceItems = serviceItems;
        }
    }
}
