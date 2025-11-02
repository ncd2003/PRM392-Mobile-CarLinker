package com.example.prm392_mobile_carlinker.data.model.order;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private OrderData data;

    public OrderResponse() {
    }

    public OrderResponse(int status, String message, OrderData data) {
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

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public static class OrderData {
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

        public OrderData() {
        }

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

        // Helper method to get order code/number
        public String getOrderCode() {
            return "ORD" + String.format("%06d", id);
        }
    }

    public static class OrderItem {
        @SerializedName("productVariant")
        private ProductVariant productVariant;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("unitPrice")
        private double unitPrice;

        @SerializedName("subtotal")
        private double subtotal;

        public OrderItem() {
        }

        public ProductVariant getProductVariant() {
            return productVariant;
        }

        public void setProductVariant(ProductVariant productVariant) {
            this.productVariant = productVariant;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }

    public static class ProductVariant {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("sku")
        private String sku;

        @SerializedName("price")
        private double price;

        @SerializedName("stockQuantity")
        private int stockQuantity;

        @SerializedName("holdQuantity")
        private int holdQuantity;

        @SerializedName("isDefault")
        private boolean isDefault;

        @SerializedName("selectedOptionValueIds")
        private String selectedOptionValueIds;

        public ProductVariant() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }

        public void setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
        }

        public int getHoldQuantity() {
            return holdQuantity;
        }

        public void setHoldQuantity(int holdQuantity) {
            this.holdQuantity = holdQuantity;
        }

        public boolean isDefault() {
            return isDefault;
        }

        public void setDefault(boolean aDefault) {
            isDefault = aDefault;
        }

        public String getSelectedOptionValueIds() {
            return selectedOptionValueIds;
        }

        public void setSelectedOptionValueIds(String selectedOptionValueIds) {
            this.selectedOptionValueIds = selectedOptionValueIds;
        }
    }
}
