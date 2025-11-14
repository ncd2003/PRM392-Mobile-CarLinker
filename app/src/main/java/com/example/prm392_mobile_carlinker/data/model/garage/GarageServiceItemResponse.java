package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response model for garage service items
 */
public class GarageServiceItemResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<GarageUpdateServiceItem> data;

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

    public List<GarageUpdateServiceItem> getData() {
        return data;
    }

    public void setData(List<GarageUpdateServiceItem> data) {
        this.data = data;
    }
}

