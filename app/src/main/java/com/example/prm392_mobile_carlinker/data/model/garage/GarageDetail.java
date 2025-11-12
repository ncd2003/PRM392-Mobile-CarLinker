package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for garage detail data
 */
public class GarageDetail {
    @SerializedName("garageDto")
    private Garage garageDto;

    @SerializedName("serviceCategories")
    private List<ServiceCategory> serviceCategories;

    @SerializedName("totalServiceItems")
    private int totalServiceItems;

    public GarageDetail() {
    }

    public Garage getGarageDto() {
        return garageDto;
    }

    public void setGarageDto(Garage garageDto) {
        this.garageDto = garageDto;
    }

    public List<ServiceCategory> getServiceCategories() {
        return serviceCategories;
    }

    public void setServiceCategories(List<ServiceCategory> serviceCategories) {
        this.serviceCategories = serviceCategories;
    }

    public int getTotalServiceItems() {
        return totalServiceItems;
    }

    public void setTotalServiceItems(int totalServiceItems) {
        this.totalServiceItems = totalServiceItems;
    }
}

