package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class ProductVariant {
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

    // Constructors
    public ProductVariant() {
    }

    public ProductVariant(int id, String name, String sku, double price, int stockQuantity,
                         int holdQuantity, boolean isDefault, String selectedOptionValueIds) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.holdQuantity = holdQuantity;
        this.isDefault = isDefault;
        this.selectedOptionValueIds = selectedOptionValueIds;
    }

    // Getters and Setters
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

