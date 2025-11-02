package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class UpdateCartRequest {
    @SerializedName("productVariantId")
    private int productVariantId;

    @SerializedName("newQuantity")
    private int newQuantity;

    public UpdateCartRequest(int productVariantId, int newQuantity) {
        this.productVariantId = productVariantId;
        this.newQuantity = newQuantity;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }
}
