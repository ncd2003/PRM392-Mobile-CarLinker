package com.example.prm392_mobile_carlinker.data.model.servicecategory;

import java.util.List;

public class ServiceCategory {
    private int id;
    private String name;
    private List<ServiceItem> serviceItems;

    public ServiceCategory() {
    }

    public ServiceCategory(int id, String name, List<ServiceItem> serviceItems) {
        this.id = id;
        this.name = name;
        this.serviceItems = serviceItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }
}

