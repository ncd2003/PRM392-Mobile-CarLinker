package com.example.prm392_mobile_carlinker.data.model.servicecategory;

import java.util.List;

public class ServiceCategoryCreateRequest {
    private String name;
    private List<Integer> serviceItems;

    public ServiceCategoryCreateRequest() {
    }

    public ServiceCategoryCreateRequest(String name, List<Integer> serviceItems) {
        this.name = name;
        this.serviceItems = serviceItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<Integer> serviceItems) {
        this.serviceItems = serviceItems;
    }
}

