package com.example.prm392_mobile_carlinker.data.model.serviceitem;

public class ServiceItemUpdateRequest {
    private String name;

    public ServiceItemUpdateRequest() {
    }

    public ServiceItemUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
