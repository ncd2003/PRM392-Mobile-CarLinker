package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("quantity")
    private int quantity;

    @SerializedName("addedDate")
    private String addedDate;

    @SerializedName("productVariant")
    private ProductVariant productVariant;

    // Constructors
    public CartItem() {
    }

    public CartItem(int quantity, String addedDate, ProductVariant productVariant) {
        this.quantity = quantity;
        this.addedDate = addedDate;
        this.productVariant = productVariant;
    }

    // Getters and Setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    // Helper methods
    public double getTotalPrice() {
        return productVariant != null ? productVariant.getPrice() * quantity : 0;
    }

    public String getProductName() {
        return productVariant != null ? productVariant.getName() : "";
    }

    public int getProductVariantId() {
        return productVariant != null ? productVariant.getId() : 0;
    }
}
