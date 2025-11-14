package com.example.prm392_mobile_carlinker.data.model.servicerecord;

import com.google.gson.annotations.SerializedName;

/**
 * Request to update service record status
 */
public class UpdateServiceRecordStatusRequest {
    @SerializedName("serviceRecordStatus")
    private int serviceRecordStatus;

    @SerializedName("totalCost")
    private Double totalCost;

    public UpdateServiceRecordStatusRequest(int serviceRecordStatus, Double totalCost) {
        this.serviceRecordStatus = serviceRecordStatus;
        this.totalCost = totalCost;
    }

    public int getServiceRecordStatus() {
        return serviceRecordStatus;
    }

    public void setServiceRecordStatus(int serviceRecordStatus) {
        this.serviceRecordStatus = serviceRecordStatus;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}

