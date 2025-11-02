package com.example.prm392_mobile_carlinker.data.model.order;

import com.google.gson.annotations.SerializedName;

public class CreateOrderRequest {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("shippingAddress")
    private String shippingAddress;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    public CreateOrderRequest(String fullName, String email, String shippingAddress,
                            String phoneNumber, String paymentMethod) {
        this.fullName = fullName;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
