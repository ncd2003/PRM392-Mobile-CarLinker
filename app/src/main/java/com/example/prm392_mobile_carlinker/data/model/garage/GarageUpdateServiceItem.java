package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

/**
 * Model for updating service items in a garage
 */
public class GarageUpdateServiceItem {

    @SerializedName("serviceItemId")
    private int serviceItemId;

    @SerializedName("price")
    private double price;

    public GarageUpdateServiceItem() {
    }

    public GarageUpdateServiceItem(int serviceItemId, double price) {
        this.serviceItemId = serviceItemId;
        this.price = price;
    }

    public int getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(int serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

