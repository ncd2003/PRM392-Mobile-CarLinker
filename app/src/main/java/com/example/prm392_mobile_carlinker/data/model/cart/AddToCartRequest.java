package com.example.prm392_mobile_carlinker.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {
    @SerializedName("productVariantId")
    private int productVariantId;

    @SerializedName("quantity")
    private int quantity;

    public AddToCartRequest(int productVariantId, int quantity) {
        this.productVariantId = productVariantId;
        this.quantity = quantity;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
