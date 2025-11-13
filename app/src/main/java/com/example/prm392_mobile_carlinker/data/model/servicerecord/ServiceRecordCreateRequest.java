package com.example.prm392_mobile_carlinker.data.model.servicerecord;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ServiceRecordCreateRequest {

    @SerializedName("vehicleId")
    private int vehicleId;

    @SerializedName("serviceItems")
    private List<Integer> serviceItems;

    public ServiceRecordCreateRequest() {
    }

    public ServiceRecordCreateRequest(int vehicleId, List<Integer> serviceItems) {
        this.vehicleId = vehicleId;
        this.serviceItems = serviceItems;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public List<Integer> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<Integer> serviceItems) {
        this.serviceItems = serviceItems;
    }
}
