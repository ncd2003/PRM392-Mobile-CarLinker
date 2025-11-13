package com.example.prm392_mobile_carlinker.data.model.serviceitem;

public class ServiceItemCreateRequest {
    private String name;

    public ServiceItemCreateRequest() {
    }

    public ServiceItemCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

