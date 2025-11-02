package com.example.prm392_mobile_carlinker.data.model.product;

import com.google.gson.annotations.SerializedName;

public class ProductDetailResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ProductDetail data;

    // Constructors
    public ProductDetailResponse() {
    }

    public ProductDetailResponse(int status, String message, ProductDetail data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
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

    public ProductDetail getData() {
        return data;
    }

    public void setData(ProductDetail data) {
        this.data = data;
    }
}

