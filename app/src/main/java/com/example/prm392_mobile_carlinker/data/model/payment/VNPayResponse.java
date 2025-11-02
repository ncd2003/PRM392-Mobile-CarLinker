package com.example.prm392_mobile_carlinker.data.model.payment;

import com.google.gson.annotations.SerializedName;

public class VNPayResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String data; // Payment URL

    public VNPayResponse() {
    }

    public VNPayResponse(int status, String message, String data) {
        this.status = status;
        this.message = message;
        this.data = data;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // Helper method to get payment URL
    public String getPaymentUrl() {
        return data;
    }
}

