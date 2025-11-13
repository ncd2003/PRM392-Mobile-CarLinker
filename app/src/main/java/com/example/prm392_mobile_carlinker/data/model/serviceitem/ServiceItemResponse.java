package com.example.prm392_mobile_carlinker.data.model.serviceitem;

public class ServiceItemResponse {
    private int status;
    private String message;
    private ServiceItemPaginatedData data;

    public ServiceItemResponse() {
    }

    public ServiceItemResponse(int status, String message, ServiceItemPaginatedData data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceItemPaginatedData getData() {
        return data;
    }

    public void setData(ServiceItemPaginatedData data) {
        this.data = data;
    }
}
