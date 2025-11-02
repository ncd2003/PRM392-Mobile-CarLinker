package com.example.prm392_mobile_carlinker.data.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderItem {
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

        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getSelectedOptionValueIds() {
            return selectedOptionValueIds;
        }

        public void setSelectedOptionValueIds(String selectedOptionValueIds) {
            this.selectedOptionValueIds = selectedOptionValueIds;
        }
    }
}
