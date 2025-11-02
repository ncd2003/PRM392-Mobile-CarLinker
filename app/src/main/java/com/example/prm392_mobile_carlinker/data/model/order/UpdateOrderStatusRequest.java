package com.example.prm392_mobile_carlinker.data.model.order;

public class UpdateOrderStatusRequest {
    private int orderId;
    private int newStatus;

    public UpdateOrderStatusRequest(int orderId, int newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }
}

