package com.example.prm392_mobile_carlinker.data.model.servicecategory;

public class ServiceCategoryDetailResponse {
    private int status;
    private String message;
    private ServiceCategory data;

    public ServiceCategoryDetailResponse() {
    }

    public ServiceCategoryDetailResponse(int status, String message, ServiceCategory data) {
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

    public ServiceCategory getData() {
        return data;
    }

    public void setData(ServiceCategory data) {
        this.data = data;
    }
}

