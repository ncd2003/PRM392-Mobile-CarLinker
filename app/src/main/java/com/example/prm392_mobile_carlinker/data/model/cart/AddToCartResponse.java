package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class AddToCartResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private CartItem data;

    public AddToCartResponse() {
    }

    public AddToCartResponse(int status, String message, CartItem data) {
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

    public CartItem getData() {
        return data;
    }

    public void setData(CartItem data) {
        this.data = data;
    }
}

