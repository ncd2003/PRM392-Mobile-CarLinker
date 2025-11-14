package com.example.prm392_mobile_carlinker.data.model.garagestaff;

import com.google.gson.annotations.SerializedName;

public class GarageStaffDetailResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private GarageStaffDto data;

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

    public GarageStaffDto getData() {
        return data;
    }

    public void setData(GarageStaffDto data) {
        this.data = data;
    }
}

