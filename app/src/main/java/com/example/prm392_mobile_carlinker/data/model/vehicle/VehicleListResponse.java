package com.example.prm392_mobile_carlinker.data.model.vehicle;

import java.util.List;

public class VehicleListResponse {
    private int status;
    private String message;
    private List<VehicleResponse.Data> data;

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<VehicleResponse.Data> getData() { return data; }
}
