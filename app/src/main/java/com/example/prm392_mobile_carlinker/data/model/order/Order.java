package com.example.prm392_mobile_carlinker.data.model.order;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Order {
    @SerializedName("id")
    private int id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("enail")
    private String enail;

    @SerializedName("totalAmount")
    private double totalAmount;

    @SerializedName("status")
    private int status;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("shippingAddress")
    private String shippingAddress;

    @SerializedName("phoneNumber")
    private String phoneNumber;

    @SerializedName("orderDate")
    private String orderDate;

    @SerializedName("orderItems")
    private List<OrderItem> orderItems;

    public Order() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEnail() {
        return enail;
    }

    public void setEnail(String enail) {
        this.enail = enail;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Helper methods
    public String getOrderCode() {
        return "ORD" + String.format("%06d", id);
    }

    public String getStatusText() {
        switch (status) {
            case 0:
                return "Chờ xác nhận";
            case 1:
                return "Đã xác nhận";
            case 2:
                return "Đã đóng gói";
            case 3:
                return "Đang giao";
            case 4:
                return "Đã giao";
            case 5:
                return "Đã hủy";
            case 6:
                return "Giao thất bại";
            default:
                return "Không xác định";
        }
    }

    public int getTotalItemsCount() {
        if (orderItems == null) return 0;
        int total = 0;
        for (OrderItem item : orderItems) {
            total += item.getQuantity();
        }
        return total;
    }

    // Check if order can be cancelled (only PENDING, CONFIRMED, PACKED can be cancelled)
    public boolean canBeCancelled() {
        return status == 0 || status == 1 || status == 2;
    }
}
