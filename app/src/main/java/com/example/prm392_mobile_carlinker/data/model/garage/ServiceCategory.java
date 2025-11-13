package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for service category
 */
public class ServiceCategory {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("serviceItems")
    private List<ServiceItem> serviceItems;

    public ServiceCategory() {
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

