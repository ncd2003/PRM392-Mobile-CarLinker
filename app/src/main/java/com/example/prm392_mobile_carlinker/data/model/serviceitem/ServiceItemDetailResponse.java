package com.example.prm392_mobile_carlinker.data.model.serviceitem;

public class ServiceItemDetailResponse {
    private int status;
    private String message;
    private ServiceItemDto data;

    public ServiceItemDetailResponse() {
    }

    public ServiceItemDetailResponse(int status, String message, ServiceItemDto data) {
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

    public ServiceItemDto getData() {
        return data;
    }

    public void setData(ServiceItemDto data) {
        this.data = data;
    }
}
