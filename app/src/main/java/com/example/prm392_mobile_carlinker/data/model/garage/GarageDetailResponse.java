package com.example.prm392_mobile_carlinker.data.model.garage;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for garage detail API
 */
public class GarageDetailResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private GarageDetail data;

    public GarageDetailResponse() {
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

    public GarageDetail getData() {
        return data;
    }

    public void setData(GarageDetail data) {
        this.data = data;
    }
}

