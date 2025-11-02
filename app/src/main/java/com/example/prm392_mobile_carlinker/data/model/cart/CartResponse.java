package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<CartItem> data;

    // Constructors
    public CartResponse() {
    }

    public CartResponse(int status, String message, List<CartItem> data) {
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

    public List<CartItem> getData() {
        return data;
    }

    public void setData(List<CartItem> data) {
        this.data = data;
    }

    // Helper method to get total items count
    public int getTotalItems() {
        if (data == null) return 0;
        int total = 0;
        for (CartItem item : data) {
            total += item.getQuantity();
        }
        return total;
    }

    // Helper method to get total price
    public double getTotalPrice() {
        if (data == null) return 0;
        double total = 0;
        for (CartItem item : data) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
