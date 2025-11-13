package com.example.prm392_mobile_carlinker.data.model.servicecategory;

public class ServiceCategoryResponse {
    private int status;
    private String message;
    private ServiceCategoryPaginatedData data;

    public ServiceCategoryResponse() {
    }

    public ServiceCategoryResponse(int status, String message, ServiceCategoryPaginatedData data) {
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

    public ServiceCategoryPaginatedData getData() {
        return data;
    }

    public void setData(ServiceCategoryPaginatedData data) {
        this.data = data;
    }
}
