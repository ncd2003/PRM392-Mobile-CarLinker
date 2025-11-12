package com.example.prm392_mobile_carlinker.data.model.servicecategory;

import java.util.List;

public class ServiceCategoryResponse {
    private int status;
    private String message;
    private List<ServiceCategory> data;

    public ServiceCategoryResponse() {
    }

    public ServiceCategoryResponse(int status, String message, List<ServiceCategory> data) {
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

    public List<ServiceCategory> getData() {
        return data;
    }

    public void setData(List<ServiceCategory> data) {
        this.data = data;
    }
}

